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


class SquaresRenderer(context: Context): GLSurfaceView.Renderer {
    var mAnimationParameter = 0f //main animation timer
    var mSets = mutableListOf<Set>()
    var mSquares = mutableListOf<Square>()
    var mFractals = mutableListOf<Fractal>()
    lateinit var mCamera: Camera
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private var mCurrentTime: Long = 0
    private var mPreviousTime: Long = 0
    private val mContext = context
    var mInputQueue = InputQueue()
    private var mMergeFractals: Array<Fractal>? = null
    private var mRecreateFractal: Fractal? = null


    companion object {
        var mTextureHandle = -1
        var mProgram = -1
        val mVPMatrix = FloatArray(16)

        var mPosAttrib = -1
        var mTexCoordAttrib = -1
        var mTexUniform = -1
        var mModelUniform = -1
    }

    private fun compileShaders(): Int {

        val vertexShaderCode = "attribute vec4 aPosition;" +
                "attribute vec2 aTexCoord;" +
                "uniform mat4 uMVPMatrix;" +
                "varying vec2 vTexCoord;" +
                "void main() {" +
                "   vTexCoord = aTexCoord;" +
                "   gl_Position = uMVPMatrix * aPosition;" +
                "}"

        val fragmentShaderCode = "precision mediump float;" +
                "uniform sampler2D uTexture;" +
                "varying vec2 vTexCoord;" +
                "void main() {" +
                "   gl_FragColor = texture2D(uTexture, vTexCoord);" +
                "}"

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
        mProgram = compileShaders()


        for(i in cubeLocations.indices) {
//            mCubes.add(Cube(puzzleData[i], i, false))
            mSets.add(Set(cubeLocations[i], i))
        }


        mCamera = Camera(floatArrayOf(0f, 0f, 3f))
        mCamera.moveTo(floatArrayOf(0f, 0f, 98f))

    }

    private fun openSet(set: Set) {
        for(s in mSets) {
            s.fadeTo(0f)
        }
        set.mIsOpen = true
        mCamera.moveTo(floatArrayOf(set.pos[0], set.pos[1], 15f))
        mSquares = set.spawnSquares()
    }

    private fun closeSet(set: Set) {
        for(s in mSets) {
            s.fadeTo(1f)
        }
        set.mIsOpen = false
        mCamera.moveTo(floatArrayOf(0f, 0f, 98f))
        mSquares.clear()
    }

    private fun openSquare(square: Square) {
        mCamera.moveTo(floatArrayOf(square.pos[0], square.pos[1], 4f))
        mFractals = square.spawnFractals(puzzleData[getOpenSet()!!.mIndex][square.mIndex]) //temp: just grabbing first index
        for(fractal in mFractals) {
            fractal.moveTo(calculateFractalPosForTarget(fractal.mIndex, fractal.mSize, fractal.mIndex, 1, square.pos))
        }

        square.mIsOpen = true

        for(s in mSquares) {
            s.fadeTo(0f)
        }
    }

    private fun closeSquare(square: Square) {
        val cubePos = cubeLocations[getOpenSet()!!.mIndex]
        for (fractal in mFractals) {
            fractal.moveTo(calculateFractalPosForTarget(fractal.mIndex, fractal.mSize, intArrayOf(0, 0), 4, square.pos))
        }
        mCamera.moveTo(floatArrayOf(cubePos[0], cubePos[1] + .25f * 4f / 2f, 15f))

        for(s in mSquares) {
            s.fadeTo(1f)
        }

        square.mIsOpen = false

        mFractals.clear()
    }

    private fun getScreenState(): Screen {
        if(mFractals.size > 0) return Screen.Fractal
        if(mSquares.size > 0) return Screen.Square
        return Screen.Set
    }

    private fun getOpenSet(): Set? {
        for(set in mSets) {
            if(set.mIsOpen) return set
        }
        return null
    }


    private fun getOpenSquare(): Square? {
        for(square in mSquares) {
            if(square.mIsOpen) return square
        }
        return null
    }



    //////////////helper functions for commands//////////////////////////////////////////
    private fun getFractal(index: IntArray): Fractal? {
        if(index[0] < 0 || index[0] >=4 || index[1] < 0 || index[1] >= 6) return null

        for(fractal in mFractals) {
            if(fractal.mIndex[0] == index[0] && fractal.mIndex[1] == index[1]) return fractal
        }

        return null
    }

