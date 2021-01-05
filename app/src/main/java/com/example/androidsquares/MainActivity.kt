//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //InputQueue is there, but the order of dispatched commands/animationParameter are all messed up
        //clean up the stuff from before (all those flags) and simplify
        //dispatchCommand should only call a handful of functions in the renderer, and animationTimers should really only change if those calls are successful

    //allow users to swap fractals
    //allow users to form fractals
    //allow users to split fractals
    //allow users to rotate/reflect fractals

    //add more flexability to cube unfolding to allow 8 different unfolded patterns

    //bug with tapping a cube before animation is done
    //one option is to disable commands until current command/onAnimationEnd is complete - related to commandQueue (see below)

package com.example.androidsquares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity: AppCompatActivity() {
    private lateinit var mSquaresSurfaceView: SquaresSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSquaresSurfaceView = SquaresSurfaceView(this)
        setContentView(mSquaresSurfaceView)
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }
}