//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //abstract all code in fractal into shared class (make it a super class of Fractal, Square and Cube)
        //need to do this bc we want to destroy the cube on expansion, and create squares in the correct place (same with Squares and Fractals)
        //also, cubes, squares and fractals respond to different inputs, so we'll need different onTouch/etc functions
        //this will simplify transformations too (since we also want to recreate most things after an animation is over)

    //animations of cubes, squares and fractals for transition between screens
        //first, simple set onTouch for opening puzzles, and onHold for closing puzzles (and zooming out) - can do the pinch in pinch out later
        //have cube oriented correctly (with square panes 2 and 3 centered) as default.  Can add animation to orient itself later
        //have cubes unfold into 6 squares surfaces
            //once unfolded, destroy cube and replace with 6 squares.  Then animate squares animating to final positions (slightly separated from other squares)
        //have squares separate into <= 16 fractals.  OnOpen, destroy square and replace with fractals.  Animate fractals to final position
        //merging them goes backwards - animate together.  OnAnimationEnd, destroy and replace with formed object
        //idea: pinching with normal distance is for forming fractals, but a large (~ > 75% of screen height) is a go back to previous screen pinch

    //each element type can be associated with an array of texture coordinates
    //get Fractals, Faces, Cubes displayed and user able to transition between the three stages.  Get 8 cubes forming into Dali Cross/tesseract

    //3 transformations with forming and splitting (all the coordinate values can be hard coded - only about 25 with a 4x4 with fractal sizes 1, 2 4)
    //all animations transitioning from 3d cube to unwrapped 6 square faces, to splitting to (up to) 16 fractals
    //GUI: go back button (going down into the fractal involves tapping a square face???)
        //could i use pinch in / pinch out to zoom in and out on fractals????

    //implement asserts in my own way (the debug enabled one is too wordy)

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