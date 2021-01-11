//contains up to 16 Fractals (4x4 grid)
package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Square(elements: Array<FractalType>, pos: FloatArray, surface: Surface) : Entity(pos, floatArrayOf(.25f, .25f, .25f), 4), Transformable {
    private val mIndexCount: Int //indices for drawing
    private var mVertexBuffer: FloatBuffer
    private var mIndexBuffer: ShortBuffer
    private val mModelMatrix = FloatArray(16)
    private val mSize: Int = 4
    private val mProgram: Int = SquaresRenderer.compileShaders(vertexShaderCode, fragmentShaderCode)
    var mSurface = surface //surface among the six cube faces - related to, but not the same, as index
    lateinit var mIndex: IntArray


    init {
        mIndex = when(mSurface) {
            Surface.Left -> intArrayOf(0, 1)
            Surface.Right -> intArrayOf(2, 2)
            Surface.Front -> intArrayOf(1, 2)
            Surface.Back -> intArrayOf(1, 0)
            Surface.Top -> intArrayOf(1, 1)
            Surface.Bottom -> intArrayOf(1, 3)
            else -> intArrayOf(-1, -1)
        }


        val vertices = vertices4
        val indices = indices4
        val emptyFractalCount = findEmptyFractalCount(elements)
        val floatsToTrim = emptyFractalCount * FLOATS_PER_QUAD //four vertices per fractal, and 5 floats per vertex
        val trimmedVertices = FloatArray(vertices.size - floatsToTrim)

        var trimmedVerticesOffset = 0
        var col: Int
        var row: Int
        elements.forEachIndexed { index, element ->
            run {
                col = index % mSize
                row = index / mSize
                if (element != FractalType.Empty) {
                    setTexture(vertices, mSize, col, row, element)
                    vertices.copyInto(trimmedVertices, trimmedVerticesOffset, (row * mSize + col) * FLOATS_PER_QUAD, (row * mSize + col + 1) * FLOATS_PER_QUAD)
                    trimmedVerticesOffset += FLOATS_PER_QUAD
                }
            }
        }

        val trimmedIndices = ShortArray(indices.size - 6 * emptyFractalCount)
        indices.copyInto(trimmedIndices, 0, 0, trimmedIndices.size)
        mIndexCount = trimmedIndices.size

        //put vertices and indices into buffer
        mVertexBuffer = ByteBuffer.allocateDirect(trimmedVertices.size * FLOAT_SIZE).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(trimmedVertices)
                position(0)
            }
        }

        mIndexBuffer = ByteBuffer.allocateDirect(trimmedIndices.size * SHORT_SIZE).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(trimmedIndices)
                position(0)
            }
        }
    }

    //all of size 1 - used when opening square for the first time
    fun spawnFractals(elements: Array<FractalType>): MutableList<Fractal> {
        val list = mutableListOf<Fractal>()
        for(i in elements.indices) {
            if(elements[i] != FractalType.Empty) {
                val index = intArrayOf(i % 4, i / 4)
                list.add(Fractal(arrayOf(elements[i]), 1, index, calculateFractalPosForTarget(index, 1, intArrayOf(0, 0), 4, pos)))
            }
        }
        return list
    }

    override fun draw(vpMatrix: FloatArray) {
        val scaleM = FloatArray(16)
        Matrix.setIdentityM(scaleM, 0)
        Matrix.scaleM(scaleM, 0, scale[0], scale[1], scale[2])

        val rotateM = FloatArray(16)
        Matrix.setIdentityM(rotateM, 0)
        Matrix.rotateM(rotateM, 0, angle, rotationAxis[0], rotationAxis[1], rotationAxis[2])

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

        GLES20.glBlendColor(1f, 1f, 1f, alpha)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexCount, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)
        GLES20.glBlendColor(1f, 1f, 1f, 1f)

        GLES20.glDisableVertexAttribArray(posAttrib)
        GLES20.glDisableVertexAttribArray(texCoordAttrib)

    }


}