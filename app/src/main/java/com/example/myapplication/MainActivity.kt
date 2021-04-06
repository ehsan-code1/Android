package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.car_database.HomePage
import com.example.myapplication.songstar.SongsterSearch
import com.example.myapplication.trivia.TriviaActivityLanding
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivityWithDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.car).setOnClickListener{ goToCarDatabase() }
        findViewById<Button>(R.id.trivia).setOnClickListener{ goToTrivia() }
        findViewById<Button>(R.id.sstar).setOnClickListener{ goToSongster() }
    }

    private fun goToTrivia() {
        val goToTrivia = Intent(this, TriviaActivityLanding::class.java)
        startActivity(goToTrivia)
    }

    private fun goToSongster() {
        val goToSongster = Intent(this, SongsterSearch::class.java)
        startActivity(goToSongster)
    }

    private fun goToCarDatabase() {
        val goToCarDatabase = Intent(this, HomePage::class.java)
        startActivity(goToCarDatabase)
    }
}