package com.example.androidsquares

import android.content.Context
import android.view.MotionEvent

import android.opengl.GLSurfaceView
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
                    if(renderer.mCubes[0].pointCollision(pair.x, pair.y)) {
                        //renderer.cube0.rotating = !renderer.cube0.rotating
                        renderer.mCamera.moveTo(floatArrayOf(renderer.mCubes[0].pos[0], renderer.mCubes[0].pos[1], -.5f))
                    }else if(renderer.mCubes[1].pointCollision(pair.x, pair.y)) {
                        //renderer.cube1.rotating = !renderer.cube1.rotating
                        renderer.mCamera.moveTo(floatArrayOf(renderer.mCubes[1].pos[0], renderer.mCubes[1].pos[1], -.5f))
                    }else {
                        renderer.mCamera.moveTo(floatArrayOf(0f, 0f, -3f))
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