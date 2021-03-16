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
        val pb : ProgressBar = findViewById(R.id.progressBar)

        pb.visibility = View.INVISIBLE

        searchB.setOnClickListener {
            val searchText :String = eText.text.toString()

            if (searchText!= "") {
                val intent = Intent(this, CarsList::class.java)
                intent.putExtra("search",searchText)
                pb.visibility = View.VISIBLE
                Thread.sleep(500)
                pb.progress = Random.nextInt(100)
                startActivity(intent)
            }
            else {
                val snack = Snackbar.make(it,"Search Field can not be empty", Snackbar.LENGTH_LONG)
                snack.show()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        val pb : ProgressBar = findViewById(R.id.progressBar)
        pb.visibility = View.INVISIBLE

    }
}