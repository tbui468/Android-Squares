//get a complete vertical slice with two puzzle cubes

    //Bug: when pushing back button while at login screen, the app crashes

    //complete puzzle set 5: similar to set 3, make a few variations of puzzles - can mix them up later
    //3 adjacent 2x2 squares variation
    //3 adjacent above and two adjacent below (or 1)
    //make puzzles where user has to connect 3 dark blocks (don't have too many of those yet) (including 2+ colors)

    //seems to be a problem with setting permission and loggging in (it's doing it twice)
        //push facebook login button passes the baton to CallbackManager which has user login with name/avatar permission
        // then comes back and executes my code in setButtonListener (which adds email permission)
        //this causes facebook to go back to login page with name/avatar/email permission
            //might need to see the other option for loggin in on the documentation and not rely on CallbackManager

    //build a quick and dirty implementation of the steps below:
        //get fb_user_id and access_token on android (both strings)
            //I have the access_token - how to get fb_user_name?
        //send http request to webserver to authenticate user (by issuing Graph API request using fb_user_name and access_token sent to from android device)
            //let's use Nodejs, express and TypeScript to set up web server - no need for html/css since we only need to send http requests with two parameters
            //web server has two forms - fb_user_id, and access_token.  use curl to send http request to Graph API with this
        //if fb_user_id and access_token verified, 'log in' to the server database using fb_user_name (and now can alter database entries for this user)
        //when user solves a puzzle/saves data, write data (such as transformations taken to solve puzzles) to database

    //make transition from login page to game page more natural (transition buttons off-screen and then transitioning sets on-screen, for example)

    //reorder puzzles into more interesting format (instead of the rows and columns they are in now)
        //this will also allow user to choose the next puzzle, giving them more agency and choice

    //Back button is normal to have in the app - keep it there since it's better to have an in-app way to go back (but include android back button functionality too)

    //bug - a 4x4 doesn't split apart to 1x1s when puzzle is cleared (it splits only to 1x1s)
        //also refactor puzzle clear code - it's very messy

    //make all the puzzles solvable within 2, 3 or 4 moves.  But give player 1, 2, or 3 extra moves.  Don't tell players how many moves puzzles can be completed in
        //then connect to social media and show a pictures of friends who completed it and the number of moves it took them
            //if playing offline, show the lowest possible number of moves
        //puzzles floating in the background also have friend's profile pics attached
        //tap on friend's profile to request hint???
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

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.util.Log
import android.os.Bundle
import com.facebook.AccessToken

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class MainActivity: AppCompatActivity() {
    private lateinit var mSquaresSurfaceView: SquaresSurfaceView
    private var mOnLogin = true
    private lateinit var mSkipButton: Button
    private lateinit var mLoginButton: Button
    private lateinit var mCallbackManager: CallbackManager

    public override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("hmm", "onCreate called")
        setTheme(R.style.SplashScreen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        initLoginView()
    }

    private fun initLoginView() {
        mSquaresSurfaceView = SquaresSurfaceView(this)

        mSkipButton = findViewById(R.id.skip_button)
        mLoginButton = findViewById(R.id.login_button)

        //login to facebook
        mLoginButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("email"))
        }

        mCallbackManager = CallbackManager.Factory.create()

        //note: object keyword here allows creation of new subclass that can then be overriden (?)
        LoginManager.getInstance().registerCallback(mCallbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("facebooktest", "success")
                val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                Log.d("facebooktest", isLoggedIn.toString())
            }
            override fun onCancel() {
                Log.d("facebooktest", "cancel")
            }
            override fun onError(error: FacebookException) {
                Log.e("facebooktest", "error")
            }
        })

        //skip loggin into facebook
        mSkipButton.setOnClickListener {
            setContentView(mSquaresSurfaceView)
            mOnLogin = false
        }
    }


    override fun onBackPressed() {
        if(!mOnLogin && mSquaresSurfaceView.renderer.getScreenState() == Screen.Set) {
            setContentView(R.layout.login)
            initLoginView()
            mOnLogin = true
        }else if(mOnLogin) {
            super.onBackPressed()
        }else {
            mSquaresSurfaceView.renderer.mInputQueue.add(InputData(TouchType.Back, 0f, 0f, 0.3f))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
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