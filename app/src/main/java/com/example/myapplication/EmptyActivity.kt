package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class EmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        val dataToPass = intent.extras
        val dFrag = CarOptions()

        dFrag.arguments = dataToPass

        supportFragmentManager.beginTransaction().replace(R.id.frame,dFrag).commit()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.helpmenu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.help -> {
                val intent = Intent(this, MenuItems::class.java)
                intent.putExtra("help",true)
                startActivity(intent)
                true
            }
            R.id.about ->{
                val intent = Intent(this, MenuItems::class.java)
                intent.putExtra("help",false)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}