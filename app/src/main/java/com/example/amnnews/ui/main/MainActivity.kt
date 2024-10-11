package com.example.amnnews.ui.main


import MyBottomSheetDialogFragment
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.amnnews.R
import com.example.amnnews.data.local.DatabaseRoom
import com.example.amnnews.data.repository.MainRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var viewModel: NewsViewModelMain

    lateinit var bottomNavigationView: BottomNavigationView


    lateinit var navHostFragmentAuth: NavHostFragment

    lateinit var navHostFragment: NavHostFragment


    lateinit var toolbar: Toolbar
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth


        // setting viewmodel
        val newsRepository = MainRepository(DatabaseRoom.getDatabase(applicationContext))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository, application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModelMain::class.java]


//********************************************setting toolbar*****************************************
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


//        ***************************** Making nav controller********************************************************************
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment



        navController =
            navHostFragment.navController  //        here we are finding //navcontroller because we are in activity now so we cant use direct findnavecontrollar() thats why


//        adding fragment in it
//***************************************ATTACHING TOOLBAR WITH NAV CONTROLLER SO THAT WHAT EVER IS FRAGMETN YOU ARE CURRENT IN WILL SHOW NAME IN TOOLBAR************************************
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.favoriteFragment,
                R.id.exploreFragment,
                R.id.myBottomSheetDialogFragment, R.id.inShortNewsFragment
            )
        )// here we are making drawerlayout and passing top lever destinations


//***************************************ATTACHING TOOLBAR WITH NAV CONTROLLER SO THAT WHAT EVER IS FRAGMETN YOU ARE CURRENT IN WILL SHOW NAME IN TOOLBAR************************************
        toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )//this will responsible for showing title to user accordingt to current location like home,settingt,more etc//TOOLBAR SETTING UP WITH NAVIGATION UI


//        *******************************SETTING UP BOTTOM NAVIGATION BAR************************************************************************
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(// here we are setting up bottom nav with nav controllaer//BOTTOM NAVIGATION SETTING UP WITH NAVIGATION UI
            navController
        )



//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.moreFragment -> {
//                    // Perform custom action for Home tab
////                    Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show()
//                    // Inside an Activity or Fragment
//                  navController.navigate(R.id.myBottomSheetDialogFragment)
//
//
//                    true // Return true to indicate the event is handled
//                }
////                R.id.nav_profile -> {
////                    // Perform custom action for Profile tab
////                    Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
////                    true
////                }
////                R.id.nav_settings -> {
////                    // Perform custom action for Settings tab
////                    Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
////                    true
////                }
//                else -> false // Let NavController handle other navigation
//            }
//        }


    }


    fun showBottomNavigation(show: Boolean) {
        bottomNavigationView.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {// three dot click listners and navigation drawer menu also
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {//handling title or toolbar

        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }



     fun updateData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {

            showBottomNavigation(true)
        } else {
            supportActionBar?.hide()
            showBottomNavigation(false)

        }
    }




    fun hideBottomNavAndToolbar() {
        // Assuming you have a toolbar and bottom navigation in your layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Hide toolbar and bottom navigation
        toolbar.visibility = View.GONE
        bottomNav.visibility = View.GONE
    }

    fun showBottomNavAndToolbar() {
        // Show toolbar and bottom navigation
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        toolbar.visibility = View.VISIBLE
        bottomNav.visibility = View.VISIBLE
    }
}