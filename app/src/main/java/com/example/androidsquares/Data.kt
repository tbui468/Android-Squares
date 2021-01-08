package com.example.androidsquares

import android.opengl.Matrix
import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.math.pow

data class CoordinatePair(val x: Float, val y: Float)

data class InputData(val touchType: TouchType, val x: Float, val y: Float, var life: Float)


const val vertexShaderCode = "attribute vec4 aPosition;" +
        "attribute vec2 aTexCoord;" +
        "uniform mat4 uMVPMatrix;" +
        "varying vec2 vTexCoord;" +
        "void main() {" +
        "   vTexCoord = aTexCoord;" +
        "   gl_Position = uMVPMatrix * aPosition;" +
        "}"

const val fragmentShaderCode = "precision mediump float;" +
        "uniform sampler2D uTexture;" +
        "varying vec2 vTexCoord;" +
        "void main() {" +
        "   gl_FragColor = texture2D(uTexture, vTexCoord);" +
        "}"

const val FLOAT_SIZE = 4
const val SHORT_SIZE = 2
const val FLOATS_PER_QUAD = 4 * 5 //four vertices, and 5 floats per vertex

enum class Screen {
    Cube, Square, Fractal
}

enum class TouchType {
    Tap, PinchIn, PinchOut, FlickLeft, FlickRight, FlickDown, FlickUp
}

enum class FractalType {
    Red, Blue, Green, Normal, Empty
}

enum class Surface(val value: Int) {
    Front(0), Back(1), Left(2), Right(3), Top(4), Bottom(5), None(6)
}

//quads start at top left.  Left to right.  Top to bottom
//sizes 1x1, 2x2 and 4x4.  Cubes will consist of 6 4x4s that are transformed into correct position

val vertices1 = floatArrayOf(-0.5f, -0.5f, 0.0f, 0f, .5f,
                            0.5f, -0.5f, 0.0f, .5f, .5f,
                            0.5f, 0.5f, 0.0f, .5f, 0f,
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


val surfaceModels = Array(6){FloatArray(16)}.also {
    it[Surface.Front.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, 0f, 0f, 2f)
    }

    it[Surface.Back.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, 0f, 0f, -2f)
        Matrix.rotateM(matrix, 0, 180f, 1f, 0f, 0f)
    }

    it[Surface.Left.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, -2f, 0f, 0f)
        Matrix.rotateM(matrix, 0, -90f, 1f, 0f, 0f)
        Matrix.rotateM(matrix, 0, -90f, 0f, 1f, 0f)
    }

    it[Surface.Right.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, 2f, 0f, 0f)
        Matrix.rotateM(matrix, 0, 90f, 0f, 1f, 0f)
    }

    it[Surface.Bottom.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, 0f, -2f, 0f)
        Matrix.rotateM(matrix, 0, 90f, 1f, 0f, 0f)
    }

    it[Surface.Top.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, 0f, 2f, 0f)
        Matrix.rotateM(matrix, 0, -90f, 1f, 0f, 0f)
    }
}

val puzzle00 = arrayOf(
        FractalType.Red, FractalType.Green, FractalType.Green, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Blue,
        FractalType.Green, FractalType.Red, FractalType.Empty, FractalType.Normal,
        FractalType.Blue, FractalType.Empty, FractalType.Green, FractalType.Blue)

val puzzle01 = arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Green, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Blue,
        FractalType.Empty, FractalType.Empty, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty)


val cubeData0: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}.also {
    it[Surface.Front.value] = puzzle00
    it[Surface.Back.value] = puzzle01
    it[Surface.Left.value] = Array(16){FractalType.Blue}
    it[Surface.Right.value] = Array(16){FractalType.Normal}
    it[Surface.Top.value] = Array(16){FractalType.Red}
    it[Surface.Bottom.value] = Array(16){FractalType.Green}
}

val cubeData1: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}.also {
    it[Surface.Front.value] = Array(16){FractalType.Normal}
    it[Surface.Back.value] = Array(16){FractalType.Blue}
    it[Surface.Left.value] = Array(16){FractalType.Blue}
    it[Surface.Right.value] = Array(16){FractalType.Blue}
    it[Surface.Top.value] = Array(16){FractalType.Normal}
    it[Surface.Bottom.value] = Array(16){FractalType.Normal}
}

val puzzleData = arrayOf(cubeData0, cubeData1)

val cubeLocations = arrayOf(floatArrayOf(-2f, 3f, 0f),
                            floatArrayOf(0f, 0f, 0f))
/*
                            floatArrayOf(-2f, -3f, 0f),
                            floatArrayOf(0f, 1.5f, 0f),
                            floatArrayOf(0f, -1.5f, 0f),
                            floatArrayOf(2f, 3f, 0f),
                            floatArrayOf(2f, 0f, 0f),
                            floatArrayOf(2f, -3f, 0f))*/

//location of surfaces with the front surface facing the user (when cube is unfolded)
//need cube pos, max margin, cube scale and cube object size
fun calculateSurfacePos(surface: Surface, cubePos: FloatArray): FloatArray {
    val cubeMaxMargin = 2.47f
    val cubeScale = floatArrayOf(.25f, .25f, .25f)
    val cubeSize = 4
    val zPos = cubePos[2] + cubeScale[0] * cubeSize / 2f
    return when(surface) {
        Surface.Front -> floatArrayOf(cubePos[0], cubePos[1] - .5f * cubeMaxMargin * cubeScale[0], zPos)
        Surface.Back -> floatArrayOf(cubePos[0], cubePos[1] + (1.5f * cubeMaxMargin + 2 * cubeSize) * cubeScale[0], zPos)
        Surface.Left -> floatArrayOf(cubePos[0] - (cubeMaxMargin + cubeSize) * cubeScale[0], cubePos[1] + (.5f * cubeMaxMargin + cubeSize) * cubeScale[0], zPos)
        Surface.Right -> floatArrayOf(cubePos[0] + (cubeMaxMargin + cubeSize) * cubeScale[0], cubePos[1] - (.5f * cubeMaxMargin) * cubeScale[0], zPos)
        Surface.Top -> floatArrayOf(cubePos[0], cubePos[1] + (.5f * cubeMaxMargin + cubeSize) * cubeScale[0], zPos)
        Surface.Bottom -> floatArrayOf(cubePos[0], cubePos[1] - (1.5f * cubeMaxMargin + cubeSize) * cubeScale[0], zPos)
        Surface.None -> floatArrayOf(0f, 0f, 0f) //temp: should be an assert or error
    }
}

//gets center of fractal of given size/index
fun calculateFractalPos(index: IntArray, size: Int, squareCenter: FloatArray): FloatArray {
    val SPACING = .4f
    val topLeftX = squareCenter[0] - SPACING * (3)/2f + SPACING * index[0]
    val topLeftY = squareCenter[1] + SPACING * (3)/2f - SPACING * index[1]
    val halfWidth = (size - 1) * SPACING / 2f
    return floatArrayOf(topLeftX + halfWidth, topLeftY - halfWidth, squareCenter[2])
}

fun calculateFractalPosForTarget(index: IntArray, size: Int, targetIndex: IntArray, targetSize: Int, squareCenter: FloatArray): FloatArray {
    val SPACING = .4f * size
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
