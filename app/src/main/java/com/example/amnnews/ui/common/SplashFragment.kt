package com.example.amnnews.ui.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.amnnews.R
import com.example.amnnews.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide bottom navigation and toolbar here
        (activity as? MainActivity)?.hideBottomNavAndToolbar()

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()

        // Simulate splash screen delay and navigate based on user authentication state
        GlobalScope.launch(Dispatchers.Main) {
            delay(3000) // 3-second delay
            Log.d("SplashFragment", "onStart: Splash screen finished")

            // Check if the user is logged in or not
            val currentUser = auth.currentUser
            if (currentUser != null) {
                findNavController().navigate(R.id.action_splashFragment2_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment2_to_loginAndSignupPageFragment)
            }
        }
    }
}
