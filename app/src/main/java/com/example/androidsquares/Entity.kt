//entities contain vertices/indices/texture for drawing and functions for user input events
//each entity needs a buffer of vertices and indices
    //Fractal is composed of 1 - 16 quads
    //Square is composed of 16 quads (some may be insivisible)
    //Cube is composed of 6 squares

    //Do Squares and Cubes need drawables?  Just make them composition of Fractals/Squares

package com.example.androidsquares

interface Drawable {
    fun draw(vpMatrix: FloatArray)
}

open class Entity {
    var pos = floatArrayOf(0f, 0f, 0f)
    private var fromPos = floatArrayOf(0f, 0f, 0f)
    private var toPos = floatArrayOf(0f, 0f, 0f)

    var scale = floatArrayOf(1f, 1f, 1f)
    private var fromScale = floatArrayOf(1f, 1f, 1f)
    private var toScale = floatArrayOf(1f, 1f, 1f)

    var angle = floatArrayOf(0f, 0f, 0f)
    private var fromAngle = floatArrayOf(0f, 0f, 0f)
    private var toAngle = floatArrayOf(0f, 0f, 0f)

    fun moveTo(newPos: FloatArray) {
    }
    fun scaleTo(newScale: FloatArray) {
    }
    fun rotateTo(newAngle: FloatArray) {
    }
}