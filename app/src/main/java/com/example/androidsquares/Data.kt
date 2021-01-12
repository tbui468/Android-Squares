package com.example.androidsquares

import android.opengl.Matrix
import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.math.pow
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

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

enum class Screen {
    Set, Square, Fractal
}

enum class TouchType {
    Tap, PinchIn, PinchOut, FlickLeft, FlickRight, FlickDown, FlickUp
}

//Block types (postfixed with B) are darker than their non-block counterparts and cannot be transformed/split/merged
enum class FractalType {
    Red, Blue, Green, Normal, Empty, RedB, BlueB, GreenB, NormalB
}

//quads start at top left.  Left to right.  Top to bottom
//sizes 1x1, 2x2 and 4x4.  Cubes will consist of 6 4x4s that are transformed into correct position

val vertices1 = floatArrayOf(-0.5f, -0.5f, 0.0f, 0f, 1f,
                            0.5f, -0.5f, 0.0f, 1f, 1f,
                            0.5f, 0.5f, 0.0f, 1f, 0f,
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





val cubeLocations = arrayOf(floatArrayOf(0f, 3f, 0f),
                            floatArrayOf(0f, 1f, 0f),
                            floatArrayOf(0f, -1f, 0f),
                            floatArrayOf(0f, -3f, 0f))
/*
                            floatArrayOf(3f, 2f, 0f),
                            floatArrayOf(-3f, 2f, 0f),
                            floatArrayOf(0f, 2f, -3f),
                            floatArrayOf(0f, 2f, 3f))*/



fun calculateSquarePosition(setPos: FloatArray, squareIndex: Int): FloatArray {
    return floatArrayOf(setPos[0] + 1f * squareIndex, setPos[1], setPos[2])
}

//gets center of fractal of given size/index
fun calculateFractalPos(index: IntArray, size: Int, squareCenter: FloatArray): FloatArray {
    val SPACING = .35f
    val topLeftX = squareCenter[0] - SPACING * (3)/2f + SPACING * index[0]
    val topLeftY = squareCenter[1] + SPACING * (3)/2f - SPACING * index[1]
    val halfWidth = (size - 1) * SPACING / 2f
    return floatArrayOf(topLeftX + halfWidth, topLeftY - halfWidth, squareCenter[2])
}

fun calculateFractalPosForTarget(index: IntArray, size: Int, targetIndex: IntArray, targetSize: Int, squareCenter: FloatArray): FloatArray {
    val SPACING = .35f * size
    val targetCenter = calculateFractalPos(targetIndex, targetSize, squareCenter)
    val halfWidth = (targetSize - 1) * .25f / 2f

    return floatArrayOf(targetCenter[0] - halfWidth + .25f * (index[0] - targetIndex[0]) + (size - 1) * .25f/2f,
                        targetCenter[1] + halfWidth - .25f * (index[1] - targetIndex[1]) - (size -1) * .25f/2f, squareCenter[2])
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
