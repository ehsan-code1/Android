package com.example.myapplication

import com.example.myapplication.SongsterSearch.SongsterSearch
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.car_database.HomePage
import com.example.myapplication.songstar.SongsterSearch
import com.example.myapplication.trivia.TriviaActivityLanding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val carDb : Button = findViewById(R.id.car)
        val trivia : Button = findViewById(R.id.trivia)
        val song : Button = findViewById(R.id.sstar)
        val soccer: Button = findViewById(R.id.ss)

        carDb.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
        trivia.setOnClickListener {
            val intent = Intent(this, TriviaActivityLanding::class.java)
            startActivity(intent)
        }
        song.setOnClickListener {
            val intent = Intent(this, SongsterSearch::class.java)
            startActivity(intent)
        }



    }
}