package com.example.application

import android.content.Intent
import android.icu.util.TimeUnit.values
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.application.databinding.ActivitySingInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.chrono.JapaneseEra.values

class SingInActivity : AppCompatActivity() {

    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth
    lateinit var bindingClass: ActivitySingInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Api exception", Toast.LENGTH_SHORT).show()
            }
        }
        buttonListener()
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun singInWithGoogle() {
        val singInClient = getClient()
        launcher.launch(singInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(applicationContext, "Google sign is successes", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Google sign is canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun buttonListener() {
        bindingClass.bSingIn.setOnClickListener {
            singInWithGoogle()
        }
    }
}