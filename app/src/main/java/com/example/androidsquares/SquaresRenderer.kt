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

    //lateinit var fractal1: Fractal
    //lateinit var fractal2: Fractal
    lateinit var fractal4: Fractal
    lateinit var cube0: Fractal
    lateinit var cube1: Fractal
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

        val elements: Array<FractalType> = arrayOf(FractalType.Empty, FractalType.Red, FractalType.Green, FractalType.Blue)
        //val elementsSquare: Array<FractalType> = Array(16) {FractalType.Blue}
        val elementsCube: Array<FractalType> = Array(96) {FractalType.Green}
        for(i in 0 until 96) {
            if(i % 5 == 0) elementsCube[i] = FractalType.Red
            if(i % 7 == 0) elementsCube[i] = FractalType.Blue
            if(i % 11 == 0) elementsCube[i] = FractalType.Normal
            if(i % 3 == 0) elementsCube[i] = FractalType.Empty
        }

        //fractal1 = Fractal(elements, 1)
        //fractal2 = Fractal(elementsSquare, 2) //setting it to 5 to make cube
        fractal4 = Fractal(elementsCube, 4)
        cube0 = Fractal(elementsCube, 4)
        cube1 = Fractal(elementsCube, 4)

        //fractal1.pos = floatArrayOf(0f, 1f, .1f)
        //fractal2.pos = floatArrayOf(0f, 0f, 0f)
        fractal4.pos = floatArrayOf(0f, 0f, .1f)
        cube0.pos = floatArrayOf(0f, 1f, 2f)
        cube1.pos = floatArrayOf(1f, .5f, 3f)

        fractal4.scale = floatArrayOf(.1f, .1f, .1f)
        cube0.scale = floatArrayOf(.1f, .1f, .1f)
        cube1.scale = floatArrayOf(.1f, .1f, .1f)
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        //fractal1.angle[2] += .5f
        //fractal2.angle[2] += .5f
        fractal4.angle[2] += .5f
        if(cube0.rotating) cube0.angle[2] += .5f
        if(cube1.rotating) cube1.angle[2] += .5f

        //fractal1.draw(vpMatrix)
        //fractal2.draw(vpMatrix)
        //fractal4.draw(vpMatrix)
        cube0.draw(mVPMatrix)
        cube1.draw(mVPMatrix)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        var ratio = width.toFloat() / height

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