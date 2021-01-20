//get a complete vertical slice with two puzzle cubes

    //get python website working so I can test sending http requests from android to website (just getting admission percent from two test scores)
        //tbui123

    //I have the access token from facebook
    //using android app (and Volley) for now, send GET request to facebook to get name and email"
    //send over https so that it's secure

    //successfully connected to python website (skim the volley docs for a general overview)
        //consider setting up request queue as a singleton that lives for duration of application (so that can add http requests anytime in code)
        //responses can be strings or json objects (which will probably be more useful)
        //is a POST request necessary for sending data to the server?? Or is a GET with parameters (encrypted) enough
            //what's the difference between POST and GET again???
        //encrypt payload (access token and user facebook id) on mobile app side
        //use Volley to send payload to web server
        //decrypt payload on web server side before verifying user


    //clear a puzzle automatically transitions back to puzzle select screen after animation is done playing (after clearPulse()) function
        //need to clean up the function queue system (currently using a ton of flags to determine which function to queue/call)
        //need to make it generalized (either queue functions with parameters together, or queue functions and parameters separately and combine at call time)
        //this should also be combined with the undo queue system.
        //all commands go through this queue - no more separate systems

    //seems to be a problem with setting permission and logging in (it's doing it twice)
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

    //start views offscreen and animate them in

    //have bubbles of friends who cleared the current puzzle appear by the transformation box they completed it in
        //have a ring (or other indicator) showing completion, and the same ring appears on user bubble after clearing a puzzle
        //if more than two friends, have a bubble showing a "+12" (number of other friends).  Pushing this bubble shows a list of other friends that user can look at
            //clicking on friend bubble shows the first move transformation that player did as a hint

    //complete puzzle set 6: similar to set 3, make a few variations of puzzles - can mix them up later
    //make puzzles that can be finished in 2 transformations, but can also allow up to 3 max transformations
    //make puzzles where user has to connect 3 dark blocks (don't have too many of those yet) (including 2+ colors)
    //3 adjacent above and two adjacent below (or 1)


    //instead of a tutorial system, allow players to see first move of friend's solution
    //put in first touch of my solutions for tutorial puzzles
    //could create an animation object that just animates, fades out, and dies
    //so I could just create a bunch and not worry about destroying them

    //reorder puzzles into more interesting format (instead of the rows and columns they are in now)
        //this will also allow user to choose the next puzzle, giving them more agency and choice

    //Back button is normal to have in the app - keep it there since it's better to have an in-app way to go back (but include android back button functionality too)

    //bug - a 4x4 doesn't split apart to 1x1s when puzzle is cleared (it splits only to 1x1s)
        //also refactor puzzle clear code - it's very messy

    //make all the puzzles solvable within 2, 3 or 4 moves.  But give player 1, 2, or 3 extra moves.  Don't tell players how many moves puzzles can be completed in
        //then connect to social media and show a pictures of friends who completed it and the number of moves it took them
            //if playing offline, show the lowest possible number of moves

            //how about instead of solution, show friend's touch inputs?? This would be a fun hint (just save transformations/merge/split instead of saving actual raw inputs)
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

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.util.Log
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.*

import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class MainActivity: AppCompatActivity() {
    private lateinit var mSquaresSurfaceView: SquaresSurfaceView
    private var mOnLogin = true
    private lateinit var mSkipButton: Button
    private lateinit var mLoginButton: Button
    private lateinit var mLogo: ImageView
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var mProfileTracker: ProfileTracker

    public override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.SplashScreen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //mSquaresSurfaceView = SquaresSurfaceView(this)
        mSquaresSurfaceView = findViewById(R.id.surface_view)
        mSkipButton = findViewById(R.id.skip_button)
        mLoginButton = findViewById(R.id.login_button)
        mLogo = findViewById(R.id.logo)


        //login to facebook
        mLoginButton.setOnClickListener {
            //LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("email"))
        }

        mCallbackManager = CallbackManager.Factory.create()

        //note: object keyword here allows creation of new subclass that can then be overriden (?)
        LoginManager.getInstance().registerCallback(mCallbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("facebooktest", "success")
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = MyProfileTracker()
                }else {
                    processProfile(Profile.getCurrentProfile())
                }
            }
            override fun onCancel() {
                Log.d("facebooktest", "cancel")
            }
            override fun onError(error: FacebookException) {
                Log.e("facebooktest", "error")
            }
        })

        mSkipButton.setOnClickListener {
            mOnLogin = false
            moveMenuOffScreen()
            mSquaresSurfaceView.renderer.openGame()
        }

    }

    companion object {
        fun processProfile(profile: Profile) {
            Log.d("facebooktest", profile.id.toString())
            /*
            var text: String
            //settting up queue for http requests
            val queue = Volley.newRequestQueue(this)
            val url = "https://graph.facebook.com/" //user id goes here
            val fields = "?fields=name&access_token=" //access token goes here

            val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
            if(accessToken != null) Log.d("facebooktest", "access token: $accessToken")
            Log.d("facebooktest", isLoggedIn.toString())
            //val completeUrl = url + user.toString() + fields + accessToken.toString()
            val requestString = StringRequest(Request.Method.GET, completeUrl,
                    { response->
                        text = response.substring(0, 79)
                        Log.d("facebooktest", text)
                    },
                    {
                        text = "error with response"
                        Log.d("facebooktest", text)
                    })

            queue.add(requestString)*/
        }
    }

    class MyProfileTracker : ProfileTracker() {
        override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
            processProfile(currentProfile!!)
            stopTracking()
        }
    }

    private fun moveMenuOffScreen() {
        ObjectAnimator.ofFloat(mLogo, "translationX", 900f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(mLoginButton, "translationX", -900f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(mSkipButton, "translationX", 900f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun moveMenuOnScreen() {
        ObjectAnimator.ofFloat(mLogo, "translationX", 0f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(mLoginButton, "translationX", 0f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(mSkipButton, "translationX", 0f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    override fun onBackPressed() {
        if(!mOnLogin && mSquaresSurfaceView.renderer.getScreenState() == Screen.Set) {
            Handler(mainLooper).postDelayed({
                moveMenuOnScreen()
            }, 500)
            mSquaresSurfaceView.renderer.closeGame()
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