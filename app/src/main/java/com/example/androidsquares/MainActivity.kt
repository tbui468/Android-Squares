//get a complete vertical slice with two puzzle cubes (will make it easy to expand to 8 cubes later to form the tesseract)

    //complete puzzle set 4: similar to set 3, make a few variations of puzzles - can mix them up later
    //3 adjacent 2x2 squares variation
    //2 adjacent squares variation
    //3 adjacent above and two adjacent below (or 1)

    //facebook login page (or choose to play without online data)
        //this is the default view on opening the app
        //transition to the game view after logging in/choosing not to log in

    //cache test data on server - name, profile pic, and (set, puzzle, transformation count in solution)

    //read test friend data from server and display profile pics of friends

    //Back button is normal to have in the app - keep it there

    //bug - a 4x4 doesn't split apart to 1x1s when puzzle is cleared (it splits only to 1x1s)
        //also refactor puzzle clear code - it's very messy

    //make all the puzzles solvable within 2, 3 or 4 moves.  But give player 1, 2, or 3 extra moves.  Don't tell players how many moves puzzles can be completed in
        //then connect to social media and show a pictures of friends who completed it and the number of moves it took them
        //sign into facebook to allow users to see friend's scores
        //can use 'python anywhere' to save data for now (or find some free/easy deployment with nodejs)
            //user facebook name: set, puzzle, number of transformations to solve
            //save all to database on pythonanywhere website - for users to access

    //when clearing a puzzle, should automatically transition out of current puzzle
        //just transitioning out and highlighting the completed puzzle and highlighting the newly unlocked ones

    //animation for clearing puzzles (queue both these commands)
            //need to pulse colored fractals....
            //all colored fractals pulse white

    //tutorial for swap, pinch in, pinch out, rotate, reflect
        //have a dot animate showing finger motion to perform certain action


    //puzzle goal should be obvious when looking at a puzzle for the first time
        //user should know exactly where the colored squares need to go to connect all the colors
        //having a clear end goal will make the puzzle for interesting, and encourage users to start thinking about solutions

    //problem - there isn't enough integration with current puzzles -
        //it feels like a cluster of random colors (it should feel like I'm connection colors together into lines/trees
        //could add more darks colors
        //keep in mind that all the colors needs to connect on the surface of the cube

    //allows swapping with difference sizes, with the condition being that IF the swapped fractal were the correct size the swap would be valid

    //idea: have a shader that changes color theme inside each complete cube
        //need this carrot for people who like visual flair
    //idea from Ollie: show friend who completed puzzle the fastest (either set or individual puzzles??)
        //or maybe show the number of transformations your friend took to finish it
        //or show your friend's solution
        //or request solution from a facebook friend, or send solution to a facebook friend

        //or each puzzle set you complete gives you "points" to request help
        //you can use these points directly or give them to facebook friends who need help with a puzzle

    //holding finger down highlights what part of the cube you are touch (center, edge or corner) so that user learns where to touch to transform

    //puzzles - get 2 sets done (combine first two current sets together into one set.  Finish reflection/rotation for second set)
    //first set be 1x1s (of up to two colors)
    //second set introduce reflections/rotations
    //third set introduce translations (and a third color)
    //fourth set up the number of transformations to 4 + introduce 4th color (also lengthen color connections to 3 to 4 colored blocks)
    //fifth set introduce 4x4 fractals - up the length of chains
    //sixth set - up the length of chains
    //seventh - up the length of chains
    //eighth - up the length of chains


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

    override fun onBackPressed() {
        if(mSquaresSurfaceView.renderer.getScreenState() == Screen.Set)
            super.onBackPressed() //exit app
        else
            mSquaresSurfaceView.renderer.mInputQueue.add(InputData(TouchType.Back, 0f, 0f, 0.3f))
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