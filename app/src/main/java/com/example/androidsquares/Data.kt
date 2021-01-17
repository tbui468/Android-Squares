package com.example.androidsquares

import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.math.pow
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.util.Log

data class CoordinatePair(val x: Float, val y: Float)

data class InputData(val touchType: TouchType, val x: Float, val y: Float, var life: Float)



const val FLOAT_SIZE = 4
const val SHORT_SIZE = 2
const val FLOATS_PER_QUAD = 4 * 5 //four vertices, and 5 floats per vertex

enum class CollisionBox {
    None,
    Center,
    TopLeft,
    TopRight,
    BottomLeft,
    BottomRight,
    Top,
    Left,
    Right,
    Bottom
}

enum class Transformation(val value: Int) {
    None(0),
    TranslatePosX(1),
    TranslateNegX(2),
    TranslatePosY(3),
    TranslateNegY(4),
    RotateCW(5),
    RotateCCW(6),
    ReflectXTop(7),
    ReflectXBottom(8),
    ReflectYLeft(9),
    ReflectYRight(10);

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value }
    }
}

enum class Screen {
    Set, Square, Fractal
}

enum class TouchType {
    Tap, PinchIn, PinchOut, FlickLeft, FlickRight, FlickDown, FlickUp, DoubleTap
}

//Block types (postfixed with B) are darker than their non-block counterparts and cannot be transformed/split/merged
enum class FractalType {
    Red, Blue, Green, Normal, Empty, RedB, BlueB, GreenB, NormalB
}

//quads start at top left.  Left to right.  Top to bottom
//sizes 1x1, 2x2 and 4x4.  Cubes will consist of 6 4x4s that are transformed into correct position

//square Ojbect vertices and indices (not actually a square for historical reasons)
val squareVertices = FloatArray(5 * 4 * 24).also {
    for(quad in 0 until 24) {
        val left = -2f + (quad % 4).toFloat()
        val bottom = 2f - (quad / 4).toFloat()
        //bottom left vertex
        it[quad * 20 + 0] = left
        it[quad * 20 + 1] = bottom
        it[quad * 20 + 2] = 0f
        it[quad * 20 + 3] = 0f
        it[quad * 20 + 4] = 0f
        //bottom right vertex
        it[quad * 20 + 5] = left + 1f
        it[quad * 20 + 6] = bottom
        it[quad * 20 + 7] = 0f
        it[quad * 20 + 8] = 0f
        it[quad * 20 + 9] = 0f
        //top right vertex
        it[quad * 20 + 10] = left + 1f
        it[quad * 20 + 11] = bottom + 1f
        it[quad * 20 + 12] = 0f
        it[quad * 20 + 13] = 0f
        it[quad * 20 + 14] = 0f
        //top left vertex
        it[quad * 20 + 15] = left
        it[quad * 20 + 16] = bottom + 1f
        it[quad * 20 + 17] = 0f
        it[quad * 20 + 18] = 0f
        it[quad * 20 + 19] = 0f
    }
}

val squareIndices = ShortArray(6 * 24).also {
    var offset: Short = 0
    for(quad in 0 until 24) {
        it[quad * 6 + 0] = offset
        it[quad * 6 + 1] = (offset + 1).toShort()
        it[quad * 6 + 2] = (offset + 2).toShort()
        it[quad * 6 + 3] = offset
        it[quad * 6 + 4] = (offset + 2).toShort()
        it[quad * 6 + 5] = (offset + 3).toShort()
        offset = (offset + 4).toShort()
    }
}

val vertices1 = floatArrayOf(-0.5f, -0.5f, 0.0f, .0f, .25f,
                            0.5f, -0.5f, 0.0f, .25f, .25f,
                            0.5f, 0.5f, 0.0f, .25f, 0f,
                            -0.5f, 0.5f, 0.0f, 0f, 0f)

val indices1 = shortArrayOf(0, 1, 2, 0, 2, 3)

val vertices2 = floatArrayOf(-1f, 0f, 0.0f, 0f, .5f,
                            0f, 0f, 0.0f, .5f, .5f,
                            0f, 1f, 0.0f, .5f, 0f,
                            -1f, 1f, 0.0f, 0f, 0f,

                            0f, 0f, 0.0f, .5f, 1f,
                            1f, 0f, 0.0f, 1f, 1f,
                            1f, 1f, 0.0f, 1f, .5f,
                            0f, 1f, 0.0f, .5f, .5f,

                            -1f, -1f, 0.0f, 0f, 1f,
                            0f, -1f, 0.0f, .5f, 1f,
                            0f, 0f, 0.0f, .5f, .5f,
                            -1f, 0f, 0.0f, 0f, .5f,

                            0f, -1f, 0.0f, .5f, .5f,
                            1f, -1f, 0.0f, 1f, .5f,
                            1f, 0f, 0.0f, 1f, 0f,
                            0f, 0f, 0.0f, .5f, 0f)


