package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer


class UndoButton(private val mMaxTransformations: Int, private var mTransformationCount: Int, pos: FloatArray) : Entity(pos, floatArrayOf(1f, .15f, .1f), 1) {

    private val mOffset = (mMaxTransformations - 1) * .15f / 2f


    private var mDarkBoxes = Array<Box?>(mMaxTransformations){null}.also {
        for(i in it.indices) {
            it[i] = Box(floatArrayOf(pos[0] - mOffset + .15f * i, pos[1], pos[2]), true)
        }
    }

    private var mLightBoxes = Array<Box?>(mMaxTransformations){null}.also {
        for(i in it.indices) {
            it[i] = Box(floatArrayOf(pos[0] - mOffset - .15f, pos[1], pos[2]), false)
        }
    }

    init {
        //set all boxes up to count to their positions
        //set remainder to final position
        for(i in mLightBoxes.indices) {
            if(i < mTransformationCount) {
                mLightBoxes[i]!!.setPosData(floatArrayOf(pos[0] - mOffset + (i) * .15f, pos[1], pos[2]))
                mLightBoxes[i]!!.setAlphaData(1f)
            }else {
                mLightBoxes[i]!!.setPosData(floatArrayOf(pos[0] - mOffset + (mTransformationCount - 1) * .15f, pos[1], pos[2]))
            }
        }
    }

    override fun fadeTo(newAlpha: Float) {
        super.fadeTo(newAlpha)
        for(box in mDarkBoxes) {
            box!!.fadeTo(newAlpha)
        }
        for(box in mLightBoxes) {
            box!!.fadeTo(newAlpha)
        }
    }

    override fun moveTo(newPos: FloatArray) {
        super.moveTo(newPos)
        val deltaX = newPos[0] - pos[0]
        val deltaY = newPos[1] - pos[1]
        val deltaZ = newPos[2] - pos[2]
        for(box in mDarkBoxes) {
            box!!.moveTo(floatArrayOf(box.pos[0] + deltaX, box.pos[1] + deltaY, box.pos[2] + deltaZ))
        }
        for(box in mLightBoxes) {
            box!!.moveTo(floatArrayOf(box.pos[0] + deltaX, box.pos[1] + deltaY, box.pos[2] + deltaZ))
        }
    }


    fun decrement() {
        for(i in mLightBoxes.indices) {
            if(i >= (mTransformationCount - 1)) {
                mLightBoxes[i]!!.moveTo(floatArrayOf(pos[0] - mOffset + (mTransformationCount - 2) * .15f, pos[1], pos[2]))
                if(i == (mTransformationCount - 1)) {
                    mLightBoxes[i]!!.fadeTo(0f)
                }
            }
        }

        mTransformationCount -= 1
    }

    fun increment() {
        for(i in mLightBoxes.indices) {
            if(i >= mTransformationCount) {
                mLightBoxes[i]!!.moveTo(floatArrayOf(pos[0] - mOffset + (mTransformationCount) * .15f, pos[1], pos[2]))
                if(i == mTransformationCount) {
                    mLightBoxes[i]!!.fadeTo(1f)
                }
            }
        }

        mTransformationCount += 1
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        for(box in mDarkBoxes) {
            box!!.onAnimationEnd()
        }
        for(box in mLightBoxes) {
            box!!.onAnimationEnd()
        }
    }

    override fun onUpdate(t: Float) {
        super.onUpdate(t)
        for(box in mDarkBoxes) {
            box!!.onUpdate(t)
        }
        for(box in mLightBoxes) {
            box!!.onUpdate(t)
        }
    }


    fun draw(vpMatrix: FloatArray) {

        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        //light boxes and dark boxes at same depth (along the z/camera-look-down axis, so depth buffer will stop drawing of dark boxes even though it's drawn last)
        for(box in mDarkBoxes) {
            box!!.draw(vpMatrix)
        }
        for(box in mLightBoxes) {
            box!!.draw(vpMatrix)
        }

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

    }
}