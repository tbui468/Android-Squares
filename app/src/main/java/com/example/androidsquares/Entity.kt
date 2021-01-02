package com.example.androidsquares

import android.opengl.Matrix
import android.util.Log

interface Drawable {
    fun draw(vpMatrix: FloatArray)
}

open class Entity(boxWidth: Float, boxHeight: Float) {
    private var collisionBox = floatArrayOf(boxWidth, boxHeight)

    var pos = floatArrayOf(0f, 0f, 0f)
    private var fromPos = floatArrayOf(0f, 0f, 0f)
    private var toPos = floatArrayOf(0f, 0f, 0f)

    var scale = floatArrayOf(1f, 1f, 1f)
        set(newScale) {
            field = newScale
            collisionBox[0] *= (newScale[0] * 1.4f)
            collisionBox[1] *= (newScale[1] * 1.4f)
        }

    private var fromScale = floatArrayOf(1f, 1f, 1f)
    private var toScale = floatArrayOf(1f, 1f, 1f)

    var angle = floatArrayOf(0f, 0f, 0f)
    private var fromAngle = floatArrayOf(0f, 0f, 0f)
    private var toAngle = floatArrayOf(0f, 0f, 0f)

    fun moveTo(newPos: FloatArray) {
    }
    fun scaleTo(newScale: FloatArray) {
    }
    fun rotateTo(newAngle: FloatArray) {
    }
    fun pointCollision(mouseX: Float, mouseY: Float): Boolean {
        //project onto screen
        val viewProjCenter = floatArrayOf(0f, 0f, 0f, 0f)
        Matrix.multiplyMV(viewProjCenter, 0, SquaresRenderer.mVPMatrix, 0, floatArrayOf(pos[0], pos[1], pos[2], 1f), 0)

        if(mouseX < viewProjCenter[0]/viewProjCenter[3] - collisionBox[0] / 2) return false
        if(mouseX > viewProjCenter[0]/viewProjCenter[3] + collisionBox[0] / 2) return false
        if(mouseY < viewProjCenter[1]/viewProjCenter[3] - collisionBox[1] / 2) return false
        if(mouseY > viewProjCenter[1]/viewProjCenter[3] + collisionBox[1] / 2) return false
        return true
    }
}