val indices2 = shortArrayOf(0, 1, 2, 0, 2, 3,
                            4, 5, 6, 4, 6, 7,
                            8, 9, 10, 8, 10, 11,
                            12, 13, 14, 12, 14, 15)


val vertices4 = floatArrayOf(
                            //row 0
                            -2f, 1f, 0.0f, .5f, 1f,
                            -1f, 1f, 0.0f, 1f, 1f,
                            -1f, 2f, 0.0f, 1f, .5f,
                            -2f, 2f, 0.0f, .5f, .5f,

                            -1f, 1f, 0.0f, 0f, 1f,
                            0f, 1f, 0.0f, .5f, 1f,
                            0f, 2f, 0.0f, .5f, .5f,
                            -1f, 2f, 0.0f, 0f, .5f,

                            0f, 1f, 0.0f, 0f, .5f,
                            1f, 1f, 0.0f, .5f, .5f,
                            1f, 2f, 0.0f, .5f, 0f,
                            0f, 2f, 0.0f, 0f, 0f,

                            1f, 1f, 0.0f, 0f, .5f,
                            2f, 1f, 0.0f, .5f, .5f,
                            2f, 2f, 0.0f, .5f, 0f,
                            1f, 2f, 0.0f, 0f, 0f,

                            //row 1
                            -2f, 0f, 0.0f, 0f, .5f,
                            -1f, 0f, 0.0f, .5f, .5f,
                            -1f, 1f, 0.0f, .5f, 0f,
                            -2f, 1f, 0.0f, 0f, 0f,

                            -1f, 0f, 0.0f, 0f, .5f,
                            0f, 0f, 0.0f, .5f, .5f,
                            0f, 1f, 0.0f, .5f, 0f,
                            -1f, 1f, 0.0f, 0f, 0f,

                            0f, 0f, 0.0f, 0f, .5f,
                            1f, 0f, 0.0f, .5f, .5f,
                            1f, 1f, 0.0f, .5f, 0f,
                            0f, 1f, 0.0f, 0f, 0f,

                            1f, 0f, 0.0f, 0f, .5f,
                            2f, 0f, 0.0f, .5f, .5f,
                            2f, 1f, 0.0f, .5f, 0f,
                            1f, 1f, 0.0f, 0f, 0f,

                            //row 2
                            -2f, -1f, 0.0f, 0f, .5f,
                            -1f, -1f, 0.0f, .5f, .5f,
                            -1f, 0f, 0.0f, .5f, 0f,
                            -2f, 0f, 0.0f, 0f, 0f,

                            -1f, -1f, 0.0f, 0f, .5f,
                            0f, -1f, 0.0f, .5f, .5f,
                            0f, 0f, 0.0f, .5f, 0f,
                            -1f, 0f, 0.0f, 0f, 0f,

                            0f, -1f, 0.0f, 0f, .5f,
                            1f, -1f, 0.0f, .5f, .5f,
                            1f, 0f, 0.0f, .5f, 0f,
                            0f, 0f, 0.0f, 0f, 0f,

                            1f, -1f, 0.0f, 0f, .5f,
                            2f, -1f, 0.0f, .5f, .5f,
                            2f, 0f, 0.0f, .5f, 0f,
                            1f, 0f, 0.0f, 0f, 0f,

                            //row 3
                            -2f, -2f, 0.0f, 0f, .5f,
                            -1f, -2f, 0.0f, .5f, .5f,
                            -1f, -1f, 0.0f, .5f, 0f,
                            -2f, -1f, 0.0f, 0f, 0f,

                            -1f, -2f, 0.0f, 0f, .5f,
                            0f, -2f, 0.0f, .5f, .5f,
                            0f, -1f, 0.0f, .5f, 0f,
                            -1f, -1f, 0.0f, 0f, 0f,

                            0f, -2f, 0.0f, 0f, .5f,
                            1f, -2f, 0.0f, .5f, .5f,
                            1f, -1f, 0.0f, .5f, 0f,
                            0f, -1f, 0.0f, 0f, 0f,

                            1f, -2f, 0.0f, 0f, .5f,
                            2f, -2f, 0.0f, .5f, .5f,
                            2f, -1f, 0.0f, .5f, 0f,
                            1f, -1f, 0.0f, 0f, 0f
)


