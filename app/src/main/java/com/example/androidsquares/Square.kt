//contains up to 16 Fractals (4x4 grid)
package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer


class Square(pos: FloatArray, squareIndex: Int, locked: Boolean, cleared: Boolean) : Entity(pos, floatArrayOf(8f, 8f, 8f), 1){
    private val mIndexCount = indices1.size
    private var mVertexBuffer: FloatBuffer
    private var mIndexBuffer = createShortBuffer(indices1.copyOf())

    private var mLockVertexBuffer: FloatBuffer //can reuse index buffer and index buffer count

    private var mModelMatrix = FloatArray(16)
    var mIndex = squareIndex
    var mIsOpen = false
    var mIsLocked = locked

    init {
        val vertices = vertices1.copyOf()
        if(cleared) { //yellow
            //bottom left
            vertices[3] = 0f
            vertices[4] = .5f
            //bottom right
            vertices[8] = .25f
            vertices[9] = .5f
            //top right
            vertices[13] = .25f
            vertices[14] = .25f
            //top left
            vertices[18] = 0f
            vertices[19] = .25f
        }

        val lockVertices = vertices.copyOf()
        lockVertices[3] = 0f
        lockVertices[4] = .75f
        lockVertices[8] = .25f
        lockVertices[9] = .75f
        lockVertices[13] = .25f
        lockVertices[14] = .5f
        lockVertices[18] = 0f
        lockVertices[19] = .5f

        mLockVertexBuffer = createFloatBuffer(lockVertices)
        mVertexBuffer = createFloatBuffer(vertices)
    }

    fun unlock() {
        mIsLocked = false
    }

    fun clear() {
        val vertices = vertices1.copyOf()
        //bottom left
        vertices[3] = 0f
        vertices[4] = .5f
        //bottom right
        vertices[8] = .25f
        vertices[9] = .5f
        //top right
        vertices[13] = .25f
        vertices[14] = .25f
        //top left
        vertices[18] = 0f
        vertices[19] = .25f
        mVertexBuffer = createFloatBuffer(vertices)
    }

    //all of size 1 - used when opening square for the first time
    fun spawnFractals(elements: Array<F>): MutableList<Fractal> {
        val list = mutableListOf<Fractal>()
        val puzzleDim = getElementsDim(elements)
        var fractal: Fractal
        for(i in elements.indices) {
            if(elements[i] != F.E) {
                val index = intArrayOf(i % MAX_PUZZLE_WIDTH, i / MAX_PUZZLE_WIDTH)
                fractal = Fractal(arrayOf(elements[i]), 1, index, pos) //temp: creating fractals right on top of square for now
                fractal.moveTo(calculateFractalPos(index, 1, pos, puzzleDim))
                list.add(fractal)
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
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexCount, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)

        if(mIsLocked) {
            mLockVertexBuffer.position(0)
            GLES20.glVertexAttribPointer(SquaresRenderer.mPosAttrib, 3, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mLockVertexBuffer)
            mLockVertexBuffer.position(3)
            GLES20.glVertexAttribPointer(SquaresRenderer.mTexCoordAttrib, 2, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mLockVertexBuffer)
            GLES20.glUniformMatrix4fv(SquaresRenderer.mModelUniform, 1, false, mvpMatrix, 0)
            mIndexBuffer.position(0)
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexCount, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)
        }
    }


}

