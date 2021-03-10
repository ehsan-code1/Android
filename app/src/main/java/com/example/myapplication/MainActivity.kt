package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.trivia.TriviaActivityLanding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.trivia_start).setOnClickListener {
            val goToTriviaIntent = Intent(this, TriviaActivityLanding::class.java)
            startActivity(goToTriviaIntent)
        }
    }
}