val indices4 = shortArrayOf(0, 1, 2, 0, 2, 3,
                            4, 5, 6, 4, 6, 7,
                            8, 9, 10, 8, 10, 11,
                            12, 13, 14, 12, 14, 15,

                            16, 17, 18, 16, 18, 19,
                            20, 21, 22, 20, 22, 23,
                            24, 25, 26, 24, 26, 27,
                            28, 29, 30, 28, 30, 31,

                            32, 33, 34, 32, 34, 35,
                            36, 37, 38, 36, 38, 39,
                            40, 41, 42, 40, 42, 43,
                            44, 45, 46, 44, 46, 47,

                            48, 49, 50, 48, 50, 51,
                            52, 53, 54, 52, 54, 55,
                            56, 57, 58, 56, 58, 59,
                            60, 61, 62, 60, 62, 63)




//4 cols, and 6 rows
fun calculateSquarePosition(setPos: FloatArray, squareIndex: Int): FloatArray {
    val col = squareIndex % 4
    val row = squareIndex / 4
    val hOffset = -1.5f * 3f / 2
    val vOffset = 1.5f * 3f / 2
    return floatArrayOf(hOffset + setPos[0] + 1.5f * col, vOffset + setPos[1] - 1.5f * row, setPos[2])
}



fun getElementsDim(elements: Array<FractalType>): IntArray {
    var width = 0
    var height = 0
    var col: Int
    var row: Int
    for(i in elements.indices) {
        if(elements[i] != FractalType.Empty) {
            col = i % 4
            row = i / 4
            if (col + 1 > width) width = col + 1
            if (row + 1 > height) height = row + 1
        }
    }

    return intArrayOf(width, height)
}

fun getPuzzleDim(setIndex: Int, puzzleIndex: Int): IntArray {
    val elements = appData.setData[setIndex].puzzleData[puzzleIndex]!!.elements

    return getElementsDim(elements)
}

//offset from square center position.  Offset if (0, 0) if the puzzle dimensions are 4x6
//if smaller than 4x6 need to shift right and down to center it
//to keep things simple, make all puzzles top-left start at col 0 and row 0.
fun calculatePuzzleOffset(width: Int, height: Int): FloatArray {
    val halfWidth = 3 * .35f / 2f
    val halfHeight = 5 * .35f / 2f

    val myHalfWidth = (width - 1) * .35f / 2f
    val myHalfHeight = (height - 1) * .35f / 2f

    return floatArrayOf(halfWidth - myHalfWidth, halfHeight - myHalfHeight)
}

//gets center of fractal of given size/index
fun calculateFractalPos(index: IntArray, size: Int, squareCenter: FloatArray, puzzleDim: IntArray): FloatArray {
    val flush = false
    val SPACING = if(flush) .25f
    else .35f
    val offset = calculatePuzzleOffset(puzzleDim[0], puzzleDim[1])
    val puzzleCenter = floatArrayOf(squareCenter[0] + offset[0], squareCenter[1] - offset[1], squareCenter[2])
    val topLeftX = puzzleCenter[0] - SPACING * (3)/2f + SPACING * index[0]
    val topLeftY = puzzleCenter[1] + SPACING * (5)/2f - SPACING * index[1]
    val halfWidth = (size - 1) * SPACING / 2f
    return floatArrayOf(topLeftX + halfWidth, topLeftY - halfWidth, puzzleCenter[2])
}

fun calculateFractalPosForTarget(index: IntArray, size: Int, targetIndex: IntArray, targetSize: Int, squareCenter: FloatArray, puzzleDim: IntArray): FloatArray {
    val targetCenter = calculateFractalPos(targetIndex, targetSize, squareCenter, puzzleDim)
    val halfWidth = (targetSize - 1) * .25f / 2f

    return floatArrayOf(targetCenter[0] - halfWidth + .25f * (index[0] - targetIndex[0]) + (size - 1) * .25f/2f,
                        targetCenter[1] + halfWidth - .25f * (index[1] - targetIndex[1]) - (size -1) * .25f/2f,
                        squareCenter[2])
}


//temp: move to Utility.kt
fun sigmoid(t: Float): Float {
    return 1f / (1f + exp(-15f * (t - .5f)))
}

fun pointDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
}

fun createFloatBuffer(floatArray: FloatArray): FloatBuffer {
    return ByteBuffer.allocateDirect(floatArray.size * FLOAT_SIZE).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(floatArray)
            position(0)
        }
    }
}

fun createShortBuffer(shortArray: ShortArray): ShortBuffer {
    return ByteBuffer.allocateDirect(shortArray.size * SHORT_SIZE).run {
        order(ByteOrder.nativeOrder())
        asShortBuffer().apply {
            put(shortArray)
            position(0)
        }
    }
}

//making my own assert bc android makes things difficult
//assert(false) will crash app. Look down trace call to see what function called assert(false)
fun myAssert(bool: Boolean, message: String) {
    if(!bool) {
        Log.d("assert", message)
        val array = FloatArray(1){0f}
        array[-10] = 10f
    }
}
