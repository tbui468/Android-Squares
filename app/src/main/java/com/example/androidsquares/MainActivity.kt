//get a complete vertical slice with two puzzle cubes

    ///////////////////////////////////////TODO NOW///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////NETWORKING////////////////////////////////////////////
    //look at SquaresServer project for the serverside stuff

    //should just save user_id, and moves it took to solve a particular puzzle
        //attributes: user_id, p00, p01, ..., p77 (a tuple has 65 attributes)

    //seems like Facebook graph API documenation with javascript is better than python
    //set up nodejs/expressjs backend and host on heroku (write in Typescript)
    //send access token, user id, app id? over https to web server
    //make Graph API call to verify access token, user_id, and app id is all correct before allowing user to update database

    //for now, just use facebook (and maybe google) as a way to verify users
        //since I don't want randos who aren't using my app to post to the database

    //have bubbles of friends who cleared the current puzzle appear by the transformation box they completed it in
    //have a ring (or other indicator) showing completion, and the same ring appears on user bubble after clearing a puzzle
    //if more than two friends, have a bubble showing a "+12" (number of other friends).  Pushing this bubble shows a list of other friends that user can look at
    //clicking on friend bubble shows the first move transformation that player did as a hint

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////CONTENT///////////////////////////////////////////
    //look through puzzles and reorder them
    //difficulty (whether rotations/reflections/large swaps are necesssary
    //mark puzzles (using comments) with minimum number of transformations needed to solve AND max number of transformations I will give people
    //look at shapes of puzzles and remove /change any duplicate shapes

    //////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////USER EXPERIENCE////////////////////////////////////////////
    //how should squares and fractals animate on/offscreen?
        //idea: have them spawn overlapped with parent object (sets for squares, and squares for fractals)
        //and then they animate to final positions

    //could have a separate queue for pulsing fractals
    //this queue takes in fractal list and pulse duration,
        //it then calls the pulse commands in sequence (where each pulse may be multiple fractals since there could be multiple colors)
        //??? think this through a little more

    //bug: undo bar can still be seen with 0 transformations (to the left)

    //bug: corners of quads are transparent, but when they overlap bad visual artifacts

    //also, need a way to differentiate between sets, puzzles, and fractals

    //when user clicks on fractal, create a semi transparent circle expand from that point (like how android framework does it)
        //this is to provide feedback and also keep visual style consistent
        //also provides visual feedback when waiting for a double tap (rather than just showing no animation)

    //bug: can see undo bar in the far distance when at main/set menu (also don't really like the undo bar having empty middles - it draws the eyes too much)

    //create main logo (how can it keep with the same simple theme of the rest of the game)

    ////////////////////////////////////////////////////////////////////////////////////


    //instead of a tutorial system, allow players to see first move of friend's solution
    //put in first touch of my solutions for tutorial puzzles
    //could create an animation object that just animates, fades out, and dies
    //so I could just create a bunch and not worry about destroying them

    //reorder puzzles into more interesting format (instead of the rows and columns they are in now)
        //this will also allow user to choose the next puzzle, giving them more agency and choice

    //Back button is normal to have in the app - keep it there since it's better to have an in-app way to go back (but include android back button functionality too)

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
import org.json.JSONObject

class MainActivity: AppCompatActivity() {
    private lateinit var mSquaresSurfaceView: SquaresSurfaceView
    private var mOnLogin = true
    private lateinit var mSkipButton: Button
    private lateinit var mLoginButton: Button
    private lateinit var mLogo: ImageView
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var mProfileTracker: ProfileTracker
    private lateinit var mTestButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.SplashScreen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //mSquaresSurfaceView = SquaresSurfaceView(this)
        mSquaresSurfaceView = findViewById(R.id.surface_view)
        mSkipButton = findViewById(R.id.skip_button)
        mLoginButton = findViewById(R.id.login_button)
        mLogo = findViewById(R.id.logo)
        mTestButton = findViewById(R.id.test_button)

        mTestButton.setOnClickListener {
            if(Profile.getCurrentProfile() != null) {
                processProfile()
            }
        }

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

    private fun processProfile() {
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()

        if(accessToken == null || accessToken.isExpired) return

        Log.d("facebooktest", accessToken.toString())

        val request: GraphRequest = GraphRequest.newMeRequest(accessToken, object: GraphRequest.GraphJSONObjectCallback {
            override fun onCompleted(obj: JSONObject, response: GraphResponse) {
                Log.d("facebooktest", obj.toString())
            }
        })

        val parameters = Bundle()
        parameters.putString("field", "name, id")
        request.parameters = parameters
        request.executeAsync()

    }

    class MyProfileTracker : ProfileTracker() {
        override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
            stopTracking()
        }
    }

    private fun moveMenuOffScreen() {
        ObjectAnimator.ofFloat(mLogo, "translationX", -1200f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(mLoginButton, "translationX", 1200f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(mSkipButton, "translationX", -1200f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(mTestButton, "translationX", 1200f).apply {
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
        ObjectAnimator.ofFloat(mTestButton, "translationX", 0f).apply {
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