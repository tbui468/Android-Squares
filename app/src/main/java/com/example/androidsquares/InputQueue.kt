package com.example.androidsquares
import android.util.Log

class InputQueue {
    var mInputData = mutableListOf<InputData>()

    fun add(data: InputData) {
        mInputData.add(data)
        Log.d("touch", data.touchType.toString())
    }

    fun isEmpty(): Boolean {
        return mInputData.size == 0
    }

    fun getNextInput(): InputData {
        val nextInput = mInputData[0]
        mInputData.remove(nextInput)
        return nextInput
    }

    fun onUpdate(deltaTime: Float) {
        for(data in mInputData) {
            data.life -= deltaTime
        }
        mInputData.removeAll {
            it.life <= 0f
        }
    }

}