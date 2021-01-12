//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //don't worry about transitions now - create and destroy everything on open/close
        //deal with animations later


    //have all squares appear when opening a set
        //set all indices to max 4x6 (4 columns and 6 rows)

    //on click square
    //square fades out, and is destroyed on animation end
    //create fractals, and fade in

    //make puzzles contain be playable in a 4x6 grid

    //make everything 2d again - make the 'cube' a puzzleset to select
        //selecting apuzzleset zooms in to display a grid of up to 24 puzzles (recall that we can fit 6x4 squares on the screen)
        //one bronze level, two silver levels, 3 gold levels, 3 platinum levels

    //puzzle goal should be obvious when looking at a puzzle for the first time
        //user should know exactly where the colored squares need to go to connect all the colors
        //having a clear end goal will make the puzzle for interesting, and encourage users to start thinking about solutions

    //reset camera back to normal position looking down z axis
        //get the puzzles fun first before worring fancy 3d cameras
        //design 1 cube puzzle (both fractal and square puzzles)
        //make egdes of square visible for easier selection/transformation

    //get cubes in correct location on screen (Dali cross formation)
    //save camera positions for easy/quick changing
        //give all cubes the same orientation when opening (including the squares/fractals)
        //when opening a cube/square, move to camera towards the center point, but keep the camera orienation the same
            //from camera's perspective, just shift left/right/up/down
            //this will simplify everything since everything is facing the same direction

            //IMPLEMENTATION: calculate camera orientation default (when camera is first created)
                //start camera orientation and open cube/square orientation are set once and never changed
                //calculate point that is x distance from center of cube/square, that also satisfies camera orientation requirement (equal to default orientation)
                    //need to save look-at variable to camera class and update per step while animating this
                //look at will just be set to center pos of cube/square

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