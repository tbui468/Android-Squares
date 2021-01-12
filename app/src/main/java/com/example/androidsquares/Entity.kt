package com.example.androidsquares

import android.opengl.Matrix
import kotlin.math.abs

//what was size again???
open class Entity(var pos: FloatArray, var scale: FloatArray, var size: Int) {
    private var COLLISION_PADDING = 1.3f //collisions boxes are 50% bigger than size
    private var fromPos = pos
    private var toPos = pos

    private var fromScale = scale
    private var toScale = scale

    var angle = 0f
    private var fromAngle = 0f
    private var toAngle = 0f
    var rotationAxis = floatArrayOf(0f, 2f, 1f)

    var alpha = 1f
    private var fromAlpha = 1f
    private var toAlpha = 1f

    fun calculateModelMatrix(): FloatArray {
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, pos[0], pos[1], pos[2])
        Matrix.rotateM(modelMatrix, 0, angle, rotationAxis[0], rotationAxis[1], rotationAxis[2])
        Matrix.scaleM(modelMatrix, 0, scale[0], scale[1], scale[2])
        return modelMatrix
    }

    open fun onUpdate(t: Float) {
        pos = floatArrayOf(fromPos[0] + (toPos[0] - fromPos[0]) * t, fromPos[1] + (toPos[1] - fromPos[1]) * t, fromPos[2] + (toPos[2] - fromPos[2]) * t)
        angle = fromAngle + (toAngle - fromAngle) * t
        alpha = fromAlpha + (toAlpha - fromAlpha) * t
    }

    fun makeInvisible() {
        alpha = 0f
        fromAlpha = 0f
        toAlpha = 0f
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
    fun rotateTo(newAngle: Float, axis: FloatArray) {
        fromAngle = angle
        toAngle = newAngle
        rotationAxis = axis
    }

    //returns lower left, and upper right corners of projected collision box on screen
    fun getScreenCoords(): FloatArray {
        //find distance bewteen transformed center of corner and use that as the box width
        val center = floatArrayOf(0f, 0f, 0f, 0f)
        Matrix.multiplyMV(center, 0, SquaresRenderer.mVPMatrix, 0, floatArrayOf(pos[0], pos[1], pos[2], 1f), 0)

        //need to project collision boxes too????? - answer: Yes
        //collision boxes are only 2 dimensions, so putting y dimension in for z as a placeholder for matrix/vector multiplication
        val corner = FloatArray(4)
        Matrix.multiplyMV(corner, 0, SquaresRenderer.mVPMatrix, 0,
                floatArrayOf(pos[0] + COLLISION_PADDING * size * scale[0]/2f,
                            pos[1] + COLLISION_PADDING * size * scale[1]/2f,
                                pos[2], 1f), 0)

        val halfDisX = abs(corner[0]/corner[3] - center[0]/center[3])
        val halfDisY = abs(corner[1]/corner[3] - center[1]/center[3])

        return floatArrayOf(center[0]/center[3] - halfDisX, center[1]/center[3] - halfDisY, center[0]/center[3] + halfDisX, center[1]/center[3] + halfDisY)
    }

    fun leftCollision(x: Float, y: Float): Boolean {
        val projBox = getScreenCoords()
        val cornerDim = abs(projBox[0] - projBox[2])/3f

        if(y < projBox[1] || y > projBox[3])
            return false

        if(x < projBox[0] + cornerDim && x > projBox[0])
            return true

        return false
    }

    fun rightCollision(x: Float, y: Float): Boolean {
        val projBox = getScreenCoords()
        val cornerDim = abs(projBox[0] - projBox[2])/3f

        if(y < projBox[1] || y > projBox[3])
            return false

        if(x > projBox[2] - cornerDim && x < projBox[2])
            return true

        return false
    }

    fun topCollision(x: Float, y: Float): Boolean {
        val projBox = getScreenCoords()
        val cornerDim = abs(projBox[1] - projBox[3])/3f

        if(x < projBox[0] || x > projBox[2])
            return false

        if(y > projBox[3] - cornerDim && y < projBox[3])
            return true

        return false
    }

    fun bottomCollision(x: Float, y: Float): Boolean {
        val projBox = getScreenCoords()
        val cornerDim = abs(projBox[1] - projBox[3])/3f

        if(x < projBox[0] || x > projBox[2])
            return false

        if(y < projBox[1] + cornerDim && y > projBox[1])
            return true

        return false
    }

    //note: center collision will overlap with edge collisions (top, bottom, left, right), so check those first before calling pointCollision
    fun centerCollision(x: Float, y: Float): Boolean {
        val projBox = getScreenCoords()

        //check if outside box
        if(x < projBox[0]) return false
        if(x > projBox[2]) return false
        if(y < projBox[1]) return false
        if(y > projBox[3]) return false

        return true
    }

    fun pointCollision(mouseX: Float, mouseY: Float): CollisionBox {
        val projBox = getScreenCoords()

        //check if outside box
        if(mouseX < projBox[0]) return CollisionBox.None
        if(mouseX > projBox[2]) return CollisionBox.None
        if(mouseY < projBox[1]) return CollisionBox.None
        if(mouseY > projBox[3]) return CollisionBox.None

        return CollisionBox.Center
    }


    //note: texture coords start from top left corner, whereas vertex coords start from lower left (going ccw as index increases)
    fun setTexture(vertices: FloatArray, size: Int, col: Int, row: Int, type: FractalType) {
        val textureIndex = IntArray(4)
        for(i in 0 until 4) {
            textureIndex[i] = (row * size + col) * FLOATS_PER_QUAD + 3 + i * 5
        }
        when (type) {
            FractalType.Normal -> {
                vertices[textureIndex[0]] = 0f
                vertices[textureIndex[0] + 1] = .25f
                vertices[textureIndex[1]] = .25f
                vertices[textureIndex[1] + 1] = .25f
                vertices[textureIndex[2]] = .25f
                vertices[textureIndex[2] + 1] = 0f
                vertices[textureIndex[3]] = 0f
                vertices[textureIndex[3] + 1] = 0f
            }
            FractalType.Red -> {
                vertices[textureIndex[0]] = .25f
                vertices[textureIndex[0] + 1] = .25f
                vertices[textureIndex[1]] = .5f
                vertices[textureIndex[1] + 1] = .25f
                vertices[textureIndex[2]] = .5f
                vertices[textureIndex[2] + 1] = 0f
                vertices[textureIndex[3]] = .25f
                vertices[textureIndex[3] + 1] = 0f
            }
            FractalType.Green -> {
                vertices[textureIndex[0]] = 0f
                vertices[textureIndex[0] + 1] = .5f
                vertices[textureIndex[1]] = .25f
                vertices[textureIndex[1] + 1] = .5f
                vertices[textureIndex[2]] = .25f
                vertices[textureIndex[2] + 1] = .25f
                vertices[textureIndex[3]] = 0f
                vertices[textureIndex[3] + 1] = .25f
            }
            FractalType.Blue -> {
                vertices[textureIndex[0]] = .25f
                vertices[textureIndex[0] + 1] = .5f
                vertices[textureIndex[1]] = .5f
                vertices[textureIndex[1] + 1] = .5f
                vertices[textureIndex[2]] = .5f
                vertices[textureIndex[2] + 1] = .25f
                vertices[textureIndex[3]] = .25f
                vertices[textureIndex[3] + 1] = .25f
            }
            FractalType.NormalB -> {
                vertices[textureIndex[0]] = .5f
                vertices[textureIndex[0] + 1] = .25f
                vertices[textureIndex[1]] = .75f
                vertices[textureIndex[1] + 1] = .25f
                vertices[textureIndex[2]] = .75f
                vertices[textureIndex[2] + 1] = 0f
                vertices[textureIndex[3]] = .5f
                vertices[textureIndex[3] + 1] = 0f
            }
            FractalType.RedB -> {
                vertices[textureIndex[0]] = .75f
                vertices[textureIndex[0] + 1] = .25f
                vertices[textureIndex[1]] = 1f
                vertices[textureIndex[1] + 1] = .25f
                vertices[textureIndex[2]] = 1f
                vertices[textureIndex[2] + 1] = 0f
                vertices[textureIndex[3]] = .75f
                vertices[textureIndex[3] + 1] = 0f
            }
            FractalType.GreenB -> {
                vertices[textureIndex[0]] = .5f
                vertices[textureIndex[0] + 1] = .5f
                vertices[textureIndex[1]] = .75f
                vertices[textureIndex[1] + 1] = .5f
                vertices[textureIndex[2]] = .75f
                vertices[textureIndex[2] + 1] = .25f
                vertices[textureIndex[3]] = .5f
                vertices[textureIndex[3] + 1] = .25f
            }
            FractalType.BlueB -> {
                vertices[textureIndex[0]] = .75f
                vertices[textureIndex[0] + 1] = .5f
                vertices[textureIndex[1]] = 1f
                vertices[textureIndex[1] + 1] = .5f
                vertices[textureIndex[2]] = 1f
                vertices[textureIndex[2] + 1] = .25f
                vertices[textureIndex[3]] = .75f
                vertices[textureIndex[3] + 1] = .25f
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