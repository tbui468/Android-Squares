//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //destroy an opened cube when opening animation is done, and create 6 squares in the correct place

    //player can then click on squares to open individual puzzles (the three animations happen concurrently)
        //camera zooms in further to center on 4x4 fractals
        //square is destroyed and up to 16 fractals are created in its place and animated to fixed margin
        //all other squares increase their margin so that they aren't visible

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