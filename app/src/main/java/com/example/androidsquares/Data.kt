package com.example.androidsquares

import android.opengl.Matrix

data class CoordinatePair(val x: Float, val y: Float)


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

enum class FractalType {
    Red, Blue, Green, Normal, Empty
}

enum class Surface(val value: Int) {
    Front(0), Back(1), Left(2), Right(3), Top(4), Bottom(5)
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
        Matrix.translateM(matrix, 0, 0f, 0f, -2f)
        Matrix.rotateM(matrix, 0, 180f, 0f, 1f, 0f)
    }

    it[Surface.Back.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, 0f, 0f, 2f)
    }

    it[Surface.Left.value] = FloatArray(16).also { matrix ->
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, -2f, 0f, 0f)
        Matrix.rotateM(matrix, 0, 270f, 0f, 1f, 0f)
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
        Matrix.rotateM(matrix, 0, 270f, 1f, 0f, 0f)
    }
}






