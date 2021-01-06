//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //problem now: onDown is registered everytime on flick is too
    //use ScaleGestureDetector to detect pinch in /out
        //might just be easier to get raw inputs (detecting ACTION_DOWN, ACTION_UP, etc) and process them myself
        //look at github for my origin detection code

    //allow users to swap fractals
    //allow users to form fractals
    //allow users to split fractals
    //allow users to rotate/reflect fractals

    //add more flexability to cube unfolding to allow 8 different unfolded patterns

    //bug with tapping a cube before animation is done
    //one option is to disable commands until current command/onAnimationEnd is complete - related to commandQueue (see below)

    //if any puzzle dimensions are odd in either axis, then when zooming in on fractals (and splitting them), center it
        //better yet, just have the camera zoom in on the correct location such that the odd dimension(s) are centered

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