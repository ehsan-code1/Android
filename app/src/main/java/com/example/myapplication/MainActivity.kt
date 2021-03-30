package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.car_database.HomePage
import com.example.myapplication.songstar.SongsterSearch
import com.example.myapplication.trivia.TriviaActivityLanding
import com.google.android.material.internal.NavigationMenuItemView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        val carDb : Button = findViewById(R.id.car)
//        val trivia : Button = findViewById(R.id.trivia)
//        val song : Button = findViewById(R.id.sstar)
//        val soccer: Button = findViewById(R.id.ss)
        val tb : Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(tb)

        val drawer : DrawerLayout = findViewById(R.id.drawer_layout)

        val toggle  = ActionBarDrawerToggle(this,drawer,tb,R.string.app_name,R.string.go_to_chat)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

//        carDb.setOnClickListener {
//            val intent = Intent(this, HomePage::class.java)
//            startActivity(intent)
//        }
//        trivia.setOnClickListener {
//            val intent = Intent(this, TriviaActivityLanding::class.java)
//            startActivity(intent)
//        }
//        song.setOnClickListener {
//            val intent = Intent(this, SongsterSearch::class.java)
//            startActivity(intent)
//        }



    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.helpmenu, menu)
        val it1 : NavigationMenuItemView = findViewById(R.id.nav_item_one)
        val it2 : NavigationMenuItemView = findViewById(R.id.nav_item_two)
        val it3 : NavigationMenuItemView = findViewById(R.id.nav_item_three)

        it1.setOnClickListener {
            val it = Intent(this,TriviaActivityLanding::class.java)
            startActivity(it)

        }
        it2.setOnClickListener {
            val it = Intent(this,SongsterSearch::class.java)
            startActivity(it)

        }
        it3.setOnClickListener {
            val it = Intent(this,HomePage::class.java)
            startActivity(it)        }
        return true
    }

}