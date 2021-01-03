package com.example.androidsquares

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap

import android.opengl.GLSurfaceView
import android.opengl.GLES20
import android.opengl.Matrix
import android.opengl.GLUtils

class SquaresRenderer(context: Context): GLSurfaceView.Renderer {

    lateinit var mCubes: Array<Cube>
    lateinit var mSquares: Array<Square>
    lateinit var mCamera: Camera
    private val mContext = context

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.167f, .212f, 1f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LESS)
        //GLES20.glEnable(GLES20.GL_CULL_FACE)
        //GLES20.glCullFace(GLES20.GL_BACK)

        mTextureHandle = loadTexture(mContext, R.drawable.fractal_colors)

        val elementsSquare: Array<FractalType> = Array(16) {FractalType.Blue}
        val elementsCube: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}

        for(i in 0 until 16) {
            for(j in 0 until 6) {
                if (i % 5 == 0) elementsCube[j][i] = FractalType.Red
                if (i % 7 == 0) elementsCube[j][i] = FractalType.Blue
                if (i % 11 == 0) elementsCube[j][i] = FractalType.Normal
                if (i % 3 == 0) elementsCube[j][i] = FractalType.Empty
            }
        }

        mCubes = arrayOf(Cube(elementsCube, floatArrayOf(0f, 1f, 3f)), Cube(elementsCube, floatArrayOf(1f, .5f, 3f)))
        mSquares = arrayOf(Square(elementsSquare, floatArrayOf(0f, -1f, 3f)), Square(elementsSquare, floatArrayOf(1f, 0f, 3f)))

        mCamera = Camera(floatArrayOf(0f, 0f, 0f))
        mCamera.moveTo(floatArrayOf(0f, 0f, -3f))
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        mCamera.onUpdate()

        Matrix.setLookAtM(mViewMatrix, 0, mCamera.pos[0], mCamera.pos[1], mCamera.pos[2], mCamera.pos[0], mCamera.pos[1], 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        for(square in mSquares) {
            square.angle[2] += .5f
            square.draw(mVPMatrix)
        }

        for(cube in mCubes) {
            cube.mParameter += .01f
            cube.angle[2] += .5f
            cube.draw(mVPMatrix)
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height

        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f) //allow depth up to 100f away from camera
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f) //allow depth up to 100f away from camera
    }

    private fun loadTexture(context: Context, resourceID: Int): Int {
        val textureHandle = IntArray(1)

        GLES20.glGenTextures(1, textureHandle, 0).also {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)

            val options: BitmapFactory.Options = BitmapFactory.Options().also {
                it.inScaled = false
            }

            val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, resourceID, options)

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            bitmap.recycle()
        }

        return textureHandle[0]
    }

    companion object {
        var mTextureHandle = -1
        val mProjectionMatrix = FloatArray(16)
        val mViewMatrix = FloatArray(16)
        val mVPMatrix = FloatArray(16)
        fun compileShaders(vertexShaderCode: String, fragmentShaderCode: String): Int {

            val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER).also {
                GLES20.glShaderSource(it, vertexShaderCode)
                GLES20.glCompileShader(it)
            }

            val fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER).also {
                GLES20.glShaderSource(it, fragmentShaderCode)
                GLES20.glCompileShader(it)
            }

            return GLES20.glCreateProgram().also {
                GLES20.glAttachShader(it, vertexShader)
                GLES20.glAttachShader(it, fragmentShader)
                GLES20.glLinkProgram(it)
            }
        }
    }
}