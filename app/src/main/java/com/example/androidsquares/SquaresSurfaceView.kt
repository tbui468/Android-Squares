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
                    if(renderer.cube0.pointCollision(pair.x, pair.y)) renderer.cube0.rotating = !renderer.cube0.rotating
                    if(renderer.cube1.pointCollision(pair.x, pair.y)) renderer.cube1.rotating = !renderer.cube1.rotating
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