package com.example.androidsquares


import androidx.appcompat.app.AppCompatActivity

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.os.SystemClock
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Log
import java.util.Stack
import java.util.Deque
import java.util.ArrayDeque

import android.opengl.GLSurfaceView
import android.opengl.GLES20
import android.opengl.Matrix
import android.opengl.GLUtils


class SquaresRenderer(context: Context): GLSurfaceView.Renderer {
    var mAnimationParameter = 1f //main animation timer
    var mAnimationSpeed = 1f
    var mSets = mutableListOf<Set>()
    var mSquares = mutableListOf<Square>()
    var mFractals = mutableListOf<Fractal>()
    lateinit var mUndoButton: UndoButton
    lateinit var mCamera: Camera
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private var mCurrentTime: Long = 0
    private var mPreviousTime: Long = 0
    private val mContext = context
    var mInputQueue = InputQueue()
    private var mMergeFractals: Array<MutableList<Fractal>>? = null
    private var mRecreateFractals = mutableListOf<Fractal>()
    private var mAnimationQueue: Deque<() -> Float> = ArrayDeque()
    private var mUndoQueue: Deque<UndoData> = ArrayDeque()
    private var mPulseFractals = Stack<MutableList<Fractal>>()
    private var mPulseIndices = Stack<MutableList<IntArray>>()
    private var mClearedPuzzleIndex  = -1
    private var mClearedSetIndex = -1
    private var mLogo = Box(floatArrayOf(0f, 0f, 0f), floatArrayOf(10f, 10f, 1f), getTexCoords(F.W))

    private var mScreenWidth = 0
    private var mScreenHeight = 0

    private var mFirstDraw = true


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
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
//        GLES20.glBlendFunc(GLES20.GL_CONSTANT_ALPHA, GLES20.GL_ONE_MINUS_CONSTANT_ALPHA)
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
 //       GLES20.glDepthFunc(GLES20.GL_LESS)
//        GLES20.glEnable(GLES20.GL_CULL_FACE)
        //GLES20.glCullFace(GLES20.GL_BACK)

        readSaveData()

        mTextureHandle = loadTexture(mContext, R.drawable.fractal_colors)
        mProgram = compileShaders()

