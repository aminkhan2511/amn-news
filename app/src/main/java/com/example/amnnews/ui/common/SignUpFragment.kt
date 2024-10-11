package com.example.amnnews.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.amnnews.R
import com.example.amnnews.ui.main.MainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SignUpFragment : Fragment() {

    private val TAG = "firebaseTesting"

    private lateinit var etEmail: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var etPassword: TextInputEditText

    private lateinit var linlaySignup: LinearLayout

    private lateinit var auth: FirebaseAuth

    private lateinit var loginHereTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        // Hide bottom navigation and toolbar from the activity
        (activity as? MainActivity)?.hideBottomNavAndToolbar()
        initView(view)
        initListners()
        return view

    }

    private fun initListners() {
        linlaySignup.setOnClickListener {
            if (checkInput()) {
                createAccount(etEmail.text.toString().trim(), etPassword.text.toString().trim())
            }
        }

        loginHereTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginAndSignupPageFragment)
        }
    }

    private fun checkInput(): Boolean {

        if (etEmail.text.isNullOrBlank()) {
            Toast.makeText(context, "Email cant be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if (etPassword.text.isNullOrBlank()) {
            Toast.makeText(context, "Password cant be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if (etConfirmPassword.text.isNullOrBlank()) {
            Toast.makeText(context, "Confirm Password cant be empty", Toast.LENGTH_SHORT).show()
            return false
        } else {
            // Get the text from both EditTexts
            val password = etPassword.text?.toString()
            val confirmPassword = etConfirmPassword.text?.toString()

            // Check if passwords are the same
            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true

    }

    private fun initView(view: View) {
        auth = Firebase.auth
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        linlaySignup = view.findViewById(R.id.linlaySignup)
        loginHereTextView = view.findViewById(R.id.loginHereTextView)


    }


    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Successful sign-in
                    val user = Firebase.auth.currentUser
                    println("User signed UP: ${user?.email}")
                    Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    takeUserToDashBoard()

                } else {
                    // Handle failure
                    println("Sign-UP failed: ${task.exception?.message}")
                    Toast.makeText(context, "Failed to create account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

        // [END create_user_with_email]
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            takeUserToDashBoard()
        }

    }

    private fun takeUserToDashBoard() {
        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
    }


}