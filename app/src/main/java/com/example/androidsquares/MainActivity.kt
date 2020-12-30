//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //problem with depth testing and transparent textures
        //bc of depth testing, if opaque quads are drawn behind a transparent quad, it won't pass the depth test
        //if it's drawn before, then it will be drawn along with the transparent texture later

        //options:
            //need to order quads by z depth or draw opaque quads before all transparent ones - dont' like this idea
            //or instead of having transparent textures, just don't have a quad there - this idea is better
                //remove the vertex (5 floats) and the 6 indices associated with it
                //could easily make a function to do this
                //code this along with setting textures based on input elements string (or array)

    //animations of cubes, squares and fractals for transition between screens
        //have cubes unfold into 6 squares surfaces
        //have squares separate into 1x1 fractals
        //merging them goes backwards

    //each element type can be associated with an array of texture coordinates
    //get Fractals, Faces, Cubes displayed and user able to transition between the three stages.  Get 8 cubes forming into Dali Cross/tesseract

    //3 transformations with forming and splitting (all the coordinate values can be hard coded - only about 25 with a 4x4 with fractal sizes 1, 2 4)
    //all animations transitioning from 3d cube to unwrapped 6 square faces, to splitting to (up to) 16 fractals
    //GUI: go back button (going down into the fractal involves tapping a square face???)
        //could i use pinch in / pinch out to zoom in and out on fractals????

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