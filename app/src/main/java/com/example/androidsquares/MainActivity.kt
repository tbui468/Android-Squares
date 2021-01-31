//get a complete vertical slice with two puzzle cubes

    ///////////////////////////////////////TODO NOW////////////////////////////////////
    //make logo object
    //add logo object to logo screen
        //when user taps it, log animates off
        //when user taps back button from set menu screen, logo animates on
    ///////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////CORE//////////////////////////////////////////////////////////
    //tutorial (show taps and drags)

    //background music - placeholder is fine (set up resource files that can be loaded in on app start)
    //sound effects - placeholders are fine (set up resource files that can be loaded in on app start)

    //create main logo (how can it keep with the same simple theme of the rest of the game)
    //add glow effects to simulate how final design should look

    //write 4 more set 4 puzzles
    //3 or 4  or 5 transformations (can reorder them later)
    //up to three colors - try to have puzzles that take advantage of 5x6 grid
    //add puzzles with 4 and 5 transformations too (later)
    //once all 8 extra sets are complete, reorganize and cut the similar ones and reorder the ones kept

    ////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////SOCIAL FEATURES///////////////////////////////////////////
    //consider dropping these features (or delaying them until done with core features)
        //look at SquaresServer project (index.ts) for the serverside stuff
        //idea: seed the database with bot data (set id to a negative number)
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////USER EXPERIENCE////////////////////////////////////////////
    //when a puzzle is cleared for the first time, and after the pulse animations, create an animated white spark in the blocks
        //this white spark should always be there to signify to use that this puzzle is already cleared (instead of having to remember if Square was yellow while inside puzzle)
    //add shaders that change theme as a prize at the end of each (or near the end) of each set

    //high chance that independent animation that can be fired off and forgotten will be needed (for non-essential/non-sequential animations)
        //find a way to implement this without breaking too much stuff

    //each fractal pules in turn, but it would look smoother if the animations overlapped instead of being play one after another

    //add a small amount of camera sway to make it more visually interesting (or at least animating background)

    //add Geometry Wars style glow shaders

    //add a little wigglyness to the squares when moving (squishing and stretching based on how user interacts with it)

    //how should squares and fractals animate on/offscreen?
        //idea: have them spawn overlapped with parent object (sets for squares, and squares for fractals)
        //and then they animate to final positions

    //also, need a way to differentiate between sets, puzzles, and fractals

    //when user clicks on fractal, create a semi transparent circle expand from that point (like how android framework does it)
        //this is to provide feedback and also keep visual style consistent
        //also provides visual feedback when waiting for a double tap (rather than just showing no animation)

    ////////////////////////////////////////////////////////////////////////////////////

package com.example.androidsquares

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.util.Log
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator


class MainActivity: AppCompatActivity() {
    private lateinit var mSquaresSurfaceView: SquaresSurfaceView
    private lateinit var mSkipButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.SplashScreen)
        super.onCreate(savedInstanceState)

        mSquaresSurfaceView = SquaresSurfaceView(this)
        setContentView(mSquaresSurfaceView)
    }



    override fun onBackPressed() {
        Log.d("ddd", mSquaresSurfaceView.renderer.getScreenState().toString())
        if(mSquaresSurfaceView.renderer.getScreenState() == Screen.Logo) {
            super.onBackPressed()
        }else {
            mSquaresSurfaceView.renderer.mInputQueue.add(InputData(TouchType.Back, 0f, 0f, 0.3f))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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