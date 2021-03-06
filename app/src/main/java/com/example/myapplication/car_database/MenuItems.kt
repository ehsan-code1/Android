package com.example.myapplication.car_database

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myapplication.R

/**
 * MenuItems extending AppCompatActivity which will load an activity and set the text of textviews based on the option item clicked
 * on Previous Activity using a Boolean Variable type, which is passed in Intent Extras.
 */
class MenuItems : AppCompatActivity() {
    /**
     * onCreate Function sets text of Both Textviews based on the boolean passed in the intent
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_options)

        val t1 : TextView = findViewById(R.id.titleoption)
        val t2 : TextView = findViewById(R.id.description)

        val type : Boolean = intent.getBooleanExtra("help",true)

        if (type){
            t1.text = "Help"
            t2.text = getString(R.string.help)
        }
        else {
            t1.text = "About"
            t2.text = getString(R.string.about)
        }


    }
    }

