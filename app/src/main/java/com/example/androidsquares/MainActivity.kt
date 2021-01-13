//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //clear a puzzle
        //implement unlocking sets when all puzzles in a set are cleared

    //play a simple animation on clear puzzle

    //transformation limit and limit icon display
        //it can be similar to how the BackButton was implemented

    //undo option for puzzles - need to save this somewhere too

    //design some puzzles with more interesting shapes and obvious ends goals

    //puzzle goal should be obvious when looking at a puzzle for the first time
        //user should know exactly where the colored squares need to go to connect all the colors
        //having a clear end goal will make the puzzle for interesting, and encourage users to start thinking about solutions

    //problem - there isn't enough integration with current puzzles -
        //it feels like a cluster of random colors (it should feel like I'm connection colors together into lines/trees
        //could add more darks colors
        //keep in mind that all the colors needs to connect on the surface of the cube

    //puzzle clearing conditions (connecting all colors will trigger a clear condition)
        //need non-moveable fractals (either within the 4x4 grid or hanging off the sides)

        //IDEA: create the first 8 puzzles - for now stick to having ALL fractals inside the 4x4 grid, but stationary colored one

    //currently square index is hacked in (using lateinit)
            //will need to add index to Square constructor so that different cube nets can be used (check notebook for the 8 nets chosen)

    //do the same thing with cubes??????
        //will require transformation of cubes - how will I do this if the cubes are rotating???

    //load default data from Data.kt
    //save data to preferences (since this is so much easier than using a database)
        //save puzzle data as single string per puzzle.  Can parse on load to get fractal types

    //order of drawing is out of order since call objects are at 0f in the z-axis
    //one option is to move active cube/square closer to camera (move in the positive z direction) so that there isn't any z fighting/undefined depth problems

    //add more flexibility to cube unfolding to allow 8 different unfolded patterns - look at notes in notebook

    //if any puzzle dimensions are odd in either axis, then when zooming in on fractals (and splitting them), center it
        //better yet, just have the camera zoom in on the correct location such that the odd dimension(s) are centered

    //draw lines between four outer corners of Square so that user has visual queue about where to touch/drag

    //allows swapping with difference sizes, with the condition being that IF the swapped fractal were the correct size the swap would be valid

    //have all empty fractals crashes everything bc my removal of vertices belonging to empty fractals was messy

    //idea: have a shader that changes color theme inside each complete cube

    //holding finger down highlights what part of the cube you are touch (center, edge or corner) so that user learns where to touch to transform

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