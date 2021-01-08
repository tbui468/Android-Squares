//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //allow users to form fractals - pinch in
    //allow users to split fractals - pinch out
    //allow users to rotate/reflect fractals

    //load default data from Data.kt
    //save data to preferences (since this is so much easier than using a database)

    //order of drawing is out of order since call objects are at 0f in the z-axis
    //one option is to move active cube/square closer to camera (move in the positive z direction) so that there isn't any z fighting/undefined depth problems

    //add more flexibility to cube unfolding to allow 8 different unfolded patterns - look at notes in notebook

    //if any puzzle dimensions are odd in either axis, then when zooming in on fractals (and splitting them), center it
        //better yet, just have the camera zoom in on the correct location such that the odd dimension(s) are centered

    //have all empty fractals crashes everything bc my removal of vertices belonging to empty fractals was messy

    //idea: have a shader that changes color theme inside each complete cube

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