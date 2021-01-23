package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Set(pos: FloatArray, index: Int, locked: Boolean, cleared: Boolean): Entity(pos, floatArrayOf(8f, 8f, 8f), 1) {
    private var mVertexBuffer: FloatBuffer
    private var mIndexBuffer: ShortBuffer = createShortBuffer(indices1.copyOf())
    private var mIndexCount = indices1.size
    private var mModelMatrix = FloatArray(16)
    var mIndex = index
    var mIsOpen = false

    init {
        val vertices = vertices1.copyOf()

        if(cleared) {
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
        }else if(!locked) {
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


    fun spawnSquares(): MutableList<Square>{
        return mutableListOf<Square>().also {
            var finalPos: FloatArray
            var square: Square
            for(i in appData.setData[mIndex].puzzleData.indices) {
                if(appData.setData[mIndex].puzzleData[i] != null) {
                    finalPos = calculateSquarePosition(pos, i)
                    square = Square(pos, i, appData.setData[mIndex].puzzleData[i]!!.isLocked, appData.setData[mIndex].puzzleData[i]!!.isCleared)
                    square.moveTo(finalPos)
                    it.add(square)
                }
            }
        }
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

//        GLES20.glBlendColor(1f, 1f, 1f, alpha)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexCount, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer)
//        GLES20.glBlendColor(1f, 1f, 1f, 1f)
    }
}