//contains up to 16 Fractals (4x4 grid)
package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer




class Square(elements: Array<FractalType>, setPos: FloatArray, squareIndex: Int) : Entity(calculateSquarePosition(setPos, squareIndex), floatArrayOf(.25f, .25f, .25f), 4){
    private val mIndexCount: Int //indices for drawing
    private var mVertexBuffer: FloatBuffer
    private var mIndexBuffer: ShortBuffer
    private var mModelMatrix = FloatArray(16)
    private val mSize: Int = 4
    var mIndex = squareIndex
    var mIsOpen = false

    init {
        val vertices = squareVertices
        val indices = squareIndices
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
        mVertexBuffer = createFloatBuffer(trimmedVertices)
        mIndexBuffer = createShortBuffer(trimmedIndices)
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

