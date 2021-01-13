//contains up to 16 Fractals (4x4 grid)
package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer


class Square(setPos: FloatArray, squareIndex: Int, locked: Boolean) : Entity(calculateSquarePosition(setPos, squareIndex), floatArrayOf(1f, 1f, 1f), 1){
    private val mIndexCount = indices1.size
    private var mVertexBuffer: FloatBuffer
    private var mIndexBuffer = createShortBuffer(indices1.copyOf())
    private var mModelMatrix = FloatArray(16)
    var mIndex = squareIndex
    var mIsOpen = false
    val mIsLocked = locked

    init {
        val vertices = vertices1.copyOf()
        //change texture if depending on lock status
        if(!mIsLocked) {
            //bottom left
            vertices[3] = 0f
            vertices[4] = .25f
            //bottom right
            vertices[8] = .25f
            vertices[9] = .25f
            //top right
            vertices[13] = .25f
            vertices[14] = 0f
            //top left
            vertices[18] = 0f
            vertices[19] = 0f
        }else {
            //bottom left
            vertices[3] = .5f
            vertices[4] = .25f
            //bottom right
            vertices[8] = .75f
            vertices[9] = .25f
            //top right
            vertices[13] = .75f
            vertices[14] = 0f
            //top left
            vertices[18] = .5f
            vertices[19] = 0f
        }

        mVertexBuffer = createFloatBuffer(vertices)
    }

    //all of size 1 - used when opening square for the first time
    fun spawnFractals(elements: Array<FractalType>): MutableList<Fractal> {
        val list = mutableListOf<Fractal>()
        for(i in elements.indices) {
            if(elements[i] != FractalType.Empty) {
                val index = intArrayOf(i % 4, i / 4)
                list.add(Fractal(arrayOf(elements[i]), 1, index, calculateInitFractalPos(index, pos)))
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

