//Fractals are the smallest object user can interact with (along with larger Squares and Cubes)
//all drawing from Cubes/Squares trickles down to the Fractal, which draws the basic quad

package com.example.androidsquares

import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

import android.opengl.Matrix
import android.opengl.GLES20


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

class Fractal(size: Int): Entity(), Drawable {

    lateinit var mVertices: FloatArray
    lateinit var mIndices: ShortArray
    private var mVertexBuffer: FloatBuffer
    private var mIndexBuffer: ShortBuffer
    private val mModelMatrix = FloatArray(16)

    init {
        when(size) {
            1 -> {
                mVertices = vertices1
                mIndices = indices1
            }
            2 -> {
                mVertices = vertices2
                mIndices = indices2
            }
            4 -> {
                mVertices = vertices4
                mIndices = indices4
            }
            else -> {
                mVertices = verticesCube
                mIndices = indicesCube
            }
        }

        //put vertices and indices into buffer
        mVertexBuffer = ByteBuffer.allocateDirect(mVertices.size * FLOAT_SIZE).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(mVertices)
                position(0)
            }
        }

        mIndexBuffer = ByteBuffer.allocateDirect(mIndices.size * SHORT_SIZE).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(mIndices)
                position(0)
            }
        }
    }


    private val mProgram: Int = SquaresRenderer.compileShaders(vertexShaderCode, fragmentShaderCode)


    override fun draw(vpMatrix: FloatArray) {

        val scaleM = FloatArray(16)
        Matrix.setIdentityM(scaleM, 0)
        Matrix.scaleM(scaleM, 0, scale[0], scale[1], scale[2])

        val rotateM = FloatArray(16)
        Matrix.setIdentityM(rotateM, 0)
        Matrix.rotateM(rotateM, 0, angle[2], 0f, 1f, 2f)

        val translationM = FloatArray(16)
        Matrix.setIdentityM(translationM, 0)
        Matrix.translateM(translationM, 0, pos[0], pos[1], pos[2])

        val srMatrix = FloatArray(16)
        Matrix.multiplyMM(srMatrix, 0, rotateM, 0, scaleM, 0)
        Matrix.multiplyMM(mModelMatrix, 0, translationM, 0, srMatrix, 0)

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

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndices.size, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)

        GLES20.glDisableVertexAttribArray(posAttrib)
        GLES20.glDisableVertexAttribArray(texCoordAttrib)

    }
}