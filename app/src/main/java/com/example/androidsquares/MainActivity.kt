//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)
    //simple colored faces with curved edges for textures (start here and then work my way down into the details)
        //make a 4 color png (make each fractal a 32x32 sprite) - try different texture options to see how smooth it looks
            //make the fractals textured (just a random color is fine for now)
            //get squares, composed of fractals, drawing by calling the fractals it's composed of - rotate them so I can see front/back sides too
            //get cubes, composed of squares, trickle down the draw call to fractals - rotate them so I can see that it's actually a cube
        //use glFrustum (since perspective projection will be needed for finals levels)
        //get Fractals, Faces, Cubes displayed and user able to transition between the three stages.  Get 8 cubes forming into Dahl Cross/tesseract

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