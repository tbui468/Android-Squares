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
    var mAnimationParameter = 0f //main animation timer
    lateinit var mCubes: MutableList<Cube>
    lateinit var mSquares: MutableList<Square>
    lateinit var mCamera: Camera
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mContext = context

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.167f, .212f, 1f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LESS)
//        GLES20.glEnable(GLES20.GL_CULL_FACE)
 //       GLES20.glCullFace(GLES20.GL_BACK)

        mTextureHandle = loadTexture(mContext, R.drawable.fractal_colors)

        val elementsSquare: Array<FractalType> = Array(16) {FractalType.Blue}
        val elementsCube: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}

        elementsCube[Surface.Front.value] = Array(16){FractalType.Normal}
        elementsCube[Surface.Back.value] = Array(16){FractalType.Red}

        for(i in 0 until 16) {
            for(j in 2 until 6) {
                if (i % 7 == 0) elementsCube[j][i] = FractalType.Blue
                if (i % 11 == 0) elementsCube[j][i] = FractalType.Green
                if (i % 5 == 0) elementsCube[j][i] = FractalType.Empty
                if (i == 0) elementsCube[j][i] = FractalType.Red //mark top left red as reference
            }
        }

        mCubes = mutableListOf(Cube(elementsCube, floatArrayOf(0f, 1.5f, 0f)), Cube(elementsCube, floatArrayOf(1f, .5f, 0f)),
                                Cube(elementsCube, floatArrayOf(0f, -1.5f, 0f)), Cube(elementsCube, floatArrayOf(-1f, 0f, 0f)),
                                Cube(elementsCube, floatArrayOf(0f, 0f, 0f)))
        mSquares = mutableListOf(Square(elementsSquare, floatArrayOf(0f, -1f, 0f)), Square(elementsSquare, floatArrayOf(1f, 0f, 0f)))

        mCamera = Camera(floatArrayOf(0f, 0f, 3f))
        mCamera.moveTo(floatArrayOf(0f, 0f, 6f))
    }

    fun openSquarePuzzle(square: Square) {
        //destroy given square and replace with fractals at that location
        //animate fractals moving to final location
        mCamera.moveTo(floatArrayOf(square.pos[0], square.pos[1], 3.5f))
        mAnimationParameter = 0f
    }

    fun closeSquarePuzzle() {
        //merge all fractals
        //destroy fractals on animation end and create square in its place
        val cube = getOpenCube()
        if(cube != null) openCubePuzzle(cube)
        else closeCubePuzzle()
        mAnimationParameter = 0f
    }

    fun getOpenCube(): Cube? {
        for(cube in mCubes) {
            if(cube.isOpen()) return cube
        }
        return null
    }

    fun openCubePuzzle(cube: Cube) {
        mAnimationParameter = 0f
        for(c in mCubes) c.close()
        cube.open()
        mCamera.moveTo(floatArrayOf(cube.pos[0], cube.pos[1] + cube.scale[1] * cube.objectSize[1] / 2f, 3.3f)) //center camera on unfolded front/top surface of cubes
    }

    //end of cube opening animation - this needs to be queued
    private fun onCubePuzzleOpened(cube: Cube) {
        /*
        mSquares.add(cube.spawnSquare(Surface.Top))
        mSquares.add(cube.spawnSquare(Surface.Bottom))
        mSquares.add(cube.spawnSquare(Surface.Left))
        mSquares.add(cube.spawnSquare(Surface.Right))
        mSquares.add(cube.spawnSquare(Surface.Front))
        mSquares.add(cube.spawnSquare(Surface.Back))*/
        mCubes.remove(cube)
        mAnimationParameter = 0f
    }

    fun closeCubePuzzle() {
        for(c in mCubes) {
            c.close()
        }
        mCamera.moveTo(floatArrayOf(0f, 0f, 6f))
        mAnimationParameter = 0f
    }

    private fun onAnimationEnd() {
        for(square in mSquares) {
            square.onAnimationEnd()
        }

        for(cube in mCubes) {
            if(cube.isOpen()) {
                //spawn 6 squares at correct locations
                //destroy self from mCubes lists
            }else {
                cube.onAnimationEnd()
            }
        }
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        if(mAnimationParameter < 1f) {
            mAnimationParameter += 0.025f //todo: scale by deltatime
            if(mAnimationParameter >= 1f) {
                onAnimationEnd()
            }
        }

        mCamera.onUpdate(mAnimationParameter)

        Matrix.setLookAtM(mViewMatrix, 0, mCamera.pos[0], mCamera.pos[1], mCamera.pos[2], mCamera.pos[0], mCamera.pos[1], 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

/*
        for(square in mSquares) {
            square.angle[2] += .5f
            square.draw(mVPMatrix)
        }*/

        for(cube in mCubes) {
            cube.onUpdate(mAnimationParameter)
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