//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //complete function in cube::rotateSurfaceTo(...)
        //rotation should occur from the four wings first, and work their way inwards
        //for now, each axis of rotation is set based on closed cube

    //make a function to unfold cube
        //need to pass shader 6 different model matrices (the model matrices can be multiplied cpu side and then the vp matrix multiplied on gpu side)
        //cube needs to track angle of each face

        //or make a cube shader that takes in array of model matrices (6 total that are pre-multiplied on cpu side)
        //the shader can take in 3 attributes: vertex positions, texture coords and model matrix index

        //or compose a cube from 6 different squares (this might make more sense).  It would also simplify the process of animating, since I wouldn't need to destroy it
        //and I could use the same shader, just making the draw call 6 times per cube

        //essentially, create a function that takes in square number (1-6), square edge for rotation, and angle of rotation
            //square number is always the same
            //square edge will be set (as long as the order of rotations is set, eg going from outer wings to inner two/cube)
                //need to send three different model matrices to shader (rotation of four wings, rotation of inner two squares + four wings, and entire cube)
            //angle of rotation is set by client

        //animate using transformations going from edge square all the way to all squares (cube)
        //animate and test the edges 'wings' first (the four outer squares)
        //animate the inner two squares (along with the outer four)
        //animate entire cube along with the 6 above

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