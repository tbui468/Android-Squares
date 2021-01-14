//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //puzzles - get 2 sets done

    //puzzles layout
        //have puzzles center on screen regardless of dimensions
        //current fractal coord functions (both normal and for target size) assume a size of 4x6
        //center them based on runtime-calculated width and height of each puzzle

    //save system

    //animation for clearing puzzles

    //design some puzzles with more interesting shapes and obvious ends goals

    //puzzle goal should be obvious when looking at a puzzle for the first time
        //user should know exactly where the colored squares need to go to connect all the colors
        //having a clear end goal will make the puzzle for interesting, and encourage users to start thinking about solutions

    //problem - there isn't enough integration with current puzzles -
        //it feels like a cluster of random colors (it should feel like I'm connection colors together into lines/trees
        //could add more darks colors
        //keep in mind that all the colors needs to connect on the surface of the cube

    //load default data from Data.kt
    //save data to preferences (since this is so much easier than using a database)
        //save puzzle data as single string per puzzle.  Can parse on load to get fractal types

    //allows swapping with difference sizes, with the condition being that IF the swapped fractal were the correct size the swap would be valid

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