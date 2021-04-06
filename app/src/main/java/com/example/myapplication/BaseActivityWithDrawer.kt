package com.example.myapplication

import android.content.Intent
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.car_database.HomePage
import com.example.myapplication.songstar.SongsterSearch
import com.example.myapplication.trivia.TriviaActivityLanding
import com.google.android.material.navigation.NavigationView

open class BaseActivityWithDrawer: AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        val fullLayout = layoutInflater.inflate(R.layout.activity_base_with_drawer, null)
        val frameLayout = fullLayout.findViewById<FrameLayout>(R.id.base_frame)

        layoutInflater.inflate(layoutResID, frameLayout, true)
        super.setContentView(fullLayout)

        val tb : Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(tb)

        val drawer : DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle  = ActionBarDrawerToggle(this, drawer, tb, R.string.open, R.string.close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_item_one -> goToTrivia()
                R.id.nav_item_two -> goToSongster()
                R.id.nav_item_three -> goToCarDatabase()
            }

            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
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