package com.example.myapplication

import android.content.Intent
import android.view.MenuItem
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


/**
 * A base class that allows subclasses to inherit the same drawer and nav bar
 */
open class BaseActivityWithDrawer: AppCompatActivity() {

    /**
     * This is the setContentView method that all subclasses will call (in their respective onCreate methods).
     * It first inflates its own layout, which holds a FrameLayout. Then it inflates the given [layoutResID] layout,
     * inside of that frame. Then it sets the content view to be the entire, newly inflated base layout.
     * This allows this class to act as a simple container, for all activities that inherit from it.
     *
     * @param layoutResID the subclass layout to inflate inside of the frame
     */
    override fun setContentView(layoutResID: Int) {
        // Inflate the base layout, which holds an empty frame
        val baseLayout = layoutInflater.inflate(R.layout.activity_base_with_drawer, null)
        val subclassLayout = baseLayout.findViewById<FrameLayout>(R.id.base_frame)

        // Inflate the subclass layout in place of the frame, and attach to baseLayout
        layoutInflater.inflate(layoutResID, subclassLayout, true)
        super.setContentView(baseLayout)

        val tb : Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(tb)

        val drawer : DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle  = ActionBarDrawerToggle(this, drawer, tb, R.string.open, R.string.close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_item_trivia -> goToTrivia()
                R.id.nav_item_songster -> goToSongster()
                R.id.nav_item_database -> goToCarDatabase()
            }

            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.trivia_menu_item -> goToTrivia()
            R.id.songster_menu_item -> goToSongster()
            R.id.car_db_menu_item -> goToCarDatabase()
        }
        return true
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