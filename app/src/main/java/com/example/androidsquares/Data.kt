package com.example.androidsquares

import android.opengl.Matrix

enum class FractalType {
    Red, Blue, Green, Normal, Empty
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


//cube face format
//when a cube is unfolded and the 6 surfaces are projected onto the screen,
//the top left of each square surface is index 0,0
//orient the cube surfaces such that unfolding will produce this orientation

/* this is the order of faces when they unfold
  6
1234
 5
 */

val indicesCube1 = ShortArray(96) { (indices4[it] + 0).toShort() }
val indicesCube2 = ShortArray(96) { (indices4[it] + 64).toShort() }
val indicesCube3 = ShortArray(96) { (indices4[it] + 128).toShort() }
val indicesCube4 = ShortArray(96) { (indices4[it] + 192).toShort() }
val indicesCube5 = ShortArray(96) { (indices4[it] + 256).toShort() }
val indicesCube6 = ShortArray(96) { (indices4[it] + 320).toShort() }

val indicesCube = indicesCube1 + indicesCube2 + indicesCube3 + indicesCube4 + indicesCube5 + indicesCube6

//temp: making 4 sides in cube (ignoring top/bottom for now)
val verticesCube = FloatArray(64 * 6 * 5).also {
    val matrix1 = FloatArray(16)
    val matrix2 = FloatArray(16)
    val matrix3 = FloatArray(16)
    val matrix4 = FloatArray(16)
    val matrix5 = FloatArray(16)
    val matrix6 = FloatArray(16)
    Matrix.setIdentityM(matrix1, 0)
    Matrix.setIdentityM(matrix2, 0)
    Matrix.setIdentityM(matrix3, 0)
    Matrix.setIdentityM(matrix4, 0)
    Matrix.setIdentityM(matrix5, 0)
    Matrix.setIdentityM(matrix6, 0)


    Matrix.translateM(matrix1, 0, 0f, 0f, 2f)

    Matrix.translateM(matrix2, 0, 0f, 0f, -2f)
    Matrix.rotateM(matrix2, 0, 180f, 0f, 1f, 0f)

    Matrix.translateM(matrix3, 0, -2f, 0f, 0f)
    Matrix.rotateM(matrix3, 0, 270f, 0f, 1f, 0f)

    Matrix.translateM(matrix4, 0, 2f, 0f, 0f)
    Matrix.rotateM(matrix4, 0, 90f, 0f, 1f, 0f)

    //bottom
    Matrix.translateM(matrix5, 0, 0f, -2f, 0f)
    Matrix.rotateM(matrix5, 0, 90f, 1f, 0f, 0f)

    //top
    Matrix.translateM(matrix6, 0, 0f, 2f, 0f)
    Matrix.rotateM(matrix6, 0, 270f, 1f, 0f, 0f)

    //grab each of the vertices in vertices4
    var index: Int
    var vertexPos: FloatArray
    val offset = intArrayOf(0*5, 64*5, 128*5, 192*5, 256*5, 320*5)
    val newVertex1 = FloatArray(4)
    val newVertex2 = FloatArray(4)
    val newVertex3 = FloatArray(4)
    val newVertex4 = FloatArray(4)
    val newVertex5 = FloatArray(4)
    val newVertex6 = FloatArray(4)

    for(i in 0 until 64) {
        index = i * 5

        vertexPos = floatArrayOf(vertices4[index], vertices4[index + 1], vertices4[index + 2], 1f)
        Matrix.multiplyMV(newVertex1, 0, matrix1, 0, vertexPos, 0)
        Matrix.multiplyMV(newVertex2, 0, matrix2, 0, vertexPos, 0)
        Matrix.multiplyMV(newVertex3, 0, matrix3, 0, vertexPos, 0)
        Matrix.multiplyMV(newVertex4, 0, matrix4, 0, vertexPos, 0)
        Matrix.multiplyMV(newVertex5, 0, matrix5, 0, vertexPos, 0)
        Matrix.multiplyMV(newVertex6, 0, matrix6, 0, vertexPos, 0)

        it[offset[0] + index + 0] = newVertex1[0]
        it[offset[0] + index + 1] = newVertex1[1]
        it[offset[0] + index + 2] = newVertex1[2]
        it[offset[0] + index + 3] = vertices4[index + 3]
        it[offset[0] + index + 4] = vertices4[index + 4]

        it[offset[1] + index + 0] = newVertex2[0]
        it[offset[1] + index + 1] = newVertex2[1]
        it[offset[1] + index + 2] = newVertex2[2]
        it[offset[1] + index + 3] = vertices4[index + 3]
        it[offset[1] + index + 4] = vertices4[index + 4]

        it[offset[2] + index + 0] = newVertex3[0]
        it[offset[2] + index + 1] = newVertex3[1]
        it[offset[2] + index + 2] = newVertex3[2]
        it[offset[2] + index + 3] = vertices4[index + 3]
        it[offset[2] + index + 4] = vertices4[index + 4]

        it[offset[3] + index + 0] = newVertex4[0]
        it[offset[3] + index + 1] = newVertex4[1]
        it[offset[3] + index + 2] = newVertex4[2]
        it[offset[3] + index + 3] = vertices4[index + 3]
        it[offset[3] + index + 4] = vertices4[index + 4]

        it[offset[4] + index + 0] = newVertex5[0]
        it[offset[4] + index + 1] = newVertex5[1]
        it[offset[4] + index + 2] = newVertex5[2]
        it[offset[4] + index + 3] = vertices4[index + 3]
        it[offset[4] + index + 4] = vertices4[index + 4]

        it[offset[5] + index + 0] = newVertex6[0]
        it[offset[5] + index + 1] = newVertex6[1]
        it[offset[5] + index + 2] = newVertex6[2]
        it[offset[5] + index + 3] = vertices4[index + 3]
        it[offset[5] + index + 4] = vertices4[index + 4]


    }

}

