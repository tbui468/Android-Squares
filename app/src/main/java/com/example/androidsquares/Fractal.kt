//Fractals are the smallest object user can interact with (along with larger Squares and Cubes)
//all drawing from Cubes/Squares trickles down to the Fractal, which draws the basic quad

package com.example.androidsquares

import java.nio.FloatBuffer
import java.nio.ShortBuffer

import android.opengl.Matrix
import android.opengl.GLES20

val FRACTAL_SIZE = 5.5f

class Fractal(elements: Array<F>, size: Int, fractalIndex: IntArray, pos: FloatArray): Entity(pos, floatArrayOf(FRACTAL_SIZE, FRACTAL_SIZE, FRACTAL_SIZE), size) {

    private val mIndexCount: Int //for rendering
    private var mVertexBuffer: FloatBuffer
    private var mIndexBuffer: ShortBuffer
    private var mModelMatrix = FloatArray(16)
    var mClearedBox: Box? = null
    var mShowClearedBox: Boolean = false
    val mSize: Int = size
    var mIndex = fractalIndex //fractal index - unrelated to rendering
    var mIsBlock = false

    init {
        val vertices: FloatArray
        val indices: ShortArray

        when(elements.size) {
            1 -> {
                vertices = vertices1.copyOf()
                indices = indices1.copyOf()
            }
            4 -> {
                vertices = vertices2.copyOf()
                indices = indices2.copyOf()
            }
            16 -> {
                vertices = vertices4.copyOf()
                indices = indices4.copyOf()
            }
            else -> {
                //temp: this should be an assert false
                vertices = vertices1
                indices = indices1
            }
        }

        val emptyFractalCount = findEmptyFractalCount(elements)
        val floatsToTrim = emptyFractalCount * FLOATS_PER_QUAD //four vertices per fractal, and 5 floats per vertex
        val trimmedVertices = FloatArray(vertices.size - floatsToTrim)

        var trimmedVerticesOffset = 0
        //problem is I'm assuming everything is a 2d square - mSize doesn't work for cubes!
        //instead, iterate through elements and find the row and column from that???  Still causes pro
        var col: Int
        var row: Int
        elements.forEachIndexed { index, element ->
            run {
                col = index % mSize
                row = index / mSize
                when(element) {
                    F.RB -> mIsBlock = true
                    F.GB -> mIsBlock = true
                    F.BB -> mIsBlock = true
                    F.NB -> mIsBlock = true
                }
                if (element != F.E) {
                    setTexture(vertices, mSize, col, row, element)
                    vertices.copyInto(trimmedVertices, trimmedVerticesOffset, (row * mSize + col) * FLOATS_PER_QUAD, (row * mSize + col + 1) * FLOATS_PER_QUAD)
                    trimmedVerticesOffset += FLOATS_PER_QUAD
                }
            }
        }


        if(mIsBlock) {
            mClearedBox = Box(pos, false)
            mShowClearedBox = true
        }


        val trimmedIndices = ShortArray(indices.size - 6 * emptyFractalCount)
        indices.copyInto(trimmedIndices, 0, 0, trimmedIndices.size)
        mIndexCount = trimmedIndices.size

        //put vertices and indices into buffer
        mVertexBuffer = createFloatBuffer(trimmedVertices)
        mIndexBuffer = createShortBuffer(trimmedIndices)
    }

    override fun onUpdate(t: Float) {
        super.onUpdate(t)
        if(mClearedBox != null)
            mClearedBox!!.onUpdate(t)
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        if(mClearedBox != null)
            mClearedBox!!.onAnimationEnd()
    }

    override fun moveTo(newPos: FloatArray) {
        super.moveTo(newPos)
        if(mClearedBox != null)
            mClearedBox!!.moveTo(newPos)
    }


    fun draw(vpMatrix: FloatArray) {
        mModelMatrix = calculateModelMatrix()

        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mModelMatrix, 0)

        //////////set data and draw////////////////////////////////////////////////

        mVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(SquaresRenderer.mPosAttrib, 3, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer)

        mVertexBuffer.position(3)
        GLES20.glVertexAttribPointer(SquaresRenderer.mTexCoordAttrib, 2, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer)

        GLES20.glUniformMatrix4fv(SquaresRenderer.mModelUniform, 1, false, mvpMatrix, 0)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexCount, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)

        if(mClearedBox != null && mShowClearedBox) {
            mClearedBox!!.draw(vpMatrix)
        }

    }
}