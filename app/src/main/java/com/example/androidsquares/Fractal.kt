//Fractals are the smallest object user can interact with (along with larger Squares and Cubes)
//all drawing from Cubes/Squares trickles down to the Fractal, which draws the basic quad

package com.example.androidsquares

import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

import android.opengl.Matrix
import android.opengl.GLES20

val vertices = floatArrayOf(-0.5f, -0.5f, 0.0f,
                            0.5f, -0.5f, 0.0f,
                            0.5f, 0.5f, 0.0f,
                            -0.5f, 0.5f, 0.0f)

val indices = shortArrayOf(0, 1, 2, 0, 2, 3)

const val vertexShaderCode = "attribute vec4 aPosition;" +
                            "uniform mat4 uMVPMatrix;" +
                            "void main() {" +
                            "   gl_Position = uMVPMatrix * aPosition;" +
                            "}"

const val fragmentShaderCode = "precision mediump float;" +
                                "uniform vec4 uColor;" +
                                "void main() {" +
                                "   gl_FragColor = uColor;" +
                                "}"

const val FLOAT_SIZE = 4
const val SHORT_SIZE = 2

class Fractal(initPos: FloatArray, initScale: FloatArray): Entity(), Drawable {

    private val mColor = floatArrayOf(1f, 1f, 1f, 1f)
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

        GLES20.glGetAttribLocation(mProgram, "aPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(it, 3, GLES20.GL_FLOAT, false, 3 * FLOAT_SIZE, mVertexBuffer)

            GLES20.glGetUniformLocation(mProgram, "uMVPMatrix").also { matrixHandle ->
                GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0)
            }

            GLES20.glGetUniformLocation(mProgram, "uColor").also { colorHandle ->
                GLES20.glUniform4fv(colorHandle, 1, mColor, 0)
            }

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}