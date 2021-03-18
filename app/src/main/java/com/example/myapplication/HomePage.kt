package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.snackbar.Snackbar


class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eText: EditText = findViewById(R.id.search)
        val searchB: Button = findViewById(R.id.searchb)
        val favB : Button = findViewById(R.id.favB)
        val spref: SharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        eText.setText(spref.getString("SEARCH", ""))

        favB.setOnClickListener {
            val intent = Intent(this, SavedCars::class.java)
            startActivity(intent)
        }

        searchB.setOnClickListener {
            val searchText :String = eText.text.toString()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(it!!.windowToken, 0)

            if (searchText!= "") {
                val intent = Intent(this, CarsList::class.java)
                intent.putExtra("search", searchText)
                startActivity(intent)
            }
            else {
                val snack = Snackbar.make(it, "Search Field can not be empty", Snackbar.LENGTH_LONG)
                snack.show()
            }

        }

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
    override fun onPause() {
        super.onPause()
        val searchT:EditText = findViewById(R.id.search);
        val spref: SharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        val editor : SharedPreferences.Editor = spref.edit();
        editor.putString("SEARCH", searchT.text.toString()).apply();
    }

    override fun onResume() {
        super.onResume()
        val spref: SharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        val eText: EditText = findViewById(R.id.search)

        eText.setText(spref.getString("SEARCH", ""))
    }

}