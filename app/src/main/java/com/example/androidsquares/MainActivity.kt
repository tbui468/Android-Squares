//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //crashes when user taps (starts new event) when a previous animation is still happening

    //one easy change to simplify - make scale 1f and then set object size of everything to .25 of currently value
        //then I can avoid using scale everywehre

    //problem: spawnSquare (the class function) is using the Bottom surface coordinates
    //should hard code relative locations of squares (relative to the cube center) so that fractals know center of square

    //fix (implement, really) transition from fractals back to Squares (currently it just goes straight back to cube screen)
        //have fractals animate back to spacing == .25f and then destroy them/recreate square in OnAnimationEnd()

    //could set the alpha of the other objects (squares and cubes) to gradually fade to 0 based on zoom level to keep it clean
            //only allow alpha of currently open/active square or cube to be fully at 1
            //then disable collisions with Squares/Cubes when looking at Fractals, and disabling Cubes when looking at Squares

    //bug with tapping a cube before animation is done
    //one option is to disable commands until current command/onAnimationEnd is complete - related to commandQueue (see below)

    //add a command queue for the SurfaceView to use instead of calling the Renderer functions directly (this is a source of concurrency problems)
    //have the surfaceview queue commands and then Renderer can dispatch them at the beginning of onDrawFrame(..)

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