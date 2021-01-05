package com.example.androidsquares

import android.opengl.Matrix
import android.opengl.GLES20
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

//we need to stagger rotations around axis for the bloom effect

class Cube(elements: Array<Array<FractalType>>, index: Int, open: Boolean): Entity(cubeLocations[index], floatArrayOf(.25f, .25f, .25f), 4), Transformable {
    private var mSurfaceAngle = 0f
    private var mToSurfaceAngle = 0f
    private var mFromSurfaceAngle = 0f
    private var mMargin = 0f
    private var mToMargin = 0f
    private var mFromMargin = 0f
    val MAX_MARGIN = 2.47f
    private var mVertexBuffer: Array<FloatBuffer>
    private var mIndexBuffer: Array<ShortBuffer>
    private val mModelMatrix = FloatArray(16) //cube model
    private val mSize: Int = 4
    val mIndex = index
    private val mProgram: Int = SquaresRenderer.compileShaders(vertexShaderCode, fragmentShaderCode)

    companion object {
        fun spawnSquare(elements: Array<FractalType>, surface: Surface, cubeIndex: Int): Square {
            return Square(elements, calculateSurfacePos(surface, cubeLocations[cubeIndex], 2.47f, floatArrayOf(.25f, .25f, .25f), 4), surface)
        }
    }


    init {

        if(open) {
            mSurfaceAngle = 90f
            mToSurfaceAngle = 90f
            mFromSurfaceAngle = 90f
            mMargin = MAX_MARGIN
            mToMargin = MAX_MARGIN
            mFromMargin = MAX_MARGIN
        }

        mVertexBuffer = arrayOf(calculateVertexBuffer(elements[0]),
                                calculateVertexBuffer(elements[1]),
                                calculateVertexBuffer(elements[2]),
                                calculateVertexBuffer(elements[3]),
                                calculateVertexBuffer(elements[4]),
                                calculateVertexBuffer(elements[5]))
        mIndexBuffer = arrayOf(calculateIndexBuffer(elements[0]),
                                calculateIndexBuffer(elements[1]),
                                calculateIndexBuffer(elements[2]),
                                calculateIndexBuffer(elements[3]),
                                calculateIndexBuffer(elements[4]),
                                calculateIndexBuffer(elements[5]))
    }

