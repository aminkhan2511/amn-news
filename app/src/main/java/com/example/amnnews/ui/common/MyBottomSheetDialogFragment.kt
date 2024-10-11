import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.amnnews.R
import com.example.amnnews.ui.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.navigation.fragment.findNavController

class MyBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Settings option click
        view.findViewById<TextView>(R.id.settings_option).setOnClickListener {
            // Handle Settings option click
            dismiss()  // Close the bottom sheet
        }

        // Handle Sign Out option click
        view.findViewById<TextView>(R.id.sign_out_option).setOnClickListener {
            // Show the confirmation dialog
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        // Use requireContext() to ensure a non-null context
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to sign out?")

        // Set up the Yes button
        builder.setPositiveButton("Yes") { dialog, which ->
            // Perform the sign out action
            Firebase.auth.signOut()

            // Navigate to LoginFragment after signing out
            navigateToLoginFragment()

        }

        // Set up the No button
        builder.setNegativeButton("No") { dialog, which ->
            // Dismiss the dialog when No is clicked
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun navigateToLoginFragment() {
        // Use findNavController() to navigate to the LoginFragment
        val navController = findNavController()

        // Make sure the navigation action exists in your navigation graph
        navController.navigate(R.id.action_myBottomSheetDialogFragment_to_loginAndSignupPageFragment)

//        // Hide bottom navigation and toolbar from the activity
//        (activity as? MainActivity)?.hideBottomNavAndToolbar()
    }
}
