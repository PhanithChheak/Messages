package com.phanith.messages

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.phanith.messages.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.btnSignUp.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }

        binding.btnLogIn.setOnClickListener{
            val email = binding.edtGmail.text.toString()
            val password = binding.edtPassword.text.toString()

            login(email,password)
        }
    }

    private fun login(email: String, password: String){

        //FireBase Document for log in user: https://firebase.google.com/docs/auth/android/password-auth#kotlin+ktx_1
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // go to main activity
                    val intent = Intent(this@LogIn, MainActivity::class.java)
                    startActivity(intent)

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this@LogIn, "User does not exist.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }

    }
}