        mCamera = Camera(floatArrayOf(0f, 0f, 98f))
        mUndoButton = UndoButton(3, 0, floatArrayOf(0f, 100f, 0f))
//        startAnimation(openGame())
    }

    private fun startAnimation(speed: Float) {
        mAnimationParameter = 0f
        mAnimationSpeed = speed
    }

    private fun spawnSets(): MutableList<Set> {
        val list = mutableListOf<Set>()
        var pos: FloatArray
        for (setIndex in appData.setData.indices) {
            pos = calculateSetPosition(setIndex)
            val offset = if (setIndex % 2 == 0) -35f //offset is only here to allow it to animate on/off screen
            else 35f
            if(appData.setData[setIndex] != null) {
                list.add(Set(floatArrayOf(pos[0] + offset, pos[1], pos[2]), setIndex, appData.setData[setIndex]!!.isLocked, appData.setData[setIndex]!!.isCleared))
            }
        }
        return list
    }

    fun openGame(): AnimationSpeed {
        mSets = spawnSets()
        for (set in mSets) {
            set.moveTo(calculateSetPosition(set.mIndex))
        }
        mLogo.moveTo(floatArrayOf(30f, 0f, 0f))
        return NORMAL_ANIMATION
    }

    fun closeGame(): AnimationSpeed {
        var pos: FloatArray
        for (set in mSets) {
            //pos = appData.setData[set.mIndex].pos
            pos = calculateSetPosition(set.mIndex)
            val offset = if (set.mIndex % 2 == 0) -35f
            else 35f
            set.moveTo(floatArrayOf(pos[0] + offset, pos[1], pos[2]))
        }
        mLogo.moveTo(floatArrayOf(0f, 0f, 0f))
        mAnimationQueue.add(::clearSetList)

        return NORMAL_ANIMATION
    }

    private fun openSet(set: Set): AnimationSpeed {
        for (s in mSets) {
            val offset = if (s.mIndex % 2 == 0) -35f
            else 35f
            s.moveTo(floatArrayOf(s.pos[0] + offset, s.pos[1], s.pos[2]))
        }
        set.mIsOpen = true
        mSquares = set.spawnSquares()
        for (square in mSquares) {
            square.setAlphaData(0f)
            square.fadeTo(1f)
        }

        return NORMAL_ANIMATION
    }

    private fun closeSet(): AnimationSpeed {
        for (s in mSets) {
            s.moveTo(calculateSetPosition(s.mIndex))
        }
        val set = getOpenSet()
        set!!.mIsOpen = false

        mAnimationQueue.add(::clearSquaresList)

        for(s in mSquares) {
            s.moveTo(set.pos)
        }

        return NORMAL_ANIMATION
    }

    private fun openSquare(square: Square): AnimationSpeed {
        val maxTransformations = appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[square.mIndex]!!.maxTransformations
        val transformationCount = appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[square.mIndex]!!.undoStack.size
        mUndoButton = UndoButton(maxTransformations, transformationCount, floatArrayOf(0f, 100f, 0f))
        mUndoButton.moveTo(floatArrayOf(0f, 28f, 0f))


        mFractals = square.spawnFractals(appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[square.mIndex]!!.elements) //temp: just grabbing first index
        val puzzleDim = getPuzzleDim(getOpenSet()!!.mIndex, square.mIndex)
        for (fractal in mFractals) {
            fractal.mShowClearedBox = appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[square.mIndex]!!.isCleared
            fractal.setAlphaData(0f)
            fractal.fadeTo(1f)
            fractal.moveTo(
                calculateFractalPosForTarget(
                    fractal.mIndex,
                    fractal.mSize,
                    fractal.mIndex,
                    1,
                    square.pos,
                    puzzleDim
                )
            )
        }

        square.mIsOpen = true

        for (s in mSquares) {
            s.moveTo(getOpenSet()!!.pos)
        }

        return NORMAL_ANIMATION
    }

    private fun closeSquare(): AnimationSpeed {
        val puzzleDim = getPuzzleDim(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex)
        for (fractal in mFractals) {
            fractal.moveTo(
                calculateFractalPosForTarget(
                    fractal.mIndex,
                    fractal.mSize,
                    intArrayOf(0, 0),
                    4,
                    getOpenSquare()!!.pos,
                    puzzleDim
                )
            )
        }
        mUndoButton.moveTo(floatArrayOf(0f, 100f, 0f))
        mUndoButton.fadeTo(0f)

        mSquares = getOpenSet()!!.spawnSquares()

        mAnimationQueue.add(::clearFractalsList)

        for(f in mFractals) {
            f.moveTo(getOpenSet()!!.pos) //temp: moving to set position for now
        }

        return NORMAL_ANIMATION
    }

    private fun clearSquaresList(): AnimationSpeed {
        mSquares.clear()
        return SKIP_ANIMATION
    }

    private fun clearFractalsList(): AnimationSpeed {
        mFractals.clear()
        return SKIP_ANIMATION
    }

    private fun clearSetList(): AnimationSpeed {
        mSets.clear()
        return SKIP_ANIMATION
    }

    fun getScreenState(): Screen {
        if (mFractals.size > 0) return Screen.Fractal
        if (mSquares.size > 0) return Screen.Square
        if(mSets.size > 0) return Screen.Set
        return Screen.Logo
    }

    private fun getOpenSet(): Set? {
        for (set in mSets) {
            if (set.mIsOpen) return set
        }
        myAssert(false, "No open set found!")
        return null
    }


    private fun getOpenSquare(): Square? {
        for (square in mSquares) {
            if (square.mIsOpen) return square
        }

        myAssert(false, "No open square found!")
        return null
    }


    //////////////helper functions for commands//////////////////////////////////////////
    private fun getFractal(index: IntArray): Fractal? {
        if (index[0] < 0 || index[0] >= MAX_PUZZLE_WIDTH || index[1] < 0 || index[1] >= MAX_PUZZLE_HEIGHT) return null

        for (fractal in mFractals) {
            if (fractal.mIndex[0] == index[0] && fractal.mIndex[1] == index[1]) return fractal
        }

        return null
    }

    private fun getElements(setIndex: Int, squareIndex: Int, index: IntArray, size: Int): Array<F> {
        val elements = Array(size * size) { F.N }
        for (row in 0 until size) {
            for (col in 0 until size) {
                elements[col + row * size] = appData.setData[setIndex]!!.puzzleData[squareIndex]!!.elements[index[0] + col + (index[1] + row) * MAX_PUZZLE_WIDTH]
            }
        }
        return elements
    }

    private fun setElements(setIndex: Int, squareIndex: Int, index: IntArray, elements: Array<F>) {
        val size = when (elements.size) {
            1 -> 1
            4 -> 2
            16 -> 4
            else -> -1
        }

        myAssert(size != -1, "invalid element list size")

        for (row in 0 until size) {
            for (col in 0 until size) {
                appData.setData[setIndex]!!.puzzleData[squareIndex]!!.elements[index[0] + col + (index[1] + row) * MAX_PUZZLE_WIDTH] = elements[col + row * size]
            }
        }
    }

    private fun setCleared(set: Set): Boolean {
        for (puzzleData in appData.setData[set.mIndex]!!.puzzleData) {
            if (puzzleData != null && !puzzleData.isCleared) return false
        }
        return true
    }

    private fun gameCleared(): Boolean {
        for(set in appData.setData) {
            if(set != null && !set.isCleared) return false
        }
        return true
    }


    private fun puzzleCleared(elements: Array<F>, dim: IntArray): Boolean {
        val redList: MutableList<IntArray> = mutableListOf()
        val greenList: MutableList<IntArray> = mutableListOf()
        val blueList: MutableList<IntArray> = mutableListOf()
        if (!colorCleared(elements, dim, F.R, redList)) return false
        if (!colorCleared(elements, dim, F.G, greenList)) return false
        if (!colorCleared(elements, dim, F.B, blueList)) return false

        var chainLength = if(redList.size > greenList.size) redList.size else greenList.size
        if(blueList.size > chainLength) chainLength = blueList.size

        mPulseIndices.clear()


        for(i in 0 until chainLength) {
            val group: MutableList<IntArray> = mutableListOf()
            if(i < redList.size) {
                group.add(redList[i])
            }
            if(i < greenList.size) {
                group.add(greenList[i])
            }
            if(i < blueList.size) {
                group.add(blueList[i])
            }
            if(group.isNotEmpty()) {
                mPulseIndices.push(group)
            }
        }

        return true
    }

    //how about this?  colorCleared returns a mutableList of Fractals (in order they how up in DFS) if that color is cleared
    //otherwise it returns an empty list
    private fun colorCleared(elements: Array<F>, dim: IntArray, fractalType: F, indexList: MutableList<IntArray>): Boolean {
        val targetColor0: F
        val targetColor1: F

        if (fractalType == F.R || fractalType == F.RB) {
            targetColor0 = F.R
            targetColor1 = F.RB
        } else if (fractalType == F.G || fractalType == F.GB) {
            targetColor0 = F.G
            targetColor1 = F.GB
        } else if (fractalType == F.B || fractalType == F.BB) {
            targetColor0 = F.B
            targetColor1 = F.BB
        } else {
            return true //if not one of the colors to check, just ignore it and return true
        }

        //find first occurrence of block
        var rootIndex = intArrayOf(-1, -1)
        for (i in elements.indices) {
            if (elements[i] == targetColor1) {
                rootIndex = intArrayOf(i % dim[0], i / dim[0])
                break
            }
        }

        if (rootIndex[0] == -1) return true

        val visited = BooleanArray(elements.size) { false }
        DFS(elements, visited, dim, rootIndex, arrayOf(targetColor0, targetColor1), indexList)

        //check if all elements of given target colors are marked true
        for (i in elements.indices) {
            if (elements[i] == targetColor0 || elements[i] == targetColor1) {
                if (!visited[i]) return false
            }
        }
        return true
    }

    private fun DFS(elements: Array<F>, visited: BooleanArray, dim: IntArray, rootIndex: IntArray, targetColors: Array<F>, indexList: MutableList<IntArray>) {
        //mark root as visited and add that fractal to list
        visited[rootIndex[0] + rootIndex[1] * dim[0]] = true
        indexList.add(rootIndex) //problem here is due to not all fractals being sized 1x1

        //check the four adjacent indices.  If not visited and of proper color, call DFS on them.  If visited, skip
        //top
        var col = rootIndex[0]
        var row = rootIndex[1] - 1
        if (row >= 0 && !visited[col + row * dim[0]] && (elements[col + row * dim[0]] == targetColors[0] || elements[col + row * dim[0]] == targetColors[1]))
            DFS(elements, visited, dim, intArrayOf(col, row), targetColors, indexList)

        //bottom
        col = rootIndex[0]
        row = rootIndex[1] + 1
        if (row < dim[1] && !visited[col + row * dim[0]] && (elements[col + row * dim[0]] == targetColors[0] || elements[col + row * dim[0]] == targetColors[1]))
            DFS(elements, visited, dim, intArrayOf(col, row), targetColors, indexList)

        //left
        col = rootIndex[0] - 1
        row = rootIndex[1]
        if (col >= 0 && !visited[col + row * dim[0]] && (elements[col + row * dim[0]] == targetColors[0] || elements[col + row * dim[0]] == targetColors[1]))
            DFS(elements, visited, dim, intArrayOf(col, row), targetColors, indexList)

        //right
        col = rootIndex[0] + 1
        row = rootIndex[1]
        if (col < dim[0] && !visited[col + row * dim[0]] && (elements[col + row * dim[0]] == targetColors[0] || elements[col + row * dim[0]] == targetColors[1]))
            DFS(elements, visited, dim, intArrayOf(col, row), targetColors, indexList)
    }

    private fun unlockAdjacentPuzzles(setIndex: Int, puzzleIndex: Int) {
        val col = puzzleIndex % PUZZLE_GRID_WIDTH
        val row = puzzleIndex / PUZZLE_GRID_WIDTH
        //to the right
        val rightIndex = col + 1 + row * PUZZLE_GRID_WIDTH
        if (col + 1 < PUZZLE_GRID_WIDTH && rightIndex < appData.setData[setIndex]!!.puzzleData.size && appData.setData[setIndex]!!.puzzleData[rightIndex] != null) {
            appData.setData[setIndex]!!.puzzleData[rightIndex]!!.isLocked = false
        }
        //to the left
        val leftIndex = col - 1 + row * PUZZLE_GRID_WIDTH
        if (col - 1 >= 0 && leftIndex < appData.setData[setIndex]!!.puzzleData.size && appData.setData[setIndex]!!.puzzleData[leftIndex] != null) {
            appData.setData[setIndex]!!.puzzleData[leftIndex]!!.isLocked = false
        }
        //to the top
        val topIndex = col + (row - 1) * PUZZLE_GRID_WIDTH
        if (row - 1 >= 0 && topIndex < appData.setData[setIndex]!!.puzzleData.size && appData.setData[setIndex]!!.puzzleData[topIndex] != null) {
            appData.setData[setIndex]!!.puzzleData[topIndex]!!.isLocked = false
        }
        //to the bottom
        val bottomIndex = col + (row + 1) * PUZZLE_GRID_WIDTH
        if (row + 1 < PUZZLE_GRID_HEIGHT && bottomIndex < appData.setData[setIndex]!!.puzzleData.size && appData.setData[setIndex]!!.puzzleData[bottomIndex] != null) {
            appData.setData[setIndex]!!.puzzleData[bottomIndex]!!.isLocked = false
        }
    }


    private fun getTransformationsRemaining(setIndex: Int, puzzleIndex: Int): Int {
        return appData.setData[setIndex]!!.puzzleData[puzzleIndex]!!.maxTransformations - appData.setData[setIndex]!!.puzzleData[puzzleIndex]!!.undoStack.size
    }

    private fun pushTransformation(setIndex: Int, puzzleIndex: Int, undoData: UndoData) {
        myAssert(getTransformationsRemaining(setIndex, puzzleIndex) > 0, "No transformations remaining")
        appData.setData[setIndex]!!.puzzleData[puzzleIndex]!!.undoStack.push(undoData)
        mUndoButton.increment()
    }

    private fun getIndexForPuzzleType(puzzleType: PuzzleType, baseIndex: IntArray): IntArray {
        return when(puzzleType) {
            PuzzleType.Normal -> intArrayOf(-1, -1) //no fractal
            PuzzleType.Shifted -> intArrayOf(baseIndex[0], (baseIndex[1] + 3) % 6)
            PuzzleType.Reflected -> intArrayOf(baseIndex[0], 5 - baseIndex[1])
        }
    }

    //assumes transformation is valid (remaining transformations, swapped fractal exists)
    //if 'undo' is true, will not push transformation onto stack
    private fun transform(fractal: Fractal, transformation: Transformation, undo: Boolean): AnimationSpeed {
        val setIndex = getOpenSet()!!.mIndex
        val puzzleIndex = getOpenSquare()!!.mIndex
        val puzzleType = appData.setData[setIndex]!!.puzzleData[puzzleIndex]!!.puzzleType
        when (transformation) {
            Transformation.TranslatePosX -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    val otherSwapped: Fractal? = getFractal(intArrayOf(otherIndex[0] + fractal.mSize, otherIndex[1]))
                    swap(otherFractal!!, otherSwapped!!)
                }

                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] + fractal.mSize, fractal.mIndex[1]))
                swap(fractal, swappedFractal!!)

                if (!undo)
                    pushTransformation(setIndex, puzzleIndex, UndoData(Transformation.TranslateNegX, fractal.mIndex, fractal.mSize))
            }
            Transformation.TranslateNegX -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    val otherSwapped: Fractal? = getFractal(intArrayOf(otherIndex[0] - fractal.mSize, otherIndex[1]))
                    swap(otherFractal!!, otherSwapped!!)
                }

                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] - fractal.mSize, fractal.mIndex[1]))
                swap(fractal, swappedFractal!!)
                if (!undo)
                    pushTransformation(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, UndoData(Transformation.TranslatePosX, fractal.mIndex, fractal.mSize))
            }
            Transformation.TranslatePosY -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    val otherSwapped: Fractal? = getFractal(intArrayOf(otherIndex[0], otherIndex[1] + fractal.mSize))
                    swap(otherFractal!!, otherSwapped!!)
                }
                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] + fractal.mSize))
                swap(fractal, swappedFractal!!)
                if (!undo)
                    pushTransformation(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, UndoData(Transformation.TranslateNegY, fractal.mIndex, fractal.mSize))
            }
            Transformation.TranslateNegY -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    val otherSwapped: Fractal? = getFractal(intArrayOf(otherIndex[0], otherIndex[1] - fractal.mSize))
                    swap(otherFractal!!, otherSwapped!!)
                }
                val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] - fractal.mSize))
                swap(fractal, swappedFractal!!)
                if (!undo)
                    pushTransformation(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, UndoData(Transformation.TranslatePosY, fractal.mIndex, fractal.mSize))
            }
            Transformation.RotateCW -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    rotateCW(otherFractal!!)
                }
                rotateCW(fractal)
                if (!undo)
                    pushTransformation(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, UndoData(Transformation.RotateCCW, fractal.mIndex, fractal.mSize))
            }
            Transformation.RotateCCW -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    rotateCCW(otherFractal!!)
                }
                rotateCCW(fractal)
                if (!undo)
                    pushTransformation(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, UndoData(Transformation.RotateCW, fractal.mIndex, fractal.mSize))
            }
            Transformation.ReflectXTop -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    reflectX(otherFractal!!, true)
                }
                reflectX(fractal, true)
                if (!undo)
                    pushTransformation(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, UndoData(Transformation.ReflectXBottom, fractal.mIndex, fractal.mSize))
            }
            Transformation.ReflectXBottom -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    reflectX(otherFractal!!, false)
                }
                reflectX(fractal, false)
                if (!undo)
                    pushTransformation(
                            getOpenSet()!!.mIndex,
                            getOpenSquare()!!.mIndex,
                            UndoData(Transformation.ReflectXTop, fractal.mIndex, fractal.mSize)
                    )
            }
            Transformation.ReflectYLeft -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    reflectY(otherFractal!!, true)
                }
                reflectY(fractal, true)
                if (!undo)
                    pushTransformation(
                            getOpenSet()!!.mIndex,
                            getOpenSquare()!!.mIndex,
                            UndoData(Transformation.ReflectYRight, fractal.mIndex, fractal.mSize)
                    )
            }
            Transformation.ReflectYRight -> {
                if(puzzleType == PuzzleType.Shifted) {
                    val otherIndex: IntArray = getIndexForPuzzleType(puzzleType, fractal.mIndex)
                    val otherFractal: Fractal? = getFractal(otherIndex)
                    reflectY(otherFractal!!, false)
                }
                reflectY(fractal, false)
                if (!undo)
                    pushTransformation(
                            getOpenSet()!!.mIndex,
                            getOpenSquare()!!.mIndex,
                            UndoData(Transformation.ReflectYLeft, fractal.mIndex, fractal.mSize)
                    )
            }
        }

        if (!undo && puzzleCleared(appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[getOpenSquare()!!.mIndex]!!.elements, intArrayOf(MAX_PUZZLE_WIDTH, MAX_PUZZLE_HEIGHT))) {
            if(!appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[getOpenSquare()!!.mIndex]!!.isCleared) { //full animation if cleared for first time
                mClearedPuzzleIndex = getOpenSquare()!!.mIndex
                mAnimationQueue.add(::clearSplit)
                mAnimationQueue.add(::clearPushPulseFractals)
                mAnimationQueue.add(::clearShowClearedBoxes)
                mAnimationQueue.add(::closeSquare)
                mAnimationQueue.add(::clearPuzzle)
                mAnimationQueue.add(::unlockPuzzles)
            }else { //only pulse blocks if cleared multiple times
                mAnimationQueue.add(::clearShowClearedBoxes)
            }
        }

        return NORMAL_ANIMATION
    }

    private fun clearGame(): AnimationSpeed {
        //final animation for clearing all puzzles
        return NORMAL_ANIMATION
    }

    private fun clearSet(): AnimationSpeed {
        for(set in mSets) {
            if(set.mIndex == mClearedSetIndex) {
                set.scalePulse(floatArrayOf(set.scale[0] * 2f, set.scale[1] * 2f, 1f))
                set.clear()
            }
        }
        return NORMAL_ANIMATION
    }

    private fun unlockAdjacentSets(setIndex: Int) {
        for(index in appData.setData.indices) {
            if(appData.setData[index] != null && isAdjacent(setIndex, index, SET_GRID_WIDTH, SET_GRID_HEIGHT)) {
                appData.setData[index]!!.isLocked = false
            }
        }
    }

    private fun unlockSets(): AnimationSpeed {
        unlockAdjacentSets(mClearedSetIndex)
        for(set in mSets) {
            if(isAdjacent(mClearedSetIndex, set.mIndex, SET_GRID_WIDTH, SET_GRID_HEIGHT) && set.mIsLocked) {
                set.scalePulse(floatArrayOf(set.scale[0] * 2f, set.scale[1] * 2f, 1f))
                set.unlock()
            }
        }

        if(gameCleared()) {
            mAnimationQueue.add(::clearGame)
            return SKIP_ANIMATION
        }

        return NORMAL_ANIMATION
    }

    private fun clearPuzzle(): AnimationSpeed {
        appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[mClearedPuzzleIndex]!!.isCleared = true
        for(puzzle in mSquares) {
            if(puzzle.mIndex == mClearedPuzzleIndex) {
                puzzle.scalePulse(floatArrayOf(puzzle.scale[0] * 2f, puzzle.scale[1] * 2f, 1f))
                puzzle.clear()
            }
        }
        return NORMAL_ANIMATION
    }

    private fun isAdjacent(index1: Int, index2: Int, gridWidth: Int, gridHeight: Int): Boolean {
        //convert to rows and cols and then compare (delta in one dimension should be zero, and the other one -1 or 1)
        val col1 = index1 % gridWidth
        val row1 = index1 / gridWidth
        val col2 = index2 % gridWidth
        val row2 = index2 / gridWidth

        //use index 1 as base.  Shift in each cardinal direction and compare to index 2
        if(col1 + 1 == col2 && row1 == row2) return true
        if(col1 - 1 == col2 && row1 == row2) return true
        if(col1 == col2 && row1 + 1 == row2) return true
        if(col1 == col2 && row1 - 1 == row2) return true

        return false
    }

    private fun unlockPuzzles(): AnimationSpeed {
        unlockAdjacentPuzzles(getOpenSet()!!.mIndex, mClearedPuzzleIndex) //need this bc it's called when when loading data

        for(puzzle in mSquares) {
            if(isAdjacent(mClearedPuzzleIndex, puzzle.mIndex, PUZZLE_GRID_WIDTH, PUZZLE_GRID_HEIGHT) && puzzle.mIsLocked) {
                puzzle.scalePulse(floatArrayOf(puzzle.scale[0] * 2f, puzzle.scale[1] * 2f, 1f))
                puzzle.unlock()
            }
        }

        if (setCleared(getOpenSet()!!)) {
            mClearedSetIndex = getOpenSet()!!.mIndex
            mAnimationQueue.add(::closeSet)
            mAnimationQueue.add(::clearSet)
            mAnimationQueue.add(::unlockSets)

            return SKIP_ANIMATION
        }

        return NORMAL_ANIMATION
    }

    //split all fractals on clear
    private fun clearSplit(): AnimationSpeed {
        val splitList = mutableListOf<Fractal>()
        for (f in mFractals) {
            if (f.mSize > 1) splitList.add(f)
        }

        if (splitList.isEmpty()) return SKIP_ANIMATION

        for (f in splitList) {
            val conditions = Array<FractalData>(f.mSize * f.mSize){FractalData(f.mIndex, f.mSize)}.also {
                for(i in 0 until (f.mSize * f.mSize)) {
                    it[i] = FractalData(intArrayOf(i % f.mSize, i / f.mSize), 1)
                }
            }
            val newFractals = split(f, conditions, FractalData(f.mIndex, f.mSize))
            for (newF in newFractals) {
                newF.moveTo(
                        calculateFractalPos(
                                newF.mIndex,
                                newF.mSize,
                                getOpenSquare()!!.pos,
                                getPuzzleDim(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex)
                        )
                )
            }
            mFractals.remove(f)
            mFractals.addAll(newFractals)
        }
        splitList.clear()

        return NORMAL_ANIMATION
    }


    //map the indices stored during DFS to the fractals in mFractals
    private fun clearPushPulseFractals(): AnimationSpeed {
        mPulseFractals.clear()
        while(mPulseIndices.isNotEmpty()) {
            val group = mPulseIndices.pop()
            val list: MutableList<Fractal> = mutableListOf()
            for(index in group) {
                list.add(getFractal(index)!!)
            }
            mPulseFractals.push(list)
        }
        return SKIP_ANIMATION
    }

    private fun clearShowClearedBoxes(): AnimationSpeed {
        for(f in mFractals) {
            if(f.mIsBlock) {
                f.mClearedBox!!.scalePulse(floatArrayOf(10f, 10f, 1f))
            }
        }
        return NORMAL_ANIMATION
    }


    private fun pulseFractals(): AnimationSpeed {
        val group = mPulseFractals.pop()
        for (f in group) {
            f.scalePulse(floatArrayOf(f.scale[0] * 2f, f.scale[1] * 2f, 1f))
            if(f.mIsBlock) {
                f.mShowClearedBox = true
                f.mClearedBox!!.scalePulse(floatArrayOf(10f, 10f, 1f))
            }
        }
        return NORMAL_ANIMATION
    }

    private fun resizeRequired(
            transformation: Transformation,
            index: IntArray,
            size: Int
    ): Boolean {
        val fractal = getFractal(index) ?: return true

        if (fractal.mSize != size) return true

        val swappedFractal: Fractal?

        when (transformation) {
            Transformation.TranslatePosX -> {
                swappedFractal =
                        getFractal(intArrayOf(fractal.mIndex[0] + fractal.mSize, fractal.mIndex[1]))
                                ?: return true
                if (swappedFractal.mSize != size) return true
            }
            Transformation.TranslateNegX -> {
                swappedFractal =
                        getFractal(intArrayOf(fractal.mIndex[0] - fractal.mSize, fractal.mIndex[1]))
                                ?: return true
                if (swappedFractal.mSize != size) return true
            }
            Transformation.TranslatePosY -> {
                swappedFractal =
                        getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] + fractal.mSize))
                                ?: return true
                if (swappedFractal.mSize != size) return true
            }
            Transformation.TranslateNegY -> {
                swappedFractal =
                        getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] - fractal.mSize))
                                ?: return true
                if (swappedFractal.mSize != size) return true
            }
        }

        return false
    }

    private fun undoResize(transformation: Transformation, index: IntArray, size: Int): AnimationSpeed {
        //get list of fractals on edge of given fractal conditions (2 for swap, 1 for other transformations)
        val conditions = when (transformation) {
            Transformation.TranslatePosX -> {
                arrayOf(
                        FractalData(index, size),
                        FractalData(intArrayOf(index[0] + size, index[1]), size)
                )
            }
            Transformation.TranslateNegX -> {
                arrayOf(
                        FractalData(index, size),
                        FractalData(intArrayOf(index[0] - size, index[1]), size)
                )
            }
            Transformation.TranslatePosY -> {
                arrayOf(
                        FractalData(index, size),
                        FractalData(intArrayOf(index[0], index[1] + size), size)
                )
            }
            Transformation.TranslateNegY -> {
                arrayOf(
                        FractalData(index, size),
                        FractalData(intArrayOf(index[0], index[1] - size), size)
                )
            }
            else -> {
                arrayOf(FractalData(index, size))
            }
        }

        val overlapList = mutableListOf<Fractal>()
        for (fractal in mFractals) {
            for (c in conditions) {
                if (onFractalEdge(fractal, c.index, c.size)) {
                    overlapList.add(fractal)
                    break //should only add it once even if it overlaps multiple target fractals
                }
            }
        }

        //call split on those (including passing in list of conditions: 2 conditions if transformation is swap)
        val splitList = mutableListOf<Fractal>()
        for (f in overlapList) {
            for (sf in split(f, conditions, FractalData(f.mIndex, f.mSize))) {
                mFractals.add(sf)
                splitList.add(sf)
            }
        }


        val insideListA = mutableListOf<Fractal>()
        val insideListB = mutableListOf<Fractal>()

        //call moveToMerge on the fractals completely inside conditions (2 for swap, 1 for other) to target
        //need to call it on all fractals, not just the recently split ones
        val puzzleDim = getPuzzleDim(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex)
        for (f in mFractals) {
            if (insideFractal(f, conditions[0].index, conditions[0].size)) {
                f.moveTo(
                        calculateFractalPosForTarget(
                                f.mIndex,
                                f.mSize,
                                conditions[0].index,
                                conditions[0].size,
                                getOpenSquare()!!.pos,
                                puzzleDim
                        )
                )
                insideListA.add(f)
            } else if (conditions.size == 2 && insideFractal(
                            f,
                            conditions[1].index,
                            conditions[1].size
                    )
            ) {
                f.moveTo(
                        calculateFractalPosForTarget(
                                f.mIndex,
                                f.mSize,
                                conditions[1].index,
                                conditions[1].size,
                                getOpenSquare()!!.pos,
                                puzzleDim
                        )
                )
                insideListB.add(f)
            } else {
                f.moveTo(calculateFractalPos(f.mIndex, f.mSize, getOpenSquare()!!.pos, puzzleDim))
            }
        }

        if (conditions.size == 1) {
            mMergeFractals = arrayOf(insideListA)
        } else {
            mMergeFractals = arrayOf(insideListA, insideListB)
        }

        return NORMAL_ANIMATION
    }

    private fun undoTransform(transformation: Transformation, index: IntArray, size: Int): AnimationSpeed {
        val fractal = getFractal(index)
        transform(fractal!!, transformation, true)
        mUndoButton.decrement()

        return NORMAL_ANIMATION
    }


    private fun swap(fractal0: Fractal, fractal1: Fractal) {
        //update data
        val elements0 = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                fractal0.mIndex,
                fractal0.mSize
        )
        val elements1 = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                fractal1.mIndex,
                fractal1.mSize
        )
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal0.mIndex, elements1)
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal1.mIndex, elements0)

        //animate
        fractal0.moveTo(fractal1.pos)
        fractal1.moveTo(fractal0.pos)
        fractal0.mIndex = fractal1.mIndex.also { fractal1.mIndex = fractal0.mIndex }
    }

    private fun rotateCW(fractal: Fractal) {
        val elements = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                fractal.mIndex,
                fractal.mSize
        )

        //rotate elements clockwise
        when (fractal.mSize) {
            2 -> {
                elements[0] = elements[2].also {
                    elements[2] = elements[3].also {
                        elements[3] = elements[1].also {
                            elements[1] = elements[0]
                        }
                    }
                }
            }
            4 -> {
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
        mRecreateFractals.add(fractal)
    }

    private fun rotateCCW(fractal: Fractal) {
        val elements = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                fractal.mIndex,
                fractal.mSize
        )

        //rotate elements ccw
        when (fractal.mSize) {
            2 -> {
                elements[0] = elements[1].also {
                    elements[1] = elements[3].also {
                        elements[3] = elements[2].also {
                            elements[2] = elements[0]
                        }
                    }
                }
            }
            4 -> {
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
        fractal.rotateTo(
                90f,
                floatArrayOf(0f, 0f, 1f)
        ) //should recreate on animation end to keep things simple
        mRecreateFractals.add(fractal)
    }

    private fun reflectX(fractal: Fractal, topPushed: Boolean) {
        //update data
        val elements = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                fractal.mIndex,
                fractal.mSize
        )
        val elemCopy = elements.copyOf()
        for (row in 0 until fractal.mSize) {
            for (col in 0 until fractal.mSize) {
                elements[col + row * fractal.mSize] =
                        elemCopy[col + (fractal.mSize - 1 - row) * fractal.mSize]
            }
        }
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, elements)

        if (topPushed) fractal.rotateTo(-180f, floatArrayOf(1f, 0f, 0f))
        else fractal.rotateTo(180f, floatArrayOf(1f, 0f, 0f))
        mRecreateFractals.add(fractal)
    }

    private fun reflectY(fractal: Fractal, leftPushed: Boolean) {
        //update data
        val elements = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                fractal.mIndex,
                fractal.mSize
        )
        val elemCopy = elements.copyOf()
        for (row in 0 until fractal.mSize) {
            for (col in 0 until fractal.mSize) {
                elements[col + row * fractal.mSize] =
                        elemCopy[(fractal.mSize - 1 - col) + row * fractal.mSize]
            }
        }
        setElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, elements)

        if (leftPushed) fractal.rotateTo(-180f, floatArrayOf(0f, 1f, 0f))
        else fractal.rotateTo(180f, floatArrayOf(0f, 1f, 0f))
        mRecreateFractals.add(fractal)
    }

    //returns true if there is partial overlap between fractal and target fractal
    //returns false if completely outside or completely inside target fractal
    private fun insideFractal(fractal: Fractal, targetIndex: IntArray, targetSize: Int): Boolean {
        if (fractal.mSize <= targetSize) {
            val inLeft = fractal.mIndex[0] >= targetIndex[0]
            val inRight = fractal.mIndex[0] + fractal.mSize <= targetIndex[0] + targetSize
            val inTop = fractal.mIndex[1] >= targetIndex[1]
            val inBottom = fractal.mIndex[1] + fractal.mSize <= targetIndex[1] + targetSize

            if (inLeft && inRight && inTop && inBottom) return true
        }
        return false
    }

    private fun onFractalEdge(fractal: Fractal, targetIndex: IntArray, targetSize: Int): Boolean {

        //check if completely outside target
        if (fractal.mIndex[0] >= targetIndex[0] + targetSize) return false
        if (targetIndex[0] >= fractal.mIndex[0] + fractal.mSize) return false
        if (fractal.mIndex[1] >= targetIndex[1] + targetSize) return false
        if (targetIndex[1] >= fractal.mIndex[1] + fractal.mSize) return false

        if (insideFractal(fractal, targetIndex, targetSize)) return false

        return true
    }

    private fun split(
            fractal: Fractal,
            conditions: Array<FractalData>?,
            ogFractal: FractalData
    ): MutableList<Fractal> {
        val squarePos = getOpenSquare()!!.pos

        val index = fractal.mIndex
        val newSize = fractal.mSize / 2

        val elements0 = getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, index, newSize)
        val elements1 = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                intArrayOf(index[0] + newSize, index[1]),
                newSize
        )
        val elements2 = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                intArrayOf(index[0], index[1] + newSize),
                newSize
        )
        val elements3 = getElements(
                getOpenSet()!!.mIndex,
                getOpenSquare()!!.mIndex,
                intArrayOf(index[0] + newSize, index[1] + newSize),
                newSize
        )

        val retList = mutableListOf<Fractal>()


        val puzzleDim = getPuzzleDim(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex)

        val corners = arrayOf(
                Fractal(
                        elements0, newSize, index, //top left
                        calculateFractalPosForTarget(
                                index,
                                newSize,
                                ogFractal.index,
                                ogFractal.size,
                                squarePos,
                                puzzleDim
                        )
                ),
                Fractal(
                        elements1, newSize, intArrayOf(index[0] + newSize, index[1]), //top right
                        calculateFractalPosForTarget(
                                intArrayOf(index[0] + newSize, index[1]),
                                newSize,
                                ogFractal.index,
                                ogFractal.size,
                                squarePos,
                                puzzleDim
                        )
                ),
                Fractal(
                        elements2, newSize, intArrayOf(index[0], index[1] + newSize), //bottom left
                        calculateFractalPosForTarget(
                                intArrayOf(index[0], index[1] + newSize),
                                newSize,
                                ogFractal.index,
                                ogFractal.size,
                                squarePos,
                                puzzleDim
                        )
                ),
                Fractal(
                        elements3,
                        newSize,
                        intArrayOf(index[0] + newSize, index[1] + newSize), //bottom righ
                        calculateFractalPosForTarget(
                                intArrayOf(index[0] + newSize, index[1] + newSize),
                                newSize,
                                ogFractal.index,
                                ogFractal.size,
                                squarePos,
                                puzzleDim
                        )
                )
        )

        mFractals.remove(fractal)

        var splitAgain: Boolean

        if (conditions != null) {
            for (splitF in corners) {
                splitAgain = false
                for (c in conditions) {
                    if (onFractalEdge(splitF, c.index, c.size)) {
                        splitAgain = true
                        retList.addAll(
                                split(
                                        splitF,
                                        arrayOf(FractalData(c.index, c.size)),
                                        ogFractal
                                )
                        )
                        break
                    }
                }
                if (!splitAgain) {
                    retList.add(splitF)
                }
            }
        } else {
            retList.addAll(corners)
        }

        return retList

    }

    private fun getFractalIndex(fractals: MutableList<Fractal>): IntArray {
        var topLeftIndex = intArrayOf(100, 100)
        for (c in fractals) {
            //update finding top left index
            if ((c.mIndex[0] + c.mIndex[1]) < (topLeftIndex[0] + topLeftIndex[1]))
                topLeftIndex = c.mIndex
        }
        return topLeftIndex
    }

    private fun getFractalSize(fractals: MutableList<Fractal>): Int {
        val topLeftIndex = getFractalIndex(fractals)
        var targetSize = 0
        for (c in fractals) {
            if (c.mIndex[0] - topLeftIndex[0] + c.mSize > targetSize)
                targetSize = c.mIndex[0] - topLeftIndex[0] + c.mSize
        }
        return targetSize
    }

    //assume all fractals fit perfectly into square target - client responsibility to check before call this
    private fun moveToMerge(fractals: MutableList<Fractal>): AnimationSpeed {

        val targetIndex = getFractalIndex(fractals)
        val targetSize = getFractalSize(fractals)
        val puzzleDim = getPuzzleDim(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex)

        for (c in fractals) {
            c.moveTo(
                    calculateFractalPosForTarget(
                            c.mIndex,
                            c.mSize,
                            targetIndex,
                            targetSize,
                            getOpenSquare()!!.pos,
                            puzzleDim
                    )
            )
        }

        return NORMAL_ANIMATION
    }

    private fun merge(fractals: MutableList<Fractal>): Fractal {

        val newSize = getFractalSize(fractals)
        val newIndex = getFractalIndex(fractals)
        val elements =
                getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, newIndex, newSize)
        val puzzleDim = getPuzzleDim(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex)

        return Fractal(
                elements,
                newSize,
                newIndex,
                calculateFractalPos(newIndex, newSize, getOpenSquare()!!.pos, puzzleDim)
        )
    }

    private fun getClosestFractal(x: Float, y: Float): Fractal {
        var closestFractal = mFractals[0]
        var fractalScreenCoords = closestFractal.getScreenCoords()
        var minDis = pointDistance(x, y, fractalScreenCoords[0], fractalScreenCoords[1])
        var thisDis: Float

        for (fractal in mFractals) {
            fractalScreenCoords = fractal.getScreenCoords()
            thisDis = pointDistance(x, y, fractalScreenCoords[0], fractalScreenCoords[1])
            if (thisDis < minDis) {
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
        val fractalCoords = floatArrayOf(
                (screenCoords[0] + screenCoords[2]) / 2f,
                (screenCoords[1] + screenCoords[3]) / 2f
        )

        if (x < fractalCoords[0]) {
            if (y < fractalCoords[1]) { //fractal is top right
                topLeft = getFractal(intArrayOf(index[0] - size, index[1]))
                topRight = closestFractal
                bottomLeft = getFractal(intArrayOf(index[0] - size, index[1] + size))
                bottomRight = getFractal(intArrayOf(index[0], index[1] + size))
            } else { //fractal is bottom right
                topLeft = getFractal(intArrayOf(index[0] - size, index[1] - size))
                topRight = getFractal(intArrayOf(index[0], index[1] - size))
                bottomLeft = getFractal(intArrayOf(index[0] - size, index[1]))
                bottomRight = closestFractal
            }
        } else {
            if (y < fractalCoords[1]) { //fractal is top left
                topLeft = closestFractal
                topRight = getFractal(intArrayOf(index[0] + size, index[1]))
                bottomLeft = getFractal(intArrayOf(index[0], index[1] + size))
                bottomRight = getFractal(intArrayOf(index[0] + size, index[1] + size))
            } else { //fractal is bottom left
                topLeft = getFractal(intArrayOf(index[0], index[1] - size))
                topRight = getFractal(intArrayOf(index[0] + size, index[1] - size))
                bottomLeft = closestFractal
                bottomRight = getFractal(intArrayOf(index[0] + size, index[1]))
            }
        }

        if (topLeft == null || topRight == null || bottomLeft == null || bottomRight == null)
            return null

        if (topLeft.mSize != size || topRight.mSize != size || bottomRight.mSize != size || bottomLeft.mSize != size) return null

        if (topLeft.mIsBlock || topRight.mIsBlock || bottomRight.mIsBlock || bottomLeft.mIsBlock) return null

        return arrayOf(topLeft, topRight, bottomLeft, bottomRight)

    }

    private fun dispatchCommand(touchType: TouchType, x: Float, y: Float): AnimationSpeed {
        when (getScreenState()) {
            Screen.Logo -> {
                if (touchType == TouchType.Tap && mLogo.pointCollision(x, y)) {
                    return openGame()
                }
            }
            Screen.Set -> {
                if (touchType == TouchType.Tap) {
                    for (set in mSets) {
                        if (set.pointCollision(x, y) && !appData.setData[set.mIndex]!!.isLocked) {
                            return openSet(set)
                        }
                    }
                }

                if (touchType == TouchType.Back) {
                    return closeGame()
                }
            }
            Screen.Square -> {
                if (touchType == TouchType.Tap) {
                    for (square in mSquares) {
                        if (square.centerCollision(x, y) && !appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[square.mIndex]!!.isLocked) {
                            return openSquare(square)
                        }
                    }
                }

                if (touchType == TouchType.Back && getOpenSet() != null) {
                    return closeSet()
                }
            }
            Screen.Fractal -> {
                if (getTransformationsRemaining(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex) > 0) {
                    for (fractal in mFractals) {
                        when (touchType) {
                            TouchType.FlickLeft -> {
                                if (fractal.topCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                    return transform(fractal, Transformation.RotateCCW, false)
                                } else if (fractal.bottomCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                    return transform(fractal, Transformation.RotateCW, false)
                                } else if (fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                    val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] - fractal.mSize, fractal.mIndex[1]))
                                    if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
                                        return transform(fractal, Transformation.TranslateNegX, false)
                                    }
                                }
                            }
                            TouchType.FlickRight -> {
                                if (fractal.topCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                    return transform(fractal, Transformation.RotateCW, false)
                                } else if (fractal.bottomCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                    return transform(fractal, Transformation.RotateCCW, false)
                                } else if (fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                    val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0] + fractal.mSize, fractal.mIndex[1]))
                                    if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
                                        return transform(fractal, Transformation.TranslatePosX, false)
                                    }
                                }
                            }
                            TouchType.FlickUp -> {
                                if (fractal.leftCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                    return transform(fractal, Transformation.RotateCW, false)
                                } else if (fractal.rightCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                    return transform(fractal, Transformation.RotateCCW, false)
                                } else if (fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                    val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] - fractal.mSize))
                                    if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
                                        return transform(fractal, Transformation.TranslateNegY, false)
                                    }
                                }
                            }
                            TouchType.FlickDown -> {
                                if (fractal.leftCollision(x, y) && fractal.mSize > 1) { //rotate ccw
                                    return transform(fractal, Transformation.RotateCCW, false)
                                } else if (fractal.rightCollision(x, y) && fractal.mSize > 1) { //rotate cw
                                    return transform(fractal, Transformation.RotateCW, false)
                                } else if (fractal.centerCollision(x, y) && !fractal.mIsBlock) {
                                    val swappedFractal: Fractal? = getFractal(intArrayOf(fractal.mIndex[0], fractal.mIndex[1] + fractal.mSize))
                                    if (swappedFractal != null && swappedFractal.mSize == fractal.mSize && !swappedFractal.mIsBlock) {
                                        return transform(fractal, Transformation.TranslatePosY, false)
                                    }
                                }
                            }
                            TouchType.Tap -> { //a block of size > 1 should never exists, so won't check for it
                                if (fractal.leftCollision(x, y) && fractal.mSize > 1) {
                                    return transform(fractal, Transformation.ReflectYLeft, false)
                                } else if (fractal.rightCollision(x, y) && fractal.mSize > 1) {
                                    return transform(fractal, Transformation.ReflectYRight, false)
                                } else if (fractal.topCollision(x, y) && fractal.mSize > 1) {
                                    return transform(fractal, Transformation.ReflectXTop, false)
                                } else if (fractal.bottomCollision(x, y) && fractal.mSize > 1) {
                                    return transform(fractal, Transformation.ReflectXBottom, false)
                                }
                            }
                        }
                    }
                }


                for (fractal in mFractals) {
                    if (touchType == TouchType.DoubleTap && fractal.pointCollision(x, y) && fractal.mSize > 1) {
                        val newFractals = split(fractal, null, FractalData(fractal.mIndex, fractal.mSize))
                        val puzzleDim = getPuzzleDim(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex)
                        for (newFractal in newFractals) {
                            newFractal.moveTo(calculateFractalPos(newFractal.mIndex, newFractal.mSize, getOpenSquare()!!.pos, puzzleDim))
                            mFractals.add(newFractal)
                        }
                        return NORMAL_ANIMATION
                    }
                }


                if (touchType == TouchType.DoubleTap) {
                    val cornerFractals = getCornerFractals(x, y) //checks for blocks in this function and returns null if ANY fractal is a block
                    if (cornerFractals != null) {
                        mMergeFractals = arrayOf(cornerFractals.toMutableList())
                        return moveToMerge(cornerFractals.toMutableList())
                    }
                }

                if (touchType == TouchType.Back) {
                    val openSquare = getOpenSquare()
                    if (openSquare != null)
                        return closeSquare()
                }

                if (touchType == TouchType.FlickLeft && mUndoButton.centerCollision(x, y)) {
                    //if undo stack is not empty
                    if (!appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[getOpenSquare()!!.mIndex]!!.undoStack.empty()) {
                        val undoData = appData.setData[getOpenSet()!!.mIndex]!!.puzzleData[getOpenSquare()!!.mIndex]!!.undoStack.pop()
                        if (resizeRequired(undoData.transformation, undoData.index, undoData.size)) {
                            mUndoQueue.add(undoData)
                            return undoResize(undoData.transformation, undoData.index, undoData.size)
                        } else {
                            return undoTransform(undoData.transformation, undoData.index, undoData.size)
                        }
                    }
                }
            }
        }

        return SKIP_ANIMATION
    }


    private fun onAnimationEnd() {
        for (set in mSets) {
            set.onAnimationEnd()
        }
        for (square in mSquares) {
            square.onAnimationEnd()
        }
        for (fractal in mFractals) {
            fractal.onAnimationEnd()
        }
        mCamera.onAnimationEnd()
        mUndoButton.onAnimationEnd()
        mLogo.onAnimationEnd()


        //this stuff shouldn't be here - fire and forget commands will simplify everything
        //so destroy cube on tap, creating squares and then animating them
        //on closing a cube, destroy all squares and create an open cube and animate it closing
        ///////////////////////////////////////////////////////////////////////////////////////

        if (mMergeFractals != null) {
            for (mergeGroup in mMergeFractals!!) {
                mFractals.add(merge(mergeGroup))
            }
            for (mergeGroup in mMergeFractals!!) {
                for (fractal in mergeGroup) {
                    mFractals.remove(fractal)
                }
            }
            mMergeFractals = null
        }

        //recreate at end of animation (to reduce complexity by not having to save angle state, mostly)
        /*
        if (mRecreateFractals.isNotEmpty()) {
            mFractals.add(Fractal(getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, mRecreateFractal!!.mIndex, mRecreateFractal!!.mSize),
                            mRecreateFractal!!.mSize, mRecreateFractal!!.mIndex, mRecreateFractal!!.pos))
            mFractals.remove(mRecreateFractal!!)
            mRecreateFractal = null
        }*/

        for(fractal in mRecreateFractals) {
            mFractals.add(Fractal(getElements(getOpenSet()!!.mIndex, getOpenSquare()!!.mIndex, fractal.mIndex, fractal.mSize),
                    fractal.mSize, fractal.mIndex, fractal.pos))
            mFractals.remove(fractal)
        }
        mRecreateFractals.clear()

        writeSaveData()
        //////////////////////////////////////////////////////////////////////////////////////////
    }

    override fun onDrawFrame(unused: GL10) {

        ////////////////////////////update setup///////////////////////////////////////////
        //fix for large delta time when app starts
        if (mFirstDraw) {
            mPreviousTime = SystemClock.uptimeMillis()
            mCurrentTime = SystemClock.uptimeMillis()
            mFirstDraw = false
        }

        //delta time
        mPreviousTime = mCurrentTime
        mCurrentTime = SystemClock.uptimeMillis()
        val deltaTime = ((mCurrentTime - mPreviousTime).toInt() / 382f) * mAnimationSpeed //382 is to make animations go at 157 bpm to match music


        //only want to call onAnimationEnd() once per animation
        if (mAnimationParameter < 1f) {
            mAnimationParameter += deltaTime
            if (mAnimationParameter >= 1f) {
                onAnimationEnd()
            }
        }

        if (mAnimationParameter >= 1f) {
            if(!mPulseFractals.isEmpty()) {
                val result = pulseFractals()
                startAnimation(result)
            }else if(!mAnimationQueue.isEmpty()) {
                val result: AnimationSpeed = mAnimationQueue.removeFirst()()
                startAnimation(result)
            }else if(!mUndoQueue.isEmpty()) {
                val undoData = mUndoQueue.removeFirst()
                val result: AnimationSpeed = undoTransform(undoData.transformation, undoData.index, undoData.size)
                startAnimation(result)
            }else{
                var speed: AnimationSpeed = SKIP_ANIMATION
                while (!mInputQueue.isEmpty() && speed > 100f) { //loops until valid command is found or no more inputs
                    val data = mInputQueue.getNextInput()
                    val pair = screenToNormalizedCoords(data.x, data.y)
                    speed = dispatchCommand(data.touchType, pair.x, pair.y)
                }
                startAnimation(speed)
            }
        }



        ////////////////////////////////////////////update////////////////////////////////
        val sigmoid = sigmoid(mAnimationParameter)

        mInputQueue.onUpdate(deltaTime)

        mCamera.onUpdate(sigmoid)

        for (fractal in mFractals) {
            fractal.onUpdate(sigmoid)
        }

        for (square in mSquares) {
            square.onUpdate(sigmoid)
        }

        for (set in mSets) {
            set.onUpdate(sigmoid)
        }

        mLogo.onUpdate(sigmoid)

        mUndoButton.onUpdate(sigmoid)

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


        mUndoButton.draw(mVPMatrix)

        mLogo.draw(mVPMatrix)

        for (fractal in mFractals) {
            fractal.draw(mVPMatrix)
        }

        for (square in mSquares) {
            square.draw(mVPMatrix)
        }
        for(set in mSets)
        {
            set.draw(mVPMatrix)
        }


        //////////////////////draw cleanup/////////////////////////////
        GLES20.glDisableVertexAttribArray(mPosAttrib)
        GLES20.glDisableVertexAttribArray(mTexCoordAttrib)

    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height

        mScreenWidth = width
        mScreenHeight = height

        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f) //allow depth up to 100f away from camera
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 103f) //allow depth up to 100 units away
    }

    //normalize to -1 to 1 in both dimensions
    private fun screenToNormalizedCoords(screenX: Float, screenY: Float): CoordinatePair {
        return CoordinatePair(screenX * 2 / mScreenWidth - 1, -(screenY * 2 / mScreenHeight - 1))
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

    private fun elementsToString(elements: Array<F>): String {
        var string = ""
        for(e in elements) {
            when(e) {
                F.R -> string += "r"
                F.RB -> string += "R"
                F.G -> string += "g"
                F.GB -> string += "G"
                F.B -> string += "b"
                F.BB -> string += "B"
                F.N -> string += "n"
                F.NB -> string += "N"
                F.E -> string += "e"
            }
        }
        return string
    }

    private fun stringToElements(string: String?): Array<F> {
        if(string == null) {
            return Array(MAX_PUZZLE_WIDTH * MAX_PUZZLE_HEIGHT){F.N}
        }
        val elements = Array(string.length){F.N}
        for(i in string.indices) {
            elements[i] = when(string[i]) {
                'r' -> F.R
                'R' -> F.RB
                'g' -> F.G
                'G' -> F.GB
                'b' -> F.B
                'B' -> F.BB
                'n' -> F.N
                'N' -> F.NB
                'e' -> F.E
                else -> F.N
            }
        }
        return elements
    }

    //optimization: setting 'dataChanged' flags for each puzzle/set, and only write puzzle data for those that changes between previous and current write
    private fun writeSaveData() {
        val sharedPref = (mContext as AppCompatActivity).getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            for(i in appData.setData.indices) {
                if(appData.setData[i] == null) continue
                for (j in appData.setData[i]!!.puzzleData.indices) {
                    if(appData.setData[i]!!.puzzleData[j] == null) continue

                    putBoolean(i.toString() + j.toString() + "isCleared", appData.setData[i]!!.puzzleData[j]!!.isCleared)
                    putString(i.toString() + j.toString() + "elements", elementsToString(appData.setData[i]!!.puzzleData[j]!!.elements))

                    //copying into temp stack
                    val tempStack = Stack<UndoData>()
                    while (!appData.setData[i]!!.puzzleData[j]!!.undoStack.isEmpty()) {
                        tempStack.push(appData.setData[i]!!.puzzleData[j]!!.undoStack.pop())
                    }
                    //writing stack to preferences and pushing them back into undostack
                    for (k in 0 until appData.setData[i]!!.puzzleData[j]!!.maxTransformations) {
                        if (!tempStack.isEmpty()) {
                            val undoData = tempStack.pop()
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "transformation", undoData.transformation.value)
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "col", undoData.index[0])
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "row", undoData.index[1])
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "size", undoData.size)
                            //push back into appdata
                            appData.setData[i]!!.puzzleData[j]!!.undoStack.push(undoData)
                        } else {
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "transformation", Transformation.None.value)
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "col", 0)
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "row", 0)
                            putInt(i.toString() + j.toString() + "undo" + k.toString() + "size", 0)
                        }
                    }
                }
            }
            apply()
        }
    }


    private fun readSaveData() {
        val sharedPref = (mContext as AppCompatActivity).getPreferences(Context.MODE_PRIVATE)
        for(i in appData.setData.indices) {
            if(appData.setData[i] == null) continue
            for(j in appData.setData[i]!!.puzzleData.indices) {
                if(appData.setData[i]!!.puzzleData[j] == null) continue
                appData.setData[i]!!.puzzleData[j]!!.isCleared = sharedPref.getBoolean(i.toString() + j.toString() + "isCleared",
                        defaultAppData.setData[i]!!.puzzleData[j]!!.isCleared)
                appData.setData[i]!!.puzzleData[j]!!.elements = stringToElements(sharedPref.getString(i.toString() + j.toString() + "elements",
                        elementsToString(defaultAppData.setData[i]!!.puzzleData[j]!!.elements)))

                //read undo stack
                appData.setData[i]!!.puzzleData[j]!!.undoStack.clear()
                for(k in 0 until appData.setData[i]!!.puzzleData[j]!!.maxTransformations) {
                    val transformation = Transformation.getByValue(sharedPref.getInt(i.toString() + j.toString() + "undo" + k.toString() + "transformation", 0))
                    val col = sharedPref.getInt(i.toString() + j.toString() + "undo" + k.toString() + "col", 0)
                    val row = sharedPref.getInt(i.toString() + j.toString() + "undo" + k.toString() + "row", 0)
                    val size = sharedPref.getInt(i.toString() + j.toString() + "undo" + k.toString() + "size", 0)

                    //just check transformation since col row and size should be saved correctly if transformation is too
                    if(transformation == null || transformation == Transformation.None) break

                    appData.setData[i]!!.puzzleData[j]!!.undoStack.push(UndoData(transformation, intArrayOf(col, row), size))
                }

            }
        }

        //if puzzle is cleared, unlock all adjacent puzzles (shouldn't matter if they're already unlocked or not)
        //if all puzzles in a set cleared, set set.isCleared to true
        var allPuzzlesCleared: Boolean
        for(setIndex in appData.setData.indices) {
            allPuzzlesCleared = true
            if(appData.setData[setIndex] == null) continue
            for (puzzleIndex in appData.setData[setIndex]!!.puzzleData.indices) {
                if(appData.setData[setIndex]!!.puzzleData[puzzleIndex] == null) continue

                if (appData.setData[setIndex]!!.puzzleData[puzzleIndex]!!.isCleared) {
                    unlockAdjacentPuzzles(setIndex, puzzleIndex)
                } else {
                    allPuzzlesCleared = false
                }
            }
            if(allPuzzlesCleared) {
                appData.setData[setIndex]!!.isCleared = true
            }
        }

        //loop through all sets.  If cleared, set adjacent sets to unlocked
        for(setIndex in appData.setData.indices) {
            if(appData.setData[setIndex] == null) continue

            if(appData.setData[setIndex]!!.isCleared) {
                unlockAdjacentSets(setIndex)
            }
        }
    }

    private fun resetSaveData() {
        appData = defaultAppData.copy()
    }


}