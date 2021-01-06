package com.example.androidsquares

import android.opengl.Matrix
import kotlin.math.abs

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

open class Entity(var pos: FloatArray, var scale: FloatArray, var size: Int) {
    private var COLLISION_PADDING = 1.5f //collisions boxes are 50% bigger than size
    private var fromPos = pos
    private var toPos = pos

    private var fromScale = scale
    private var toScale = scale

    var angle = floatArrayOf(0f, 0f, 0f)
    private var fromAngle = floatArrayOf(0f, 0f, 0f)
    private var toAngle = floatArrayOf(0f, 0f, 0f)

    var alpha = 1f
    private var fromAlpha = 1f
    private var toAlpha = 1f

    open fun onUpdate(t: Float) {
        pos = floatArrayOf(fromPos[0] + (toPos[0] - fromPos[0]) * t, fromPos[1] + (toPos[1] - fromPos[1]) * t, fromPos[2] + (toPos[2] - fromPos[2]) * t)
        alpha = fromAlpha + (toAlpha - fromAlpha) * t
    }

    open fun onAnimationEnd() {
        pos = toPos
        fromPos = toPos
        scale = toScale
        fromScale = toScale
        angle = toAngle
        fromAngle = toAngle
        alpha = toAlpha
        fromAlpha = toAlpha
    }

    open fun fadeTo(newAlpha: Float) {
        fromAlpha = alpha
        toAlpha = newAlpha
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
        //find distance bewteen transformed center of corner and use that as the box width
        val center = floatArrayOf(0f, 0f, 0f, 0f)
        Matrix.multiplyMV(center, 0, SquaresRenderer.mVPMatrix, 0, floatArrayOf(pos[0], pos[1], pos[2], 1f), 0)

        //need to project collision boxes too????? - answer: Yes
        //collision boxes are only 2 dimensions, so putting y dimension in for z as a placeholder for matrix/vector multiplication
        val corner = FloatArray(4)
        Matrix.multiplyMV(corner, 0, SquaresRenderer.mVPMatrix, 0, floatArrayOf(pos[0] + COLLISION_PADDING * size * scale[0]/2f, pos[1], pos[2], 1f), 0)

        val halfDis = abs(corner[0]/corner[3] - center[0]/center[3])

        if(mouseX < center[0]/center[3] - halfDis) return false
        if(mouseX > center[0]/center[3] + halfDis) return false
        if(mouseY < center[1]/center[3] - halfDis) return false
        if(mouseY > center[1]/center[3] + halfDis) return false
        return true
    }
}