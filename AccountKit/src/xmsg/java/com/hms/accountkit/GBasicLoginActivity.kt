package com.hms.accountkit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hms.availabletoalllbraries.BaseActivity
import kotlinx.android.synthetic.xmsgh.layout_activity.*
import kotlinx.android.synthetic.xmsgh.layout_activity.sign_in_button
import kotlinx.android.synthetic.xmsgh.login_activity.*

class GBasicLoginActivity : BaseActivity(true) {

    companion object{
        fun newStartActivity(context: Context){
            context.startActivity(Intent(context,HBasicLoginActivity::class.java))

        }
    }

    val TAG=BaseActivity::class.java.simpleName
    private val RC_SIGN_IN = 234

    //creating a GoogleSignInClient object
    var mGoogleSignInClient: GoogleSignInClient? = null

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_activity)
        supportActionBar?.title="Google Login"

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        //first we intialized the FirebaseAuth object
        //    mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()

        //Then we will get the GoogleSignInClient object from GoogleSignIn class

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
       sign_in_button.setOnClickListener { signIn() }


    }



    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode ==RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Sign In was successful, authenticate with Firebase
                val account =
                    task.getResult(
                        ApiException::class.java
                    )
                Toast.makeText(this, "user signed in", Toast.LENGTH_SHORT).show()
                status_details.text="user signed in "+ account?.displayName

                //authenticating with firebase
                /// firebaseAuthWithGoogle(account);
            } catch (e: ApiException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}