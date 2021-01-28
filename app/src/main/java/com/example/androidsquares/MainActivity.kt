//get a complete vertical slice with two puzzle cubes

    ///////////////////////////////////////TODO NOW///////////////////////////////////////////////
    //write 8 more set 3 puzzles
    //3 transformations
    //up to three colors - try to have puzzles that take advantage of 5x6 grid
    //add puzzles with 4 and 5 transformations too (later)
    //once all 8 extra sets are complete, reorganize and cut the similar ones and reorder the ones kept
    ///////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////SOCIAL FEATURES////////////////////////////////////////////
    //look at SquaresServer project (index.ts) for the serverside stuff
    //idea: seed the database with bot data (set id to a negative number)
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////CORE//////////////////////////////////////////////////////////
    //background music - placeholder is fine (set up resource files that can be loaded in on app start)
    //sound effects - placeholders are fine (set up resource files that can be loaded in on app start)

    //create main logo (how can it keep with the same simple theme of the rest of the game)
        //add glow effects to simulate how final design should look

    ////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////USER EXPERIENCE////////////////////////////////////////////
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
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
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

            if(Profile.getCurrentProfile() == null) return@setOnClickListener

            val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
            if(accessToken == null || accessToken.isExpired) return@setOnClickListener

            //need to add puzzle data I want to update on database here
            val data: JSONObject = JSONObject().also {
                it.put("fb_id", Profile.getCurrentProfile().id)
                it.put("access_token", accessToken.token)
                it.put("app_id", getString(R.string.facebook_app_id))
            }

            postUserData(getString(R.string.server_url), data)
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

    //send post request to server.  Currently does not contain puzzle data
    private fun postUserData(url: String, data: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, data,
                {
                    Log.d("facebooktest", "response received")
                },
                {
                    Log.d("facebooktest", "response error")
                }
        )

        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)
    }

    /*
    //was used to send request to facebook API, but now this will be done on the server
    private fun processProfile() {
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()

        if(accessToken == null || accessToken.isExpired) return

        Log.d("facebooktest", "Access token: " + accessToken.toString())
        Log.d("facebooktest", "profile ID: " + Profile.getCurrentProfile().getId())

        val request: GraphRequest = GraphRequest.newMeRequest(accessToken, object: GraphRequest.GraphJSONObjectCallback {
            override fun onCompleted(obj: JSONObject, response: GraphResponse) {
                Log.d("facebooktest", obj.toString())
            }
        })

        val parameters = Bundle()
        parameters.putString("field", "name, id")
        request.parameters = parameters
        request.executeAsync()
    }*/

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