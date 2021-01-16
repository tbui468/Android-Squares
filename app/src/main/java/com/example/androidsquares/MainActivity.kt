//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //design set2 puzzles - introduce translation of 2x2 and a third color

    //animation for clearing puzzles

    //puzzle goal should be obvious when looking at a puzzle for the first time
        //user should know exactly where the colored squares need to go to connect all the colors
        //having a clear end goal will make the puzzle for interesting, and encourage users to start thinking about solutions

    //problem - there isn't enough integration with current puzzles -
        //it feels like a cluster of random colors (it should feel like I'm connection colors together into lines/trees
        //could add more darks colors
        //keep in mind that all the colors needs to connect on the surface of the cube

    //allows swapping with difference sizes, with the condition being that IF the swapped fractal were the correct size the swap would be valid

    //idea: have a shader that changes color theme inside each complete cube

    //holding finger down highlights what part of the cube you are touch (center, edge or corner) so that user learns where to touch to transform

    //puzzles - get 2 sets done (combine first two current sets together into one set.  Finish reflection/rotation for second set)
    //first set be 1x1s (of up to two colors)
    //second set introduce reflections/rotations
    //third set introduce translations (and a third color)
    //fourth set up the number of transformations to 4 + introduce 4th color
    //fifth set introduce 4x4 fractals - 4 transformations
    //sixth set up the number of transformations to 5 (no new concepts)
    //seventh up the number of transformations up to 5 (no new concepts)
    //eighth up the number of transformations up to 5 (no new concepts)


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