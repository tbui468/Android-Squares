package com.example.androidsquares

import android.opengl.Matrix
import kotlin.math.exp

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


val cubeData0: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}.also {
    it[Surface.Front.value] = Array(16){FractalType.Red}
    it[Surface.Back.value] = Array(16){FractalType.Green}
    it[Surface.Left.value] = Array(16){FractalType.Blue}
    it[Surface.Right.value] = Array(16){FractalType.Normal}
    it[Surface.Top.value] = Array(16){FractalType.Red}
    it[Surface.Bottom.value] = Array(16){FractalType.Green}
}

val cubeLocations = arrayOf(floatArrayOf(-3f, 4.5f, 0f),
                            floatArrayOf(-3f, 0f, 0f),
                            floatArrayOf(-3f, -4.5f, 0f),
                            floatArrayOf(0f, 2.25f, 0f),
                            floatArrayOf(0f, -2.25f, 0f),
                            floatArrayOf(3f, 4.5f, 0f),
                            floatArrayOf(3f, 0f, 0f),
                            floatArrayOf(3f, -4.5f, 0f))

//location of surfaces with the front surface facing the user (when cube is unfolded)
//need cube pos, max margin, cube scale and cube object size
fun calculateSurfacePos(surface: Surface, cubePos: FloatArray, cubeMaxMargin: Float, cubeScale: FloatArray, cubeSize: Int): FloatArray {
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

fun calculateFractalPos(index: IntArray, size: Int, targetIndex: IntArray, targetSize: Int, center: FloatArray): FloatArray {
    val spacing = if(targetSize == 4) {
        .25f //size of fractal (1x1)
    }else { //==1
        .4f //size of above times golden ratio
    }
    return floatArrayOf(center[0] + spacing * (index[0] - 2) + spacing/2, center[1] + spacing * (index[1] - 2) + spacing/2, center[2])
}


//temp: move to Utility.kt
fun sigmoid(t: Float): Float {
    return 1f / (1f + exp(-15f * (t - .5f)))
}

