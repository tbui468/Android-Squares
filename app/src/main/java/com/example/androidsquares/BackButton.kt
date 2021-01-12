package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class BackButton(pos: FloatArray) : Entity(pos, floatArrayOf(.1f, .1f, .1f), 1) {

    private var mVertexBuffer: FloatBuffer = createFloatBuffer(vertices1)
    private var mIndexBuffer: ShortBuffer = createShortBuffer(indices1)
    private var mIndexCount = indices1.size
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