package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eText: EditText = findViewById(R.id.search)
        val searchB: Button = findViewById(R.id.searchb)

        searchB.setOnClickListener {
            val searchText :String = eText.text.toString()

            if (searchText!= "") {
                val intent = Intent(this, CarsList::class.java)
                intent.putExtra("search",searchText)
                startActivity(intent)
            }
            else {
                val snack = Snackbar.make(it,"Search Field can not be empty", Snackbar.LENGTH_LONG)
                snack.show()
            }

        }

    }

}