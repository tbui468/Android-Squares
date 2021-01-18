package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer


class UndoButton(pos: FloatArray) : Entity(pos, floatArrayOf(1f, .15f, .1f), 1) {

    private var transformationCount = 0
    /*

    private var mVertexBuffer: FloatBuffer = createFloatBuffer(vertices1.copyOf())
    private var mIndexBuffer: ShortBuffer = createShortBuffer(indices1.copyOf())
    private var mIndexCount = indices1.size
    private var mModelMatrix = FloatArray(16)*/

    private var mDarkBoxes = arrayOf(
            Box(floatArrayOf(pos[0] - .15f, pos[1], pos[2]), true),
            Box(floatArrayOf(pos[0], pos[1], pos[2]), true),
            Box(floatArrayOf(pos[0] + .15f, pos[1], pos[2]), true))
    private var mLightBoxes = arrayOf(
            Box(floatArrayOf(pos[0] - .3f, pos[1], pos[2]), false),
            Box(floatArrayOf(pos[0] - .15f, pos[1], pos[2]), false),
            Box(floatArrayOf(pos[0], pos[1], pos[2]), false))

    override fun moveTo(newPos: FloatArray) {
        super.moveTo(newPos)
        val deltaX = newPos[0] - pos[0]
        val deltaY = newPos[1] - pos[1]
        val deltaZ = newPos[2] - pos[2]
        for(box in mDarkBoxes) {
            box.moveTo(floatArrayOf(box.pos[0] + deltaX, box.pos[1] + deltaY, box.pos[2] + deltaZ))
        }
        for(box in mLightBoxes) {
            box.moveTo(floatArrayOf(box.pos[0] + deltaX, box.pos[1] + deltaY, box.pos[2] + deltaZ))
        }
    }


    fun decrement() {
        when(transformationCount) {
            0 -> {
                myAssert(false, "trying to decrement below zero!")
            }
            1 -> {
                mLightBoxes[0].moveTo(floatArrayOf(pos[0] - .3f, pos[1], pos[2]))
                mLightBoxes[0].fadeTo(0f)
            }
            2 -> {
                mLightBoxes[1].moveTo(floatArrayOf(pos[0] - .15f, pos[1], pos[2]))
                mLightBoxes[1].fadeTo(0f)
            }
            3 -> {
                mLightBoxes[2].moveTo(floatArrayOf(pos[0], pos[1], pos[2]))
                mLightBoxes[2].fadeTo(0f)
            }
        }
        transformationCount -= 1
    }

    fun increment() {
        when(transformationCount) {
            0 -> {
                mLightBoxes[0].moveTo(floatArrayOf(pos[0] - .15f, pos[1], pos[2]))
                mLightBoxes[0].fadeTo(1f)
            }
            1 -> {
                mLightBoxes[1].moveTo(floatArrayOf(pos[0], pos[1], pos[2]))
                mLightBoxes[1].fadeTo(1f)
            }
            2 -> {
                mLightBoxes[2].moveTo(floatArrayOf(pos[0] + .15f, pos[1], pos[2]))
                mLightBoxes[2].fadeTo(1f)
            }
            3 -> {
                myAssert(false, "trying to increment after max transformations!")
            }
        }
        transformationCount += 1
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        for(box in mDarkBoxes) {
            box.onAnimationEnd()
        }
        for(box in mLightBoxes) {
            box.onAnimationEnd()
        }
    }

    override fun onUpdate(t: Float) {
        super.onUpdate(t)
        for(box in mDarkBoxes) {
            box.onUpdate(t)
        }
        for(box in mLightBoxes) {
            box.onUpdate(t)
        }
    }


    fun draw(vpMatrix: FloatArray) {

        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        //light boxes and dark boxes at same depth (along the z/camera-look-down axis, so depth buffer will stop drawing of dark boxes even though it's drawn last)
        for(box in mDarkBoxes) {
            box.draw(vpMatrix)
        }
        for(box in mLightBoxes) {
            box.draw(vpMatrix)
        }

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

    }
}