    private fun getSquare(index: Int): Square? {
        if(index >= 24) return null

        for(square in mSquares) {
            if(index == square.mIndex) return square
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


    private fun swapSquares(square0: Square, square1: Square) {
        //update data
        puzzleData[getOpenSet()!!.mIndex][square0.mIndex] = puzzleData[getOpenSet()!!.mIndex][square1.mIndex].also {
            puzzleData[getOpenSet()!!.mIndex][square1.mIndex] = puzzleData[getOpenSet()!!.mIndex][square0.mIndex]
        }

        //animate
        square0.moveTo(square1.pos)
        square1.moveTo(square0.pos)
        square0.mIndex = square1.mIndex.also {square1.mIndex = square0.mIndex}
    }



    private fun swap(fractal0: Fractal, fractal1: Fractal) {
        //update data
        val elements0 = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal0.mIndex, fractal0.mSize)
        val elements1 = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal1.mIndex, fractal1.mSize)
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal0.mIndex, elements1)
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal1.mIndex, elements0)

        //animate
        fractal0.moveTo(fractal1.pos)
        fractal1.moveTo(fractal0.pos)
        fractal0.mIndex = fractal1.mIndex.also {fractal1.mIndex = fractal0.mIndex}
    }

    private fun rotateCW(fractal: Fractal) {
        val elements = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, fractal.mSize)

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

        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, elements)
        fractal.rotateTo(-90f, floatArrayOf(0f, 0f, 1f))
        mRecreateFractal = fractal
    }

    private fun rotateCCW(fractal: Fractal) {
        val elements = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, fractal.mSize)

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

        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, elements)
        fractal.rotateTo(90f, floatArrayOf(0f, 0f, 1f)) //should recreate on animation end to keep things simple
        mRecreateFractal = fractal
    }

    private fun reflectX(fractal: Fractal, topPushed: Boolean) {
        //update data
        val elements = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, fractal.mSize)
        val elemCopy = elements.copyOf()
        for(row in 0 until fractal.mSize) {
            for(col in 0 until fractal.mSize) {
                elements[col + row * fractal.mSize] = elemCopy[col + (fractal.mSize - 1 - row) * fractal.mSize]
            }
        }
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, elements)

        if(topPushed) fractal.rotateTo(-180f, floatArrayOf(1f, 0f, 0f))
        else fractal.rotateTo(180f, floatArrayOf(1f, 0f, 0f))
        mRecreateFractal = fractal
    }

    private fun reflectY(fractal: Fractal, leftPushed: Boolean) {
        //update data
        val elements = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, fractal.mSize)
        val elemCopy = elements.copyOf()
        for(row in 0 until fractal.mSize) {
            for(col in 0 until fractal.mSize) {
                elements[col + row * fractal.mSize] = elemCopy[(fractal.mSize - 1 - col) + row * fractal.mSize]
            }
        }
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, elements)

        if(leftPushed) fractal.rotateTo(-180f, floatArrayOf(0f, 1f, 0f))
        else fractal.rotateTo(180f, floatArrayOf(0f, 1f, 0f))
        mRecreateFractal = fractal
    }

    private fun split(fractal: Fractal) {
        val squarePos = getOpenSquare()!!.pos

        val index = fractal.mIndex
        val newSize = fractal.mSize / 2

        val elements0 = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, index, newSize)
        val elements1 = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, intArrayOf(index[0] + newSize, index[1]), newSize)
        val elements2 = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, intArrayOf(index[0], index[1] + newSize), newSize)
        val elements3 = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, intArrayOf(index[0] + newSize, index[1] + newSize), newSize)

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


        for(c in corners) {
            c.moveTo(calculateFractalPosForTarget(c.mIndex, c.mSize, topLeftIndex, c.mSize * 2, getOpenSquare()!!.pos))
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
                    elements[newCol + newRow * newSize] = puzzleData[getOpenSet()!!.mIndex][getOpenSquare()!!.mIndex][c.mIndex[0] + col + 4 * (c.mIndex[1] + row)]
                }
            }
        }

        return Fractal(elements, newSize, topLeftIndex, calculateFractalPos(topLeftIndex, newSize, getOpenSquare()!!.pos))
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

        if(topLeft.mIsBlock || topRight.mIsBlock || bottomRight.mIsBlock || bottomLeft.mIsBlock) return null

        return arrayOf(topLeft, topRight, bottomLeft, bottomRight)

    }

    private fun dispatchCommand(touchType: TouchType, x: Float, y: Float): Boolean {
        when(getScreenState()) {
            Screen.Set -> {
                if(touchType == TouchType.Tap) {
                    for (set in mSets) {
                        if (set.pointCollision(x, y) == CollisionBox.Center) {
                            openSet(set)
                            return true
                        }
                    }
                }
            }
            Screen.Square -> {
                for (square in mSquares) {
                    if(touchType == TouchType.Tap && square.centerCollision(x, y)) {
                        openSquare(square)
                        return true
                    }
                }

                if(touchType == TouchType.PinchIn) {
                    val openSet = getOpenSet()
                    if(openSet != null)
                        closeSet(openSet)
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
                            }else if(fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] - fractal.mSize, fractal.mIndex[1]))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
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
                            }else if(fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] + fractal.mSize, fractal.mIndex[1]))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
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
                            }else if(fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] - fractal.mSize))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
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
                            }else if(fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] + fractal.mSize))
                                if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
                                    swap(fractal, swappedFractal)
                                    return true
                                }
                            }
                        }
                        TouchType.PinchOut -> { //a block of size > 1 should never exist, so won't check for it
                            if (fractal.centerCollision(x, y) && fractal.mSize > 1) {
                                split(fractal)
                                return true
                            }
                        }
                        TouchType.Tap -> { //a block of size > 1 should never exists, so won't check for it
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
                    val cornerFractals = getCornerFractals(x, y) //checks for blocks in this function and returns null if ANY fractal is a block
                    if(cornerFractals != null) {
                        startMerge(cornerFractals)
                        mMergeFractals = cornerFractals
                        return true
                    }
                }

                if(touchType == TouchType.Tap) {
                    val openSquare = getOpenSquare()
                    if(openSquare != null)
                        closeSquare(openSquare)
                    return true
                }
            }
        }

        return false
    }


    private fun onAnimationEnd() {
        for(set in mSets) {
            set.onAnimationEnd()
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

        if(mMergeFractals != null) {
            mFractals.add(merge(mMergeFractals!!))
            for(fractal in mMergeFractals!!) {
                mFractals.remove(fractal)
            }
            mMergeFractals = null
        }

        //recreate at end of animation (to reduce complexity by not having to save angle state, mostly)
        if(mRecreateFractal != null) {
            mFractals.add(Fractal(getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, mRecreateFractal!!.mIndex, mRecreateFractal!!.mSize),
                    mRecreateFractal!!.mSize, mRecreateFractal!!.mIndex, mRecreateFractal!!.pos))
            mFractals.remove(mRecreateFractal!!)
            mRecreateFractal = null
        }

        //////////////////////////////////////////////////////////////////////////////////////////
    }

    override fun onDrawFrame(unused: GL10) {

        ////////////////////////////update setup///////////////////////////////////////////

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

        for(set in mSets) {
            set.onUpdate(sigmoid)
        }

        /////////////////////////////////////draw setup////////////////////////////////////
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.setLookAtM(mViewMatrix, 0, mCamera.pos[0], mCamera.pos[1], mCamera.pos[2], mCamera.pos[0], mCamera.pos[1], 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        GLES20.glUseProgram(mProgram)
        mPosAttrib = GLES20.glGetAttribLocation(mProgram, "aPosition")
        mTexCoordAttrib = GLES20.glGetAttribLocation(mProgram, "aTexCoord")
        mTexUniform = GLES20.glGetUniformLocation(mProgram, "uTexture")
        mModelUniform = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        GLES20.glEnableVertexAttribArray(mPosAttrib)
        GLES20.glEnableVertexAttribArray(mTexCoordAttrib)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle)
        GLES20.glUniform1i(mTexUniform, 0)

        //////////////////////draw////////////////////////////////////
        for(fractal in mFractals) {
            fractal.draw(mVPMatrix)
        }

        for(square in mSquares) {
            square.draw(mVPMatrix)
        }

        for(set in mSets) {
            set.draw(mVPMatrix)
        }

        //////////////////////draw cleanup/////////////////////////////
        GLES20.glDisableVertexAttribArray(mPosAttrib)
        GLES20.glDisableVertexAttribArray(mTexCoordAttrib)

    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height

        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f) //allow depth up to 100f away from camera
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 103f) //allow depth up to 100 units away
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


}