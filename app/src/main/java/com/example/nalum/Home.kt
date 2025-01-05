@file:Suppress("DEPRECATION")

package com.example.nalum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity() {

    // Declare variables without initialization
    private lateinit var drawerlayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_home)



        // Initialize views AFTER setContentView
        drawerlayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        // Setup toolbar
        setSupportActionBar(toolbar)

        // Setup edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerlayout, toolbar, R.string.openDrawer, R.string.closeDrawer

        )

        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation item clicks
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            Log.d("NavClick", "Profile Clicked") // Debug log
            when (item.itemId) {
                R.id.profile -> {
                    Log.d("NavClick", "Profile Clicked") // Debug log
                    val intent = Intent(this, Userprofile::class.java)
                    startActivity(intent)
                    drawerlayout.closeDrawers() // Close drawer after click
                    true
                }
                R.id.logout -> {
                    Log.d("NavClick", "Logout Clicked") // Debug log
                    val intent = Intent(this, Logintesting::class.java)
                    startActivity(intent) // Close the app
                    drawerlayout.closeDrawers()
                    true
                }
                else -> {
                    Log.d("NavClick", "Other Item Clicked") // Debug log
                    false
                }
            }
        }

        // Disable home button in ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    // Handle back button to close the drawer if it's open
    override fun onBackPressed() {
        if (drawerlayout.isDrawerOpen(navigationView)) {
            drawerlayout.closeDrawer(navigationView)
        } else {
            super.onBackPressed()
        }
    }
}
