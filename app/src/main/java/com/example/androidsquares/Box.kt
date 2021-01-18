package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Box(pos: FloatArray, dark: Boolean) : Entity(pos, floatArrayOf(.15f, .15f, .1f), 1) {

    init {
        if(!dark) {
            setAlphaData(0f)
        }
    }


    val vertices = floatArrayOf(-0.5f, -0.5f, 0.0f, .0f, .25f,
            0.5f, -0.5f, 0.0f, .25f, .25f,
            0.5f, 0.5f, 0.0f, .25f, 0f,
            -0.5f, 0.5f, 0.0f, 0f, 0f).also {
        if(dark) {
            it[3] = .5f
            it[4] = .25f
            it[8] = .75f
            it[9] = .25f
            it[13] = .75f
            it[14] = 0f
            it[18] = .5f
            it[19] = 0f
        }
    }

    val indices = shortArrayOf(0, 1, 2, 0, 2, 3)

    private var mVertexBuffer: FloatBuffer = createFloatBuffer(vertices)
    private var mIndexBuffer: ShortBuffer = createShortBuffer(indices)
    private var mIndexCount = indices.size
    private var mModelMatrix = FloatArray(16)

    fun draw(vpMatrix: FloatArray) {
        mModelMatrix = calculateModelMatrix()

        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mModelMatrix, 0)

        ///////////////////////set data and draw //////////////////////////

        mVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(SquaresRenderer.mPosAttrib, 3, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer)

        mVertexBuffer.position(3)
        GLES20.glVertexAttribPointer(SquaresRenderer.mTexCoordAttrib, 2, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer)

        GLES20.glUniformMatrix4fv(SquaresRenderer.mModelUniform, 1, false, mvpMatrix, 0)

        GLES20.glBlendColor(1f, 1f, 1f, alpha)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexCount, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)
        GLES20.glBlendColor(1f, 1f, 1f, 1f)
    }
}

