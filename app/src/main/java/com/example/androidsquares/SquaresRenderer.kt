package com.example.androidsquares

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.util.Log

import android.os.SystemClock
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap

import android.opengl.GLSurfaceView
import android.opengl.GLES20
import android.opengl.Matrix
import android.opengl.GLUtils


class SquaresRenderer(context: Context): GLSurfaceView.Renderer {
    var mAnimationParameter = 0f //main animation timer
    var mCubes = mutableListOf<Cube>()
    var mSquares = mutableListOf<Square>()
    var mFractals = mutableListOf<Fractal>()
    lateinit var mCamera: Camera
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private var mCurrentTime: Long = 0
    private var mPreviousTime: Long = 0
    private val mContext = context
    var mInputQueue = InputQueue()
    private var mOpeningCube: Cube? = null
    private var mClosingSquare = Surface.None

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.167f, .212f, 1f)
        GLES20.glEnable(GLES20.GL_BLEND)
//        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA) //SRC was from the texture (in my case)
        GLES20.glBlendFunc(GLES20.GL_CONSTANT_ALPHA, GLES20.GL_ONE_MINUS_CONSTANT_ALPHA)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LESS)
//        GLES20.glEnable(GLES20.GL_CULL_FACE)
 //       GLES20.glCullFace(GLES20.GL_BACK)

        mTextureHandle = loadTexture(mContext, R.drawable.fractal_colors)


        for(i in cubeLocations.indices) {
            mCubes.add(Cube(puzzleData[i], i, false))
        }

        mCamera = Camera(floatArrayOf(0f, 0f, 3f))
        mCamera.moveTo(floatArrayOf(0f, 0f, 16f))
    }


    fun openCube(cube: Cube) {
        for(c in mCubes) {
            if(c != cube) {
                c.fadeTo(0f)
            }
        }
        cube.open()
        mOpeningCube = cube
        mCamera.moveTo(floatArrayOf(cube.pos[0], cube.pos[1] + cube.scale[1] * cube.size / 2f, 12f)) //center camera on unfolded front/top surface of cubes
    }

    private fun closeCube(cubeIndex: Int) {
        mSquares.clear()

        for(c in mCubes) {
            c.fadeTo(1f)
        }

        Cube(puzzleData[cubeIndex], cubeIndex, true).also {
            it.close()
            mCubes.add(it)
        }

        mCamera.moveTo(floatArrayOf(0f, 0f, 16f))
    }

    private fun openSquare(square: Square) {
        mCamera.moveTo(floatArrayOf(square.pos[0], square.pos[1], 5f))
        mFractals = square.spawnFractals(puzzleData[getOpenCubeIndex()][square.mSurface.value])
        for(fractal in mFractals) {
            fractal.moveTo(calculateFractalPos(fractal.mIndex, fractal.mSize, fractal.mIndex, 1, square.pos))
        }
        mSquares.remove(square)

        for(s in mSquares) {
            s.fadeTo(0f)
        }
    }

    private fun closeSquare(surface: Surface) {
        mClosingSquare = surface
        val cubePos = cubeLocations[getOpenCubeIndex()]
        for (fractal in mFractals) {
            //temp: need square location - not camera and then adding half of cube width (bc it will get messy if I decide to change anything)
            fractal.moveTo(calculateFractalPos(fractal.mIndex, fractal.mSize, fractal.mIndex, 4, floatArrayOf(mCamera.pos[0], mCamera.pos[1], .5f)))
        }
        mCamera.moveTo(floatArrayOf(cubePos[0], cubePos[1] + .25f * 4f / 2f, 12f))

        for(s in mSquares) {
            s.fadeTo(1f)
        }
    }

    private fun getScreenState(): Screen {
        if(mFractals.size > 0) return Screen.Fractal
        if(mSquares.size > 0) return Screen.Square
        return Screen.Cube
    }

    //open cubes are destroyed when animation is over, so just need to iterate over mCubes list and find out which index (0-7) is missing
    //returns -1 if no cubes open
    private fun getOpenCubeIndex(): Int {
        val found = BooleanArray(8){false}
        for(cube in mCubes) {
            found[cube.mIndex] = true
        }

        found.forEachIndexed { index, f ->
            if(!f) return index
        }

        return -1
    }


    //open square is destroyed when animation is over, so just need to iterate over mSquares and find index that is missing
    private fun getOpenSquare(): Surface {
        val found = BooleanArray(6){false}
        for(square in mSquares) {
            found[square.mSurface.value] = true
        }

        found.forEachIndexed { index, f ->
            if(!f) {
                return when(index) {
                    0 -> Surface.Front
                    1 -> Surface.Back
                    2 -> Surface.Left
                    3 -> Surface.Right
                    4 -> Surface.Top
                    5 -> Surface.Bottom
                    else -> Surface.None
                }
            }
        }

        return Surface.None
    }

    //////////////helper functions for commands//////////////////////////////////////////
    private fun getFractal(index: IntArray): Fractal? {
        for(fractal in mFractals) {
            if(fractal.mIndex[0] == index[0] && fractal.mIndex[1] == index[1]) return fractal
        }

        return null
    }

    private fun swap(fractal0: Fractal, fractal1: Fractal) {
        //update data
        val index0 = fractal0.mIndex[0] + 4 * fractal0.mIndex[1]
        val index1 = fractal1.mIndex[0] + 4 * fractal1.mIndex[1]
        puzzleData[getOpenCubeIndex()][getOpenSquare().value][index0] = puzzleData[getOpenCubeIndex()][getOpenSquare().value][index1].also {
            puzzleData[getOpenCubeIndex()][getOpenSquare().value][index1] = puzzleData[getOpenCubeIndex()][getOpenSquare().value][index0]
        }

        //animate
        fractal0.moveTo(fractal1.pos)
        fractal1.moveTo(fractal0.pos)
        fractal0.mIndex = fractal1.mIndex.also {fractal1.mIndex = fractal0.mIndex}
    }

    private fun dispatchCommand(touchType: TouchType, x: Float, y: Float): Boolean {
        when(getScreenState()) {
            Screen.Cube -> {
                if(touchType == TouchType.Tap) {
                    for (cube in mCubes) {
                        if (cube.pointCollision(x, y)) {
                            openCube(cube)
                            return true
                        }
                    }
                }
            }
            Screen.Square -> {
                if(touchType == TouchType.Tap) {
                    for (square in mSquares) {
                        if (square.pointCollision(x, y)) {
                            openSquare(square)
                            return true
                        }
                    }

                    //need to specify type of input I want register as going a screen back (such as pinch out)
                    closeCube(getOpenCubeIndex())
                    return true
                }
            }
            Screen.Fractal -> {
                for (fractal in mFractals) {
                    if (fractal.pointCollision(x, y)) {
                        when(touchType) {
                            TouchType.FlickLeft -> {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] - fractal.mSize, fractal.mIndex[1]))
                                if(swappedFractal != null) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                            TouchType.FlickRight -> {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] + fractal.mSize, fractal.mIndex[1]))
                                if(swappedFractal != null) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                            TouchType.FlickUp -> {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] - fractal.mSize))
                                if(swappedFractal != null) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                            TouchType.FlickDown -> {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] + fractal.mSize))
                                if(swappedFractal != null) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                        }
                    }
                }

                if(touchType == TouchType.Tap) {
                    closeSquare(getOpenSquare())
                    return true
                }
            }
        }

        return false
    }


    private fun onAnimationEnd() {
        for(cube in mCubes) {
            cube.onAnimationEnd()
        }
        for(square in mSquares) {
            square.onAnimationEnd()
        }
        for(fractal in mFractals) {
            fractal.onAnimationEnd()
        }
        mCamera.onAnimationEnd()


        //this stuff shouldn't be here - fire and forget commands will simplify everything
        //so destroy cube on tap, creating squares and then animating them
        //on closing a cube, destroy all squares and create an open cube and animate it closing
        ///////////////////////////////////////////////////////////////////////////////////////
        if(mOpeningCube != null) {
            mSquares = mOpeningCube!!.spawnSquares(puzzleData[mOpeningCube!!.mIndex])
            mCubes.remove(mOpeningCube)
            mOpeningCube = null
        }


        if(mClosingSquare != Surface.None  && getOpenCubeIndex() != -1) {
            mFractals.clear()
            mSquares.add(Cube.spawnSquare(puzzleData[getOpenCubeIndex()][mClosingSquare.value], mClosingSquare, getOpenCubeIndex()))
            mClosingSquare = Surface.None
        }
        //////////////////////////////////////////////////////////////////////////////////////////
    }

    override fun onDrawFrame(unused: GL10) {

        ////////////////////////////setup///////////////////////////////////////////
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        //delta time
        mPreviousTime = mCurrentTime
        mCurrentTime = SystemClock.uptimeMillis()
        val deltaTime = 0.001f * (mCurrentTime - mPreviousTime).toInt()


        //only want to call onAnimationEnd() once per animation
        if(mAnimationParameter < 1f) {
            mAnimationParameter += deltaTime
            if(mAnimationParameter >= 1f) {
                onAnimationEnd()
            }
        }

        if(mAnimationParameter >= 1f) {
            var touchRegistered = false
            while (!mInputQueue.isEmpty() && !touchRegistered) {
                val data = mInputQueue.getNextInput()
                touchRegistered = dispatchCommand(data.touchType, data.x, data.y)
            }
            if (touchRegistered) mAnimationParameter = 0f
        }


        val sigmoid = sigmoid(mAnimationParameter)

        ////////////////////////////////////////////update////////////////////////////////
        mInputQueue.onUpdate(deltaTime)

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