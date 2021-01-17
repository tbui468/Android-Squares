package com.example.androidsquares

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.GestureDetector
import androidx.core.view.GestureDetectorCompat

import kotlin.math.abs

class SquaresSurfaceView(context: Context): GLSurfaceView(context) {
    val renderer: SquaresRenderer
    var mDetector: GestureDetectorCompat

    init {
        setEGLContextClientVersion(2)
        renderer = SquaresRenderer(context)
        setRenderer(renderer)
        mDetector = GestureDetectorCompat(context, MyGestureListener(renderer))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mDetector.onTouchEvent(event)
    }

    private class MyGestureListener(renderer: SquaresRenderer): GestureDetector.SimpleOnGestureListener() {
        val mRenderer = renderer

        override fun onDown(event: MotionEvent): Boolean {
            //need to consume this event to trigger other events that follow
            return true
        }

        override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
            mRenderer.mInputQueue.add(InputData(TouchType.Tap, event.x, event.y, 0.3f))
            return true
        }
        override fun onDoubleTap(event: MotionEvent): Boolean {
            mRenderer.mInputQueue.add(InputData(TouchType.DoubleTap, event.x, event.y, 0.3f))
            return true
        }
        override fun onScroll(event1: MotionEvent, event2: MotionEvent, deltaX: Float, deltaY: Float): Boolean {
            if(abs(deltaX) > abs(deltaY)) { //horizontal motion
                if(deltaX < 0f)
                    mRenderer.mInputQueue.add(InputData(TouchType.FlickRight, event1.x, event1.y, 0.3f))
                else
                    mRenderer.mInputQueue.add(InputData(TouchType.FlickLeft, event1.x, event1.y, 0.3f))
            }else {
                if(deltaY < 0f)
                    mRenderer.mInputQueue.add(InputData(TouchType.FlickDown, event1.x, event1.y, 0.3f))
                else
                    mRenderer.mInputQueue.add(InputData(TouchType.FlickUp, event1.x, event1.y, 0.3f))
            }
            return true
        }



    }



}