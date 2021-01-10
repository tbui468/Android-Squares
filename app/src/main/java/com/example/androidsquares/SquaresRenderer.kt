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
    private var mMergeFractals: Array<Fractal>? = null
    private var mRecreateFractal: Fractal? = null

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
        mCamera.moveTo(floatArrayOf(cube.pos[0], cube.pos[1] + cube.scale[1] * cube.size / 2f, 10f)) //center camera on unfolded front/top surface of cubes
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
        mCamera.moveTo(floatArrayOf(square.pos[0], square.pos[1], 4f))
        mFractals = square.spawnFractals(puzzleData[getOpenCubeIndex()][square.mSurface.value])
        for(fractal in mFractals) {
            fractal.moveTo(calculateFractalPosForTarget(fractal.mIndex, fractal.mSize, fractal.mIndex, 1, square.pos))
        }
        mSquares.remove(square)

        for(s in mSquares) {
            s.fadeTo(0f)
        }
    }

    private fun closeSquare(surface: Surface) {
        mClosingSquare = surface
        val cubePos = cubeLocations[getOpenCubeIndex()]
        val squarePos = calculateSurfacePos(getOpenSquareSurface(), cubeLocations[getOpenCubeIndex()])
        for (fractal in mFractals) {
            fractal.moveTo(calculateFractalPosForTarget(fractal.mIndex, fractal.mSize, intArrayOf(0, 0), 4, squarePos))
        }
        mCamera.moveTo(floatArrayOf(cubePos[0], cubePos[1] + .25f * 4f / 2f, 10f))

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
    //this should really be getOpenSquareSurface
    private fun getOpenSquareSurface(): Surface {
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
        if(index[0] < 0 || index[0] >=4 || index[1] < 0 || index[1] >= 4) return null

        for(fractal in mFractals) {
            if(fractal.mIndex[0] == index[0] && fractal.mIndex[1] == index[1]) return fractal
        }

        return null
    }

    private fun getElements(cubeIndex: Int, squareIndex: Int, index: IntArray, size: Int): Array<FractalType> {
        val elements = Array(size * size){FractalType.Normal}
        for(row in 0 until size) {
            for(col in 0 until size) {
                elements[col + row * size] = puzzleData[cubeIndex][squareIndex][index[0] + col + (index[1] + row) * 4]
            }
        }
        return elements
    }

    private fun setElements(cubeIndex: Int, squareIndex: Int, index: IntArray, elements: Array<FractalType>) {
        val size = when(elements.size) {
            1 -> 1
            4 -> 2
            16 -> 4
            else -> 1 //should be assert
        }

        for(row in 0 until size) {
            for(col in 0 until size) {
                puzzleData[cubeIndex][squareIndex][index[0] + col + (index[1] + row) * 4] = elements[col + row * size]
            }
        }
    }


    private fun swap(fractal0: Fractal, fractal1: Fractal) {
        //update data
        val elements0 = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal0.mIndex, fractal0.mSize)
        val elements1 = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal1.mIndex, fractal1.mSize)
        setElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal0.mIndex, elements1)
        setElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal1.mIndex, elements0)

        //animate
        fractal0.moveTo(fractal1.pos)
        fractal1.moveTo(fractal0.pos)
        fractal0.mIndex = fractal1.mIndex.also {fractal1.mIndex = fractal0.mIndex}
    }

    private fun rotateCW(fractal: Fractal) {
        val elements = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, fractal.mSize)

        //rotate elements clockwise
        when(fractal.mSize) {
            2-> {
                elements[0] = elements[2].also {
                    elements[2] = elements[3].also {
                        elements[3] = elements[1].also {
                            elements[1] = elements[0]
                        }
                    }
                }
            }
            4-> {
                val elemCopy = elements.copyOf()

                elements[0] = elemCopy[12]
                elements[1] = elemCopy[8]
                elements[2] = elemCopy[4]
                elements[3] = elemCopy[0]

                elements[4] = elemCopy[13]
                elements[5] = elemCopy[9]
                elements[6] = elemCopy[5]
                elements[7] = elemCopy[1]

                elements[8] = elemCopy[14]
                elements[9] = elemCopy[10]
                elements[10] = elemCopy[6]
                elements[11] = elemCopy[2]

                elements[12] = elemCopy[15]
                elements[13] = elemCopy[11]
                elements[14] = elemCopy[7]
                elements[15] = elemCopy[3]
            }
        }

        setElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, elements)
        fractal.rotateTo(-90f, floatArrayOf(0f, 0f, 1f))
        mRecreateFractal = fractal
    }

    private fun rotateCCW(fractal: Fractal) {
        val elements = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, fractal.mSize)

        //rotate elements ccw
        when(fractal.mSize) {
            2-> {
                elements[0] = elements[1].also {
                    elements[1] = elements[3].also {
                        elements[3] = elements[2].also {
                            elements[2] = elements[0]
                        }
                    }
                }
            }
            4-> {
                val elemCopy = elements.copyOf()

                elements[0] = elemCopy[3]
                elements[1] = elemCopy[7]
                elements[2] = elemCopy[11]
                elements[3] = elemCopy[15]

                elements[4] = elemCopy[2]
                elements[5] = elemCopy[6]
                elements[6] = elemCopy[10]
                elements[7] = elemCopy[14]

                elements[8] = elemCopy[1]
                elements[9] = elemCopy[5]
                elements[10] = elemCopy[9]
                elements[11] = elemCopy[13]

                elements[12] = elemCopy[0]
                elements[13] = elemCopy[4]
                elements[14] = elemCopy[8]
                elements[15] = elemCopy[12]
            }
        }

        setElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, elements)
        fractal.rotateTo(90f, floatArrayOf(0f, 0f, 1f)) //should recreate on animation end to keep things simple
        mRecreateFractal = fractal
    }

    private fun reflectX(fractal: Fractal, topPushed: Boolean) {
        //update data
        val elements = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, fractal.mSize)
        val elemCopy = elements.copyOf()
        for(row in 0 until fractal.mSize) {
            for(col in 0 until fractal.mSize) {
                elements[col + row * fractal.mSize] = elemCopy[col + (fractal.mSize - 1 - row) * fractal.mSize]
            }
        }
        setElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, elements)

        if(topPushed) fractal.rotateTo(-180f, floatArrayOf(1f, 0f, 0f))
        else fractal.rotateTo(180f, floatArrayOf(1f, 0f, 0f))
        mRecreateFractal = fractal
    }

    private fun reflectY(fractal: Fractal, leftPushed: Boolean) {
        //update data
        val elements = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, fractal.mSize)
        val elemCopy = elements.copyOf()
        for(row in 0 until fractal.mSize) {
            for(col in 0 until fractal.mSize) {
                elements[col + row * fractal.mSize] = elemCopy[(fractal.mSize - 1 - col) + row * fractal.mSize]
            }
        }
        setElements(getOpenCubeIndex(), getOpenSquareSurface().value, fractal.mIndex, elements)

        if(leftPushed) fractal.rotateTo(-180f, floatArrayOf(0f, 1f, 0f))
        else fractal.rotateTo(180f, floatArrayOf(0f, 1f, 0f))
        mRecreateFractal = fractal
    }

    private fun split(fractal: Fractal) {
        val squarePos = calculateSurfacePos(getOpenSquareSurface(), cubeLocations[getOpenCubeIndex()])

        val index = fractal.mIndex
        val newSize = fractal.mSize / 2

        val elements0 = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, index, newSize)
        val elements1 = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, intArrayOf(index[0] + newSize, index[1]), newSize)
        val elements2 = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, intArrayOf(index[0], index[1] + newSize), newSize)
        val elements3 = getElements(getOpenCubeIndex(), getOpenSquareSurface().value, intArrayOf(index[0] + newSize, index[1] + newSize), newSize)

        val topLeft = Fractal(elements0, newSize, index,
                                calculateFractalPosForTarget(index, newSize, fractal.mIndex, fractal.mSize, squarePos))
        val topRight = Fractal(elements1, newSize, intArrayOf(index[0] + newSize, index[1]),
                                calculateFractalPosForTarget(intArrayOf(index[0] + newSize, index[1]), newSize, fractal.mIndex, fractal.mSize, squarePos))
        val bottomLeft = Fractal(elements2, newSize, intArrayOf(index[0], index[1] + newSize),
                                calculateFractalPosForTarget(intArrayOf(index[0], index[1] + newSize), newSize, fractal.mIndex, fractal.mSize, squarePos))
        val bottomRight = Fractal(elements3, newSize, intArrayOf(index[0] + newSize, index[1] + newSize),
                                calculateFractalPosForTarget(intArrayOf(index[0] + newSize, index[1] + newSize), newSize, fractal.mIndex, fractal.mSize, squarePos))

        topLeft.moveTo(calculateFractalPos(topLeft.mIndex, topLeft.mSize, squarePos))
        topRight.moveTo(calculateFractalPos(topRight.mIndex, topRight.mSize, squarePos))
        bottomLeft.moveTo(calculateFractalPos(bottomLeft.mIndex, bottomLeft.mSize, squarePos))
        bottomRight.moveTo(calculateFractalPos(bottomRight.mIndex, bottomRight.mSize, squarePos))

        mFractals.add(topLeft)
        mFractals.add(topRight)
        mFractals.add(bottomLeft)
        mFractals.add(bottomRight)

        mFractals.remove(fractal)
    }

    private fun startMerge(corners: Array<Fractal>) {
        var topLeftIndex = intArrayOf(100, 100)
        for(c in corners) {
            //update finding top left index
            if((c.mIndex[0] + c.mIndex[1]) < (topLeftIndex[0] + topLeftIndex[1]))
                topLeftIndex = c.mIndex
        }

        //animate based on top left fractal index and larger size
        val squarePos = calculateSurfacePos(getOpenSquareSurface(), cubeLocations[getOpenCubeIndex()])

        for(c in corners) {
            c.moveTo(calculateFractalPosForTarget(c.mIndex, c.mSize, topLeftIndex, c.mSize * 2, squarePos))
        }
    }

    private fun merge(corners: Array<Fractal>): Fractal {
        val newSize = corners[0].mSize * 2

        val elements = Array(newSize * newSize){FractalType.Red}

        var topLeftIndex = intArrayOf(100, 100)
        for(c in corners) {
            //update finding top left index
            if((c.mIndex[0] + c.mIndex[1]) < (topLeftIndex[0] + topLeftIndex[1]))
                topLeftIndex = c.mIndex
        }

        for(c in corners) {
            //fill in elements
            for (row in 0 until c.mSize) {
                for (col in 0 until c.mSize) {
                    val newCol = col + c.mIndex[0] - topLeftIndex[0]
                    val newRow = row + c.mIndex[1] - topLeftIndex[1]
                    elements[newCol + newRow * newSize] = puzzleData[getOpenCubeIndex()][getOpenSquareSurface().value][c.mIndex[0] + col + 4 * (c.mIndex[1] + row)]
                }
            }
        }

        val squarePos = calculateSurfacePos(getOpenSquareSurface(), cubeLocations[getOpenCubeIndex()])
        return Fractal(elements, newSize, topLeftIndex, calculateFractalPos(topLeftIndex, newSize, squarePos))
    }

    private fun getClosestFractal(x: Float, y: Float): Fractal {
        var closestFractal = mFractals[0]
        var fractalScreenCoords = closestFractal.getScreenCoords()
        var minDis = pointDistance(x, y, fractalScreenCoords[0], fractalScreenCoords[1])
        var thisDis: Float

        for(fractal in mFractals) {
            fractalScreenCoords = fractal.getScreenCoords()
            thisDis = pointDistance(x, y, fractalScreenCoords[0], fractalScreenCoords[1])
            if(thisDis < minDis) {
                closestFractal = fractal
                minDis = thisDis
            }
        }

        return closestFractal
    }

    private fun getCornerFractals(x: Float, y: Float): Array<Fractal>? {

        val closestFractal = getClosestFractal(x, y)
        val index = closestFractal.mIndex
        val size = closestFractal.mSize

        var topLeft: Fractal? = null
        var topRight: Fractal? = null
        var bottomLeft: Fractal? = null
        var bottomRight: Fractal? = null

        //determine which corner it's in
        val screenCoords = closestFractal.getScreenCoords()
        val fractalCoords = floatArrayOf((screenCoords[0] + screenCoords[2]) / 2f, (screenCoords[1] + screenCoords[3])/2f)

        if(x < fractalCoords[0]) {
            if(y < fractalCoords[1]) { //fractal is top right
                topLeft = getFractal(intArrayOf(index[0] - size, index[1]))
                topRight = closestFractal
                bottomLeft = getFractal(intArrayOf(index[0] - size, index[1] + size))
                bottomRight = getFractal(intArrayOf(index[0], index[1] + size))
            }else { //fractal is bottom right
                topLeft = getFractal(intArrayOf(index[0] - size, index[1] - size))
                topRight = getFractal(intArrayOf(index[0], index[1] - size))
                bottomLeft = getFractal(intArrayOf(index[0] - size, index[1]))
                bottomRight = closestFractal
            }
        }else {
            if(y < fractalCoords[1]) { //fractal is top left
                topLeft = closestFractal
                topRight = getFractal(intArrayOf(index[0] + size, index[1]))
                bottomLeft = getFractal(intArrayOf(index[0], index[1] + size))
                bottomRight = getFractal(intArrayOf(index[0] + size, index[1] + size))
            }else { //fractal is bottom left
                topLeft = getFractal(intArrayOf(index[0], index[1] - size))
                topRight = getFractal(intArrayOf(index[0] + size, index[1] - size))
                bottomLeft = closestFractal
                bottomRight = getFractal(intArrayOf(index[0] + size, index[1]))
            }
        }

        if(topLeft == null || topRight == null || bottomLeft == null || bottomRight == null)
            return null

        if(topLeft.mSize != size || topRight.mSize != size || bottomRight.mSize != size || bottomLeft.mSize != size) return null

        return arrayOf(topLeft, topRight, bottomLeft, bottomRight)

    }

    private fun dispatchCommand(touchType: TouchType, x: Float, y: Float): Boolean {
        when(getScreenState()) {
            Screen.Cube -> {
                if(touchType == TouchType.Tap) {
                    for (cube in mCubes) {
                        if (cube.pointCollision(x, y) == CollisionBox.Center) {
                            openCube(cube)
                            return true
                        }
                    }
                }
            }
            Screen.Square -> {
                if(touchType == TouchType.Tap) {
                    for (square in mSquares) {
                        if (square.pointCollision(x, y) == CollisionBox.Center) {
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
                    when(touchType) {
                        TouchType.FlickLeft -> {
                            if(fractal.topCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                rotateCCW(fractal)
                                return true
                            }else if(fractal.bottomCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                rotateCW(fractal)
                                return true
                            }else if(fractal.centerCollision(x, y)) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] - fractal.mSize, fractal.mIndex[1]))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                        }
                        TouchType.FlickRight -> {
                            if(fractal.topCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                rotateCW(fractal)
                                return true
                            }else if(fractal.bottomCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                rotateCCW(fractal)
                                return true
                            }else if(fractal.centerCollision(x, y)) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] + fractal.mSize, fractal.mIndex[1]))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                        }
                        TouchType.FlickUp -> {
                            if(fractal.leftCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                rotateCW(fractal)
                                return true
                            }else if(fractal.rightCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                rotateCCW(fractal)
                                return true
                            }else if(fractal.centerCollision(x, y)) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] - fractal.mSize))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                        }
                        TouchType.FlickDown -> {
                            if(fractal.leftCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                rotateCCW(fractal)
                                return true
                            }else if(fractal.rightCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                rotateCW(fractal)
                                return true
                            }else if(fractal.centerCollision(x, y)) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] + fractal.mSize))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                        }
                        TouchType.PinchOut -> {
                            if (fractal.centerCollision(x, y) && fractal.mSize > 1) {
                                split(fractal)
                                return true
                            }
                        }
                        TouchType.Tap -> {
                            if(fractal.leftCollision(x, y) && fractal.mSize > 1) {
                                reflectY(fractal, true)
                                return true
                            }else if(fractal.rightCollision(x, y) && fractal.mSize > 1) {
                                reflectY(fractal, false)
                                return true
                            }else if(fractal.topCollision(x, y) && fractal.mSize > 1) {
                                reflectX(fractal, true)
                                return true
                            }else if(fractal.bottomCollision(x, y) && fractal.mSize > 1) {
                                reflectX(fractal, false)
                                return true
                            }
                        }
                    }
                }

                if(touchType == TouchType.PinchIn) {
                    Log.d("kouch", "in pinch")
                    val cornerFractals = getCornerFractals(x, y)
                    if(cornerFractals != null) {
                        Log.d("kouch", "before start merge")
                        startMerge(cornerFractals)
                        Log.d("kouch", "after start merge")
                        mMergeFractals = cornerFractals
                        return true
                    }
                }

                if(touchType == TouchType.Tap) {
                    closeSquare(getOpenSquareSurface())
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

        if(mMergeFractals != null) {
            mFractals.add(merge(mMergeFractals!!))
            for(fractal in mMergeFractals!!) {
                mFractals.remove(fractal)
            }
            mMergeFractals = null
        }

        if(mRecreateFractal != null) {
            mFractals.add(Fractal(getElements(getOpenCubeIndex(), getOpenSquareSurface().value, mRecreateFractal!!.mIndex, mRecreateFractal!!.mSize),
                    mRecreateFractal!!.mSize, mRecreateFractal!!.mIndex, mRecreateFractal!!.pos))
            mFractals.remove(mRecreateFractal!!)
            mRecreateFractal = null
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
        val deltaTime = 0.0015f * (mCurrentTime - mPreviousTime).toInt()


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