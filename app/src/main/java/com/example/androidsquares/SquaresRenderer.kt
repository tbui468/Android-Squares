package com.example.androidsquares

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLSurfaceView
import android.opengl.GLES20
import android.opengl.Matrix

class SquaresRenderer: GLSurfaceView.Renderer {

    lateinit var fractal0: Fractal
    lateinit var fractal1: Fractal
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.5f, 0.5f, .7f, 1f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        fractal0 = Fractal(floatArrayOf(0f, -400f, 0f), floatArrayOf(500f, 500f, 1f))
        fractal1 = Fractal(floatArrayOf(0f, 400f, 4f), floatArrayOf(500f, 500f, 1f))
    }
    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        val vpMatrix = FloatArray(16)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(vpMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        fractal0.draw(vpMatrix)
        fractal1.draw(vpMatrix)
    }
    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        Matrix.frustumM(mProjectionMatrix, 0, -width/2f, width/2f, -height/2f, height/2f, 3f, 7f)
    }

    companion object {
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