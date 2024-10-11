package com.example.amnnews.ui.common

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
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


class LoginAndSignupPageFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var linlayLogin:LinearLayout

    lateinit var  registerTxt:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_login_and_signup_page, container, false)
        // Hide bottom navigation and toolbar from the activity
        (activity as? MainActivity)?.hideBottomNavAndToolbar()
         registerTxt = view.findViewById<TextView>(R.id.registerTextView)

//        val currentDestination = findNavController().currentDestination
//        Log.d("Navigation", "Current Destination: ${currentDestination?.label}")
        makeRegisterClickable(registerTxt)


        initView(view)
        initClickLis()

        return view
    }

    private fun initClickLis() {
        linlayLogin.setOnClickListener {
            if(checkInput())
            {
                Log.d("Signin11",etEmail.text.toString().trim())
                Log.d("Signin11",etPassword.text.toString().trim())
                signInUser(etEmail.text.toString().trim(),etPassword.text.toString().trim())
            }
        }



    }

    private fun signInUser(etEmail: String, etPassword: String) {

        auth.signInWithEmailAndPassword(etEmail, etPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    findNavController().navigate(R.id.action_loginAndSignupPageFragment_to_homeFragment)
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
    }

    private fun checkInput(): Boolean {

        if (etEmail.text.isNullOrBlank())
        {
            Toast.makeText(context,"Email cant be empty",Toast.LENGTH_SHORT).show()
            return false
        }
        else if (etPassword.text.isNullOrBlank())
        {
            Toast.makeText(context,"Password cant be empty",Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }


    private fun initView(view: View) {

        auth = Firebase.auth
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        linlayLogin = view.findViewById(R.id.linlayLogin)
    }


    private fun makeRegisterClickable(registerTxt: TextView) {
        val text = "Register"

        // Set the text
        registerTxt.text = text

        // Enable Linkify for the TextView
        registerTxt.setLinkTextColor(Color.BLUE)
        registerTxt.movementMethod = LinkMovementMethod.getInstance()

        // Add a click listener
        registerTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginAndSignupPageFragment_to_signUpFragment)
        }
    }


}