package com.example.androidsquares

class UndoButton(private val mMaxTransformations: Int, private var mTransformationCount: Int, pos: FloatArray) : Entity(pos, floatArrayOf(40f, 5.5f, .1f), 1) {

    private val mSpacing = 4f
    private val mOffset = (mMaxTransformations - 1) * mSpacing / 2f


    private var mDarkBoxes = Array<Box?>(mMaxTransformations){null}.also {
        for(i in it.indices) {
            it[i] = Box(floatArrayOf(pos[0] - mOffset + mSpacing * i, pos[1], pos[2]), F.NB)
        }
    }

    private var mLightBoxes = Array<Box?>(mMaxTransformations){null}.also {
        for(i in it.indices) {
            it[i] = Box(floatArrayOf(pos[0] - mOffset - mSpacing, pos[1], pos[2]), F.N)
        }
    }

    init {
        //set all boxes up to count to their positions
        //set remainder to final position
        for(i in mLightBoxes.indices) {
            if(i < mTransformationCount) {
                mLightBoxes[i]!!.setPosData(floatArrayOf(pos[0] - mOffset + (i) * mSpacing, pos[1], pos[2]))
            }else {
                mLightBoxes[i]!!.setPosData(floatArrayOf(pos[0] - mOffset + (mTransformationCount - 1) * mSpacing, pos[1], pos[2]))
            }
            if(mTransformationCount == 0) {
                mLightBoxes[i]!!.setScaleData(floatArrayOf(0f, 3f, 3f))
            }
        }
    }

    override fun fadeTo(newAlpha: Float) {
        super.fadeTo(newAlpha)
        for(box in mDarkBoxes) {
            box!!.fadeTo(newAlpha)
        }
        for(box in mLightBoxes) {
            box!!.fadeTo(newAlpha)
        }
    }

    override fun moveTo(newPos: FloatArray) {
        super.moveTo(newPos)
        val deltaX = newPos[0] - pos[0]
        val deltaY = newPos[1] - pos[1]
        val deltaZ = newPos[2] - pos[2]
        for(box in mDarkBoxes) {
            box!!.moveTo(floatArrayOf(box.pos[0] + deltaX, box.pos[1] + deltaY, box.pos[2] + deltaZ))
        }
        for(box in mLightBoxes) {
            box!!.moveTo(floatArrayOf(box.pos[0] + deltaX, box.pos[1] + deltaY, box.pos[2] + deltaZ))
        }
    }


    fun decrement() {
        for(i in mLightBoxes.indices) {
            if(i < mTransformationCount - 1) continue
            mLightBoxes[i]!!.moveTo(floatArrayOf(pos[0] - mOffset + (mTransformationCount - 2) * mSpacing, pos[1], pos[2]))
            if(mTransformationCount == 1) {
                mLightBoxes[i]!!.scaleTo(floatArrayOf(0f, 3f, 3f))
            }
        }

        mTransformationCount -= 1
    }

    fun increment() {
        for(i in mLightBoxes.indices) {
            if(i < mTransformationCount) continue
            mLightBoxes[i]!!.moveTo(floatArrayOf(pos[0] - mOffset + (mTransformationCount) * mSpacing, pos[1], pos[2]))
            if(mTransformationCount == 0) {
                mLightBoxes[i]!!.scaleTo(floatArrayOf(3f, 3f, 3f))
            }
        }

        mTransformationCount += 1
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        for(box in mDarkBoxes) {
            box!!.onAnimationEnd()
        }
        for(box in mLightBoxes) {
            box!!.onAnimationEnd()
        }
    }

    override fun onUpdate(t: Float) {
        super.onUpdate(t)
        for(box in mDarkBoxes) {
            box!!.onUpdate(t)
        }
        for(box in mLightBoxes) {
            box!!.onUpdate(t)
        }
    }


    fun draw(vpMatrix: FloatArray) {

        for(box in mDarkBoxes) {
            box!!.draw(vpMatrix)
        }

        for (box in mLightBoxes) {
            box!!.draw(vpMatrix)
        }

    }
}