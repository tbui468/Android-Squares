package com.example.androidsquares

import android.content.Context

import android.opengl.GLSurfaceView

class SquaresSurfaceView(context: Context): GLSurfaceView(context) {
    private val renderer: SquaresRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = SquaresRenderer()
        setRenderer(renderer)
    }
}