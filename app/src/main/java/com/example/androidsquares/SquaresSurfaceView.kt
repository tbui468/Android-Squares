package com.example.androidsquares

import android.content.Context
import android.view.MotionEvent

import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log

class SquaresSurfaceView(context: Context): GLSurfaceView(context) {
    private val renderer: SquaresRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = SquaresRenderer(context)
        //set configurations here
        setRenderer(renderer)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var pair: CoordinatePair
        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                for(i in 0 until event.pointerCount) {
                    pair = screenToWorldCoords(event.getX(i), event.getY(i))

                    //check cubes
                    for(cube in renderer.mCubes) {
                        if(cube.pointCollision(pair.x, pair.y)) {
                            renderer.openCube(cube)
                            return true
                        }
                    }

                    //check squares
                    for (square in renderer.mSquares) {
                        if (square.pointCollision(pair.x, pair.y)) {
                            renderer.mOpenSquareFlag = true
                            renderer.mOpenSquareIndex = square.mIndex
                            return true
                        }
                    }

                    if(renderer.mOpenSquareIndex != -1) {
                        renderer.mCloseSquareFlag = true
                        return true
                    }

                    if(renderer.mOpenCubeIndex != -1) {
                        renderer.mCloseCubeFlag = true
                        return true
                    }

                }
            }
        }
        return true
    }

    //normalize to -1 to 1 in both dimensions
    private fun screenToWorldCoords(screenX: Float, screenY: Float): CoordinatePair {
        return CoordinatePair(screenX * 2 / width - 1, -(screenY * 2 / height - 1))
    }

}