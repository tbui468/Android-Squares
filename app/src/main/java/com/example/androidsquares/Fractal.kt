//Fractals are the smallest object user can interact with (along with larger Squares and Cubes)
//all drawing from Cubes/Squares trickles down to the Fractal, which draws the basic quad

package com.example.androidsquares

import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

import android.opengl.Matrix
import android.opengl.GLES20

import android.util.Log

val vertices = floatArrayOf(-0.5f, -0.5f, 0.0f, 0f, 1f,
                            0.5f, -0.5f, 0.0f, 1f, 1f,
                            0.5f, 0.5f, 0.0f, 1f, 0f,
                            -0.5f, 0.5f, 0.0f, 0f, 0f)

val indices = shortArrayOf(0, 1, 2, 0, 2, 3)

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

class Fractal(initPos: FloatArray, initScale: FloatArray): Entity(), Drawable {

    private val mModelMatrix = FloatArray(16)

    init {
        //temp: caching model matrix using only translation for now (will need to integrate scaling and rotation)
        pos = initPos
        scale = initScale

        val scaleM = FloatArray(16)
        Matrix.setIdentityM(scaleM, 0)
        Matrix.scaleM(scaleM, 0, scale[0], scale[1], scale[2])

        val translationM = FloatArray(16)
        Matrix.setIdentityM(translationM, 0)
        Matrix.translateM(translationM, 0, pos[0], pos[1], pos[2])

        Matrix.multiplyMM(mModelMatrix, 0, translationM, 0, scaleM, 0)
    }

    //put vertices and indices into buffer
    private val mVertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(vertices)
            position(0)
        }
    }

    private val mIndexBuffer: ShortBuffer = ByteBuffer.allocateDirect(indices.size * SHORT_SIZE).run {
        order(ByteOrder.nativeOrder())
        asShortBuffer().apply {
            put(indices)
            position(0)
        }
    }

    private val mProgram: Int = SquaresRenderer.compileShaders(vertexShaderCode, fragmentShaderCode)


    override fun draw(vpMatrix: FloatArray) {

        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mModelMatrix, 0)

        GLES20.glUseProgram(mProgram)

        val posAttrib = GLES20.glGetAttribLocation(mProgram, "aPosition")
        GLES20.glEnableVertexAttribArray(posAttrib)
        mVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(posAttrib, 3, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer)

        val texCoordAttrib = GLES20.glGetAttribLocation(mProgram, "aTexCoord")
        GLES20.glEnableVertexAttribArray(texCoordAttrib)
        mVertexBuffer.position(3)
        GLES20.glVertexAttribPointer(texCoordAttrib, 2, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, SquaresRenderer.mTextureHandle)

        GLES20.glGetUniformLocation(mProgram, "uTexture").also {
            GLES20.glUniform1i(it, 0)
        }

        GLES20.glGetUniformLocation(mProgram, "uMVPMatrix").also {
            GLES20.glUniformMatrix4fv(it, 1, false, mvpMatrix, 0)
        }

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)

        GLES20.glDisableVertexAttribArray(posAttrib)
        GLES20.glDisableVertexAttribArray(texCoordAttrib)

    }
}