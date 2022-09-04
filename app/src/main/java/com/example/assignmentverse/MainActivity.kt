package com.example.assignmentverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initalise()

        val navController = findNavController(R.id.navController)

        bottomNavBar.setupWithNavController(navController)

    }

    private fun initalise() {
        bottomNavBar = findViewById(R.id.bottom_menu)
    }
}