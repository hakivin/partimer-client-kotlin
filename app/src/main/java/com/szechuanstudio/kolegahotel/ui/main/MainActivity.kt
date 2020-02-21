package com.szechuanstudio.kolegahotel.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.utils.KeepStateNavigator
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        setSupportActionBar(toolbar)

        navView.setOnNavigationItemSelectedListener {item ->
            onNavDestinationSelected(item, navController)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!

        // setup custom navigator
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator

        // set navigation graph
        navController.setGraph(R.navigation.mobile_navigation)

        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println(resultCode)
    }
}
