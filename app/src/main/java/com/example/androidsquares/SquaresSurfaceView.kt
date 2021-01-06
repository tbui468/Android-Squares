package com.example.androidsquares

import android.content.Context
import android.opengl.GLSurfaceView

import android.view.MotionEvent

class SquaresSurfaceView(context: Context): GLSurfaceView(context) {
    val renderer: SquaresRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = SquaresRenderer(context)
        setRenderer(renderer)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        var pair: CoordinatePair
        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                for(i in 0 until event.pointerCount) {
                    pair = screenToNormalizedCoords(event.getX(i), event.getY(i))
                    renderer.mInputQueue.add(InputData(TouchType.Tap, pair.x, pair.y, 0.3f))
                }
            }
        }
        return true
    }


    //normalize to -1 to 1 in both dimensions
    private fun screenToNormalizedCoords(screenX: Float, screenY: Float): CoordinatePair {
        return CoordinatePair(screenX * 2 / width - 1, -(screenY * 2 / height - 1))
    }

}