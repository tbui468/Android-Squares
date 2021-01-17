package com.example.androidsquares

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.util.Log

import kotlin.math.sqrt
import kotlin.math.pow
import kotlin.math.abs

class SquaresSurfaceView(context: Context): GLSurfaceView(context) {
    val renderer: SquaresRenderer
    private val FLICK_THRESHOLD = 50f //if the distance from ACTION_DOWN to ACTION_UP < FLICK_THRESHOLD, register as a tap
    private val TAP_THRESHOLD = 50f
    private val PINCHIN_THRESHOLD = 100f //when first current coords.  Second finger compare distance bewteen second down and second current to first current
    private val PINCHOUT_THRESHOLD = 100f
    private var mFirstTouch = Array(3){floatArrayOf(-1f, -1f)} //down coords, current coords, up coords
    private var mSecondTouch = Array(3){floatArrayOf(-1f -1f)}
    private var mPinched = false
    private var mFlicked = false

    init {
        setEGLContextClientVersion(2)
        renderer = SquaresRenderer(context)
        setRenderer(renderer)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var pair: CoordinatePair

        when(event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                mFirstTouch = arrayOf(floatArrayOf(event.getX(event.actionIndex), event.getY(event.actionIndex)), floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f))
            }

            MotionEvent.ACTION_UP -> {
                mFirstTouch = arrayOf(mFirstTouch[0], mFirstTouch[1], floatArrayOf(event.getX(event.actionIndex), event.getY(event.actionIndex)))

                if(!mFlicked && pointDistance(mFirstTouch[0][0], mFirstTouch[0][1], mFirstTouch[2][0], mFirstTouch[2][1]) < TAP_THRESHOLD) {
                    pair = screenToNormalizedCoords(mFirstTouch[0][0], mFirstTouch[0][1])
                    renderer.mInputQueue.add(InputData(TouchType.Tap, pair.x, pair.y, 0.3f))
                }

                mFlicked = false
                mPinched = false //don't need this but just to cover all bases

                mFirstTouch = arrayOf(floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f))
            }

            //event.actionIndex is always 0, so need to iterate over pointer indices
            MotionEvent.ACTION_MOVE -> {
                for(i in 0 until event.pointerCount) {
                    if(i == 0) {
                        mFirstTouch = arrayOf(mFirstTouch[0], floatArrayOf(event.getX(i), event.getY(i)), floatArrayOf(-1f, -1f))
                    }else if(i == 1) {
                        mSecondTouch = arrayOf(mSecondTouch[0], floatArrayOf(event.getX(i), event.getY(i)), floatArrayOf(-1f, -1f))
                    }else {
                        break
                    }
                }


                if(!mFlicked && event.pointerCount == 1) {
                    //if over first touch down and first touch current  distance > threshold, register flick in correct direction  and set mTappedFlicked to false
                    val deltaX = mFirstTouch[1][0] - mFirstTouch[0][0]
                    val deltaY = mFirstTouch[1][1] - mFirstTouch[0][1]
                    if(pointDistance(mFirstTouch[0][0], mFirstTouch[0][1], mFirstTouch[1][0], mFirstTouch[1][1]) > FLICK_THRESHOLD) {
                        pair = screenToNormalizedCoords(mFirstTouch[0][0], mFirstTouch[0][1])
                        if(abs(deltaX) > abs(deltaY)) { //horizontal flick
                            if(deltaX > 0f) {
                                Log.d("kouch", "flicked right")
                                renderer.mInputQueue.add(InputData(TouchType.FlickRight, pair.x, pair.y, 0.3f))
                            }else {
                                Log.d("kouch", "flicked left")
                                renderer.mInputQueue.add(InputData(TouchType.FlickLeft, pair.x, pair.y, 0.3f))
                            }
                        }else { //vertical flick
                            if(deltaY > 0f) {
                                Log.d("kouch", "flicked down")
                                renderer.mInputQueue.add(InputData(TouchType.FlickDown, pair.x, pair.y, 0.3f))
                            }else {
                                Log.d("kouch", "flicked up")
                                renderer.mInputQueue.add(InputData(TouchType.FlickUp, pair.x, pair.y, 0.3f))
                            }
                        }
                        mFlicked = true
                    }
                }

                //if pinched
                if(!mPinched && event.pointerCount == 2) {
                    val downDis = pointDistance(mFirstTouch[1][0], mFirstTouch[1][1], mSecondTouch[0][0], mSecondTouch[0][1])
                    val currentDis = pointDistance(mFirstTouch[1][0], mFirstTouch[1][1], mSecondTouch[1][0], mSecondTouch[1][1])
                    if(downDis < currentDis) { //check pinch out threshold
                        if(abs(downDis - currentDis) > PINCHOUT_THRESHOLD) {
                            Log.d("kouch", "Pinchout")
                            val centerX = average(mFirstTouch[1][0], mSecondTouch[1][0])
                            val centerY = average(mFirstTouch[1][1], mSecondTouch[1][1])
                            pair = screenToNormalizedCoords(centerX, centerY)
                            renderer.mInputQueue.add(InputData(TouchType.PinchOut, pair.x, pair.y, 0.3f))
                            mPinched = true
                            mFlicked = true
                        }
                    }else { //check pinch in threshold
                        if(abs(downDis - currentDis) > PINCHIN_THRESHOLD) {
                            Log.d("kouch", "Pinchin")
                            val centerX = average(mFirstTouch[1][0], mSecondTouch[1][0])
                            val centerY = average(mFirstTouch[1][1], mSecondTouch[1][1])
                            pair = screenToNormalizedCoords(centerX, centerY)
                            renderer.mInputQueue.add(InputData(TouchType.PinchIn, pair.x, pair.y, 0.3f))
                            mPinched = true
                            mFlicked = true
                        }
                    }
                }
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if(event.pointerCount < 3) { //ignore third or higher touches
                    mSecondTouch = arrayOf(floatArrayOf(event.getX(event.actionIndex), event.getY(event.actionIndex)), floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f))
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                if(event.pointerCount < 3) { //ignore third or higher touches
                    mPinched = false
                    when(event.actionIndex) {
                        0 -> {
                            mFirstTouch = arrayOf(mFirstTouch[0], mFirstTouch[1], floatArrayOf(event.getX(event.actionIndex), event.getY(event.actionIndex)))
                            //process first touch event here (or set flags waiting for next finger to do its thing)
                            mFirstTouch = mSecondTouch
                            mSecondTouch = arrayOf(floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f))
                        }
                        1 -> {
                            mSecondTouch = arrayOf(mSecondTouch[0], mSecondTouch[1], floatArrayOf(event.getX(event.actionIndex), event.getY(event.actionIndex)))
                            //process second touch here (or set flags waiting for first touch to do its thing first)
                            mSecondTouch = arrayOf(floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f), floatArrayOf(-1f, -1f))
                        }
                    }
                }
            }

        }

        return true
    }


    private fun average(x1: Float, x2: Float): Float {
        return (x1 + x2) / 2f
    }

    //normalize to -1 to 1 in both dimensions
    private fun screenToNormalizedCoords(screenX: Float, screenY: Float): CoordinatePair {
        return CoordinatePair(screenX * 2 / width - 1, -(screenY * 2 / height - 1))
    }

}