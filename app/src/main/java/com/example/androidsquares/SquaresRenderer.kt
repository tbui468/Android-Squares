package com.example.androidsquares

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.os.SystemClock
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap

import android.opengl.GLSurfaceView
import android.opengl.GLES20
import android.opengl.Matrix
import android.opengl.GLUtils

import kotlin.math.exp

//temp: move to Utility.kt
fun sigmoid(t: Float): Float {
    return 1f / (1f + exp(-15f * (t - .5f)))
}

class SquaresRenderer(context: Context): GLSurfaceView.Renderer {
    var mAnimationParameter = 1f //main animation timer
    var mCubes = mutableListOf<Cube>()
    var mSquares = mutableListOf<Square>()
    var mFractals = mutableListOf<Fractal>()
    lateinit var mCamera: Camera
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private var mCurrentTime: Long = 0
    private var mPreviousTime: Long = 0
    private val mContext = context
    var mCloseCubeFlag = false //temp: should create command queue that surfaceview can add commands to
    var mOpenSquareFlag = false
    var mCloseSquareFlag = false
    var mStupidFlag = false
    var mOpenCubeIndex: Int = -1
    var mOpenSquareIndex: Int = -1

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.167f, .212f, 1f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LESS)
//        GLES20.glEnable(GLES20.GL_CULL_FACE)
 //       GLES20.glCullFace(GLES20.GL_BACK)

        mTextureHandle = loadTexture(mContext, R.drawable.fractal_colors)


        for(i in cubeLocations.indices) {
            mCubes.add(Cube(cubeData0, i, false))
        }

        mCamera = Camera(floatArrayOf(0f, 0f, 3f))
        mCamera.moveTo(floatArrayOf(0f, 0f, 24f))
    }


    fun openCube(cube: Cube) {
        mAnimationParameter = 0f
        for(c in mCubes) c.close()
        cube.open()
        mOpenCubeIndex = cube.mIndex
        mCamera.moveTo(floatArrayOf(cube.pos[0], cube.pos[1] + cube.scale[1] / 2f, 12f)) //center camera on unfolded front/top surface of cubes
    }

    private fun closeCube(cubeIndex: Int) {
        mAnimationParameter = 0f
        for(c in mCubes) {
            c.close()
        }
        mSquares.clear()
        mFractals.clear()

        val newCube = Cube(cubeData0, cubeIndex, true)
        newCube.close()
        mCubes.add(newCube)

        mCamera.moveTo(floatArrayOf(0f, 0f, 24f))
    }

    private fun openSquare(squareIndex: Int) {
        mAnimationParameter = 0f
        var openSquare: Square? = null
        for(square in mSquares) {
            if(square.mIndex == squareIndex) openSquare = square
        }
        if(openSquare != null) {
            mCamera.moveTo(floatArrayOf(openSquare.pos[0], openSquare.pos[1], 5f))
            mFractals = openSquare.spawnFractals(cubeData0[squareIndex])
            for(fractal in mFractals) {
                fractal.moveTo(calculateFractalPos(fractal.mIndex, fractal.mSize, fractal.mIndex, 1, openSquare.pos))
            }
            mSquares.remove(openSquare)
        }

    }

    private fun closeSquare(squareIndex: Int) {
        mAnimationParameter = 0f
        val cubePos = cubeLocations[mOpenCubeIndex]
        mCamera.moveTo(floatArrayOf(cubePos[0], cubePos[1], 12f))
        for (fractal in mFractals) {
            //temp: need square location - not camera and then adding half of cube width (bc it will get messy if I decide to change anything)
            fractal.moveTo(calculateFractalPos(fractal.mIndex, fractal.mSize, fractal.mIndex, 4, floatArrayOf(mCamera.pos[0], mCamera.pos[1], .5f)))
        }
    }


    private fun onAnimationEnd() {
        for(square in mSquares) {
            square.onAnimationEnd()
        }

        var openCube: Cube? = null
        for(cube in mCubes) {
            if(cube.mIndex == mOpenCubeIndex) {
                mSquares = cube.spawnSquares(cubeData0) //temp: should look up cube index and find proper data with given index
                openCube = cube
            }else {
                cube.onAnimationEnd()
            }
        }


        if(mStupidFlag) {
            mSquares.add(Cube.spawnSquare(cubeData0[mOpenSquareIndex], mOpenSquareIndex, mOpenCubeIndex))
            mOpenSquareIndex = -1
            mStupidFlag = false
        }

        if(openCube != null)
            mCubes.remove(openCube)
    }

    override fun onDrawFrame(unused: GL10) {

        //temp: should use command queue for surfaceview to queue commands instead of boolean flags for all this stuff
        if(mAnimationParameter >= 1f) {
            if (mCloseCubeFlag) {
                closeCube(mOpenCubeIndex)
                mCloseCubeFlag = false
                mOpenCubeIndex = -1
            }

            if(mOpenSquareFlag) {
                openSquare(mOpenSquareIndex)
                mOpenSquareFlag = false
            }

            if(mCloseSquareFlag) {
                closeSquare(mOpenSquareIndex)
                mCloseSquareFlag = false
//                mOpenSquareIndex = -1
                mStupidFlag = true
            }
        }

        ////////////////////////////setup///////////////////////////////////////////
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        //delta time
        mPreviousTime = mCurrentTime
        mCurrentTime = SystemClock.uptimeMillis()

        if(mAnimationParameter < 1f) {
            mAnimationParameter += 0.001f * (mCurrentTime - mPreviousTime).toInt() //delta time
            if(mAnimationParameter >= 1f) {
                onAnimationEnd()
            }
        }



        val sigmoid = sigmoid(mAnimationParameter)

        ////////////////////////////////////////////update////////////////////////////////

        mCamera.onUpdate(sigmoid)

        for(fractal in mFractals) {
            fractal.onUpdate(sigmoid)
        }

        for(square in mSquares) {
            square.onUpdate(sigmoid)
        }

        for(cube in mCubes) {
            cube.onUpdate(sigmoid)
        }

        Matrix.setLookAtM(mViewMatrix, 0, mCamera.pos[0], mCamera.pos[1], mCamera.pos[2], mCamera.pos[0], mCamera.pos[1], 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        /////////////////////////////////////draw/////////////////////////////////////
        for(fractal in mFractals) {
            fractal.draw(mVPMatrix)
        }

        for(square in mSquares) {
            square.draw(mVPMatrix)
        }

        for(cube in mCubes) {
            cube.draw(mVPMatrix)
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height

        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f) //allow depth up to 100f away from camera
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 33f) //allow depth up to 30 units away
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