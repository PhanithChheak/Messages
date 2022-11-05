package com.phanith.messages

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.phanith.messages.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var DbRef: DatabaseReference

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        //Set up form validation action
        binding.NameContainer.helperText = null
        binding.GmailContainer.helperText = null
        binding.PasswordContainer.helperText = null
        binding.PasswordContainer2.helperText = null


        nameFocusListener()
        gmailFocusListener()
        passwordFocusLister()

        //Sign up button on click action
        binding.btnSignUp.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtGmail.text.toString()
            val password = binding.edtPassword.text.toString()


            if (binding.NameContainer.helperText == null &&
                binding.GmailContainer.helperText == null &&
                binding.PasswordContainer.helperText == null &&
                binding.PasswordContainer2.helperText == null
            ) {
                signUp(name, email, password)
            }else{
                Toast.makeText(
                    this@SignUp, "incorrect form",
                    Toast.LENGTH_SHORT
                ).show()

            }


        }
    }

    private fun signUp(name: String, email: String, password: String) {

        //FireBase Document for Sign up user: https://firebase.google.com/docs/auth/android/password-auth#kotlin+ktx_1
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // go to main Activity

                    addUserToDatabase(name, email, auth.currentUser?.uid!!)

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@SignUp, "Connection issue with Database.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        //initialize database
        DbRef = FirebaseDatabase.getInstance().getReference()
        DbRef.child("user").child(uid).setValue(User(name, email, uid))
    }


    // Form validation

    private fun nameFocusListener() {
        binding.edtName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.NameContainer.helperText = isValidName()
            }
        }
    }

    private fun gmailFocusListener() {
        Log.i("GmailFunction","run")
        binding.edtGmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.GmailContainer.helperText = isValidEmail()
            }
        }

    }

    private fun passwordFocusLister() {
        binding.edtPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.PasswordContainer.helperText = isValidPassword()
                binding.PasswordContainer2.helperText = isValidPassword()
            }
        }

    }


    private fun isValidName(): String? {
        val text = binding.edtName.text.toString()

        if (text.length < 6) {
            return " minimum text length 6"
        }
        if (!text.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        return null
    }


    private fun isValidEmail(): String? {
        val text = binding.edtGmail.text.toString()
        return if (EMAIL_ADDRESS_PATTERN.matcher(text).matches()) {
            null
        } else {
            "incorrect Email"
        }
    }


    private fun isValidPassword(): String? {
        val text = binding.edtPassword.text.toString()
        val text1 = binding.edtPassword2.text.toString()

        if (text.length < 6) {
            return " minimum text length 6"
        }
        if (!text.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (text != text1) {
            return "Password does not match"
        }
        return null

    }


}