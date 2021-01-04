package com.example.androidsquares

import android.opengl.Matrix
import android.util.Log

interface Transformable {
    fun draw(vpMatrix: FloatArray)
    fun setTexture(vertices: FloatArray, size: Int, col: Int, row: Int, type: FractalType) {
        val textureIndex = IntArray(4)
        for(i in 0 until 4) {
            textureIndex[i] = (row * size + col) * FLOATS_PER_QUAD + 3 + i * 5
        }
        when (type) {
            FractalType.Red -> {
                vertices[textureIndex[0]] = .5f
                vertices[textureIndex[0] + 1] = .5f
                vertices[textureIndex[1]] = 1f
                vertices[textureIndex[1] + 1] = .5f
                vertices[textureIndex[2]] = 1f
                vertices[textureIndex[2] + 1] = 0f
                vertices[textureIndex[3]] = .5f
                vertices[textureIndex[3] + 1] = 0f
            }
            FractalType.Green -> {
                vertices[textureIndex[0]] = 0f
                vertices[textureIndex[0] + 1] = 1f
                vertices[textureIndex[1]] = .5f
                vertices[textureIndex[1] + 1] = 1f
                vertices[textureIndex[2]] = .5f
                vertices[textureIndex[2] + 1] = .5f
                vertices[textureIndex[3]] = 0f
                vertices[textureIndex[3] + 1] = .5f
            }
            FractalType.Blue -> {
                vertices[textureIndex[0]] = .5f
                vertices[textureIndex[0] + 1] = 1f
                vertices[textureIndex[1]] = 1f
                vertices[textureIndex[1] + 1] = 1f
                vertices[textureIndex[2]] = 1f
                vertices[textureIndex[2] + 1] = .5f
                vertices[textureIndex[3]] = .5f
                vertices[textureIndex[3] + 1] = .5f
            }
            FractalType.Normal -> {
                vertices[textureIndex[0]] = 0f
                vertices[textureIndex[0] + 1] = .5f
                vertices[textureIndex[1]] = .5f
                vertices[textureIndex[1] + 1] = .5f
                vertices[textureIndex[2]] = .5f
                vertices[textureIndex[2] + 1] = 0f
                vertices[textureIndex[3]] = 0f
                vertices[textureIndex[3] + 1] = 0f
            }
            FractalType.Empty -> {
                //do nothing
            }
        }
    }
    fun findEmptyFractalCount(elements: Array<FractalType>): Int {
        var count = 0
        for(element in elements) {
            if(element == FractalType.Empty) count++
        }
        return count
    }
}

open class Entity(var pos: FloatArray, scale: FloatArray, var objectSize: FloatArray) {
    private var fromPos = pos
    private var toPos = pos

    var collisionBox = floatArrayOf(scale[0] * objectSize[0] * 1.4f, scale[1] * objectSize[1] * 1.4f)

    var scale = scale
        set(newScale) {
            field = newScale
            collisionBox = floatArrayOf(newScale[0] * objectSize[0] * 1.4f, newScale[1] * objectSize[1] * 1.4f)
        }


    private var fromScale = scale
    private var toScale = scale

    var angle = floatArrayOf(0f, 0f, 0f)
    private var fromAngle = floatArrayOf(0f, 0f, 0f)
    private var toAngle = floatArrayOf(0f, 0f, 0f)

    open fun onUpdate(t: Float) {
        pos = floatArrayOf(fromPos[0] + (toPos[0] - fromPos[0]) * t, fromPos[1] + (toPos[1] - fromPos[1]) * t, fromPos[2] + (toPos[2] - fromPos[2]) * t)
    }

    open fun onAnimationEnd() {
        pos = toPos
        scale = toScale
        angle = toAngle
    }

    open fun moveTo(newPos: FloatArray) {
        fromPos = pos
        toPos = newPos
    }
    fun scaleTo(newScale: FloatArray) {
        fromScale = scale
        toScale = newScale
    }
    fun rotateTo(newAngle: FloatArray) {
        fromAngle = angle
        toAngle = newAngle
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