    private fun calculateVertexBuffer(elements: Array<FractalType>): FloatBuffer {
        val vertices = vertices4
        val trimmedVertices = FloatArray(vertices.size - findEmptyFractalCount(elements) * FLOATS_PER_QUAD )

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

        //put vertices and indices into buffer
        return ByteBuffer.allocateDirect(trimmedVertices.size * FLOAT_SIZE).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(trimmedVertices)
                position(0)
            }
        }
    }

    private fun calculateIndexBuffer(elements: Array<FractalType>): ShortBuffer {
        val indices = indices4

        val trimmedIndices = ShortArray(indices.size - 6 * findEmptyFractalCount(elements))
        indices.copyInto(trimmedIndices, 0, 0, trimmedIndices.size)

        return ByteBuffer.allocateDirect(trimmedIndices.size * SHORT_SIZE).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(trimmedIndices)
                position(0)
            }
        }
    }

   fun spawnSquares(elements: Array<Array<FractalType>>): MutableList<Square>{
        return mutableListOf(Square(elements[Surface.Front.value], calculateSurfacePos(Surface.Front, pos, MAX_MARGIN, scale, size), Surface.Front),
                            Square(elements[Surface.Back.value], calculateSurfacePos(Surface.Back, pos, MAX_MARGIN, scale, size), Surface.Back),
                            Square(elements[Surface.Left.value], calculateSurfacePos(Surface.Left, pos, MAX_MARGIN, scale, size ), Surface.Left),
                            Square(elements[Surface.Right.value], calculateSurfacePos(Surface.Right, pos, MAX_MARGIN, scale, size ), Surface.Right),
                            Square(elements[Surface.Top.value], calculateSurfacePos(Surface.Top, pos, MAX_MARGIN, scale, size ), Surface.Top),
                            Square(elements[Surface.Bottom.value], calculateSurfacePos(Surface.Bottom, pos, MAX_MARGIN, scale, size ), Surface.Bottom))
   }

    override fun onUpdate(t: Float) {
        super.onUpdate(t)
        mSurfaceAngle = mFromSurfaceAngle + (mToSurfaceAngle - mFromSurfaceAngle) * t
        mMargin = mFromMargin + (mToMargin - mFromMargin) * t
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        mSurfaceAngle = mToSurfaceAngle
        mMargin = mToMargin
    }

    fun open() {
        mToMargin = MAX_MARGIN
        mFromMargin = mMargin
        mFromSurfaceAngle = mSurfaceAngle
        mToSurfaceAngle = 90f
    }

    fun close() {
        mFromSurfaceAngle = mSurfaceAngle
        mToSurfaceAngle = 0f
        mFromMargin = mMargin
        mToMargin = 0f
    }

    //make axes of rotation set for each surface for now - can add more options later
    private fun getRotationMatrix(surface: Surface, angle: Float, margin: Float): FloatArray {
        val rMatrix = FloatArray(16)
        Matrix.setIdentityM(rMatrix, 0)
        when(surface) {
            Surface.Front -> {
                Matrix.translateM(rMatrix, 0, 0f, -.5f * margin, 0f)
            }
            Surface.Back -> { //note: top transformation needs to be applied to the back surface AFTER this one is applied
                Matrix.translateM(rMatrix, 0, 0f, 2f, -2f - margin)
                Matrix.rotateM(rMatrix, 0, angle, 1f, 0f, 0f) //assign axis of rotation such that a positive angle means opening
                Matrix.translateM(rMatrix, 0, 0f, -2f, 2f)
            }
            Surface.Left -> {
                Matrix.translateM(rMatrix, 0, -2f - margin, 2f, 0f)
                Matrix.rotateM(rMatrix, 0, angle, 0f, 0f, -1f)
                Matrix.translateM(rMatrix, 0, 2f, -2f, 0f)
            }
            Surface.Right -> {
                Matrix.translateM(rMatrix, 0, 2f + margin, -.5f* margin, 2f)
                Matrix.rotateM(rMatrix, 0, angle, 0f, -1f, 0f)
                Matrix.translateM(rMatrix, 0, -2f, 0f, -2f)
            }
            Surface.Top -> {
                Matrix.translateM(rMatrix, 0, 0f, 2f + .5f * margin, 2f)
                Matrix.rotateM(rMatrix, 0, angle, 1f, 0f, 0f)
                Matrix.translateM(rMatrix, 0, 0f, -2f, -2f)
            }
            Surface.Bottom -> {
                Matrix.translateM(rMatrix, 0, 0f, -2f - margin * 1.5f, 2f)
                Matrix.rotateM(rMatrix, 0, angle, -1f, 0f, 0f)
                Matrix.translateM(rMatrix, 0, 0f, 2f, -2f)
            }
        }

        return rMatrix
    }

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
        val texCoordAttrib = GLES20.glGetAttribLocation(mProgram, "aTexCoord")
        GLES20.glEnableVertexAttribArray(texCoordAttrib)
        val texUniformID = GLES20.glGetUniformLocation(mProgram, "uTexture")
        val modelUniformID = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")


        //rotate all the panes
        val rotatedModels = Array(6){FloatArray(16)}
        Matrix.multiplyMM(rotatedModels[Surface.Front.value], 0, getRotationMatrix(Surface.Front, mSurfaceAngle, mMargin), 0, surfaceModels[Surface.Front.value], 0)
        Matrix.multiplyMM(rotatedModels[Surface.Top.value], 0, getRotationMatrix(Surface.Top, mSurfaceAngle, mMargin), 0, surfaceModels[Surface.Top.value], 0)
        Matrix.multiplyMM(rotatedModels[Surface.Bottom.value], 0, getRotationMatrix(Surface.Bottom, mSurfaceAngle, mMargin), 0, surfaceModels[Surface.Bottom.value], 0)
        Matrix.multiplyMM(rotatedModels[Surface.Right.value], 0, getRotationMatrix(Surface.Right, mSurfaceAngle, mMargin), 0, surfaceModels[Surface.Right.value], 0)

        //back needs to be multiplied by model matrix of top rotation matrix too (after its own rotation on the surface model)
        Matrix.multiplyMM(rotatedModels[Surface.Left.value], 0, getRotationMatrix(Surface.Left,  mSurfaceAngle, mMargin), 0, surfaceModels[Surface.Left.value], 0)
        Matrix.multiplyMM(rotatedModels[Surface.Left.value], 0, getRotationMatrix(Surface.Top, mSurfaceAngle, mMargin), 0, rotatedModels[Surface.Left.value], 0)

        Matrix.multiplyMM(rotatedModels[Surface.Back.value], 0, getRotationMatrix(Surface.Back, mSurfaceAngle, mMargin), 0, surfaceModels[Surface.Back.value], 0)
        Matrix.multiplyMM(rotatedModels[Surface.Back.value], 0, getRotationMatrix(Surface.Top, mSurfaceAngle, mMargin), 0, rotatedModels[Surface.Back.value], 0)

        val finalMatrix = FloatArray(16) //this will be the matrix uniform

        for(i in 0 until 6) {
            mVertexBuffer[i].position(0)
            GLES20.glVertexAttribPointer(posAttrib, 3, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer[i])

            mVertexBuffer[i].position(3)
            GLES20.glVertexAttribPointer(texCoordAttrib, 2, GLES20.GL_FLOAT, false, 5 * FLOAT_SIZE, mVertexBuffer[i])

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, SquaresRenderer.mTextureHandle)

            GLES20.glUniform1i(texUniformID, 0)

            Matrix.multiplyMM(finalMatrix, 0, mvpMatrix, 0, rotatedModels[i], 0)

            GLES20.glUniformMatrix4fv(modelUniformID, 1, false, finalMatrix, 0)

            mIndexBuffer[i].position(0)
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexBuffer[i].capacity(), GLES20.GL_UNSIGNED_SHORT, mIndexBuffer[i])
        }

        GLES20.glDisableVertexAttribArray(posAttrib)
        GLES20.glDisableVertexAttribArray(texCoordAttrib)

    }
}