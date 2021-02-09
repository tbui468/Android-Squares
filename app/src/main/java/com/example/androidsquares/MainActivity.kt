//get a complete vertical slice with two puzzle cubes

    ///////////////////////////////////////TODO NOW////////////////////////////////////
    //it might be better to have separate puzzles (instead of putting them all in one field)
        //each puzzle is the composed on 1, 2 or 4 mini puzzles
        //each 'Mini' is connected to other minis either by shift or reflections
        //any transformation of fractals in a mini will be easier to keep track of in this case

        //bug: if shifted/reflected puzzle, we don't want to allow translations into the shifted/reflected play field
        //implement the top/bottom matching transformations
            //need to implement merging and splitting of fractals too (crashing when undoing requires a fractal resize)
        //make function to get fractal based on puzzletype and base fractal
            //getOtherFractalIndex(puzzleType, baseIndex)
        //expand the flexibility of the shift/reflect puzzles to allow greater puzzle variety

    //write 4 puzzles for set 5
    //3 or 4  or 5 transformations (can reorder them later)
    //up to three colors - try to have puzzles that take advantage of 5x6 grid
    //for shifted and reflected puzzles, only the interactable fractals need to match - blocks don't need to have a corresponding match
    //add puzzles with 4 and 5 transformations too (later)
    //once all 8 extra sets are complete, reorganize and cut the similar ones and reorder the ones kept
    ///////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////CORE//////////////////////////////////////////////////////////
    //background music - placeholder is fine (set up resource files that can be loaded in on app start)
    //use MediaPlayer
    //sound effects - placeholders are fine (set up resource files that can be loaded in on app start)
    //use SoundPool builder??? Apparently there are people who have problems with this
    //look around for more options before settling on a sound API for sound effects

    //tutorial (show taps and drags)

    //create main logo (how can it keep with the same simple theme of the rest of the game)
    //add glow effects to simulate how final design should look
    ////////////////////////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////////////////////////////

package com.example.androidsquares

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.os.Bundle


class MainActivity: AppCompatActivity() {
    private lateinit var mSquaresSurfaceView: SquaresSurfaceView
    private lateinit var mMediaPlayer: MediaPlayer

    public override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.SplashScreen)
        super.onCreate(savedInstanceState)

        mSquaresSurfaceView = SquaresSurfaceView(this)
        /*
        mMediaPlayer = MediaPlayer.create(this, R.raw.winter)
        mMediaPlayer.start()*/
        setContentView(mSquaresSurfaceView)
    }



    override fun onBackPressed() {
/*
        mMediaPlayer.stop()
        mMediaPlayer.prepare()
        mMediaPlayer.start()*/
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
//        mMediaPlayer.start()
        super.onResume()
    }

    public override fun onPause() {
 //       mMediaPlayer.pause()
        super.onPause()
    }

    public override fun onDestroy() {
  //      mMediaPlayer.release()
        super.onDestroy()
    }
}