package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

/**
 * class HomePage extending AppCompatActivity, this will be our starting activity. Having an edit text for search functionality
 * A button that takes you to SavedCars Activity, and optionsBar to load optionbar with help and about
 */
class HomePage : AppCompatActivity() {
    /**
     * onCreate function which sets text into searchbar from shared preferences, and launches CarsList activity when search button is
     * clicked, sending the text input as intent extras. Shows snackbar if searchtext is empty, prompting user to enter something
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

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
    /**
     * Adds helpmenu menu item to the optionsMenu if its present
     * @param menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.helpmenu, menu)
        return true
    }
    /**
     * onOptionsItemSelected using Switch to execute certain Event based on item of menu clicked
     * if help is clicked, an alertdialog with help is shown, and instructions on to click more to get extra help
     * which launches activity and puts a boolean variable which is used to change text of the activity user is sent to
     * if its true, help will be shown, otherwise about will be shown
     * @param item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.help -> {
                val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
                alertDialog.setTitle("Enter Car Manufacturer's Name and click go "
                ).setMessage("To view your saved cars, click on View Saved Cars. \nFor More help click More")
                        .setPositiveButton("More") { _, _ ->
                            val intent = Intent(this, MenuItems::class.java)
                            intent.putExtra("help",true)
                            startActivity(intent)
                        }.setNeutralButton("Close Help") {_,_ ->

                        }
                alertDialog.show()
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

    /**
     * During onPause, Saves the text input in the search bar into SharedPreferences
     */
    override fun onPause() {
        super.onPause()
        val searchT:EditText = findViewById(R.id.search);
        val spref: SharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        val editor : SharedPreferences.Editor = spref.edit();
        editor.putString("SEARCH", searchT.text.toString()).apply();
    }

    /**
     * onResume retrieves the text from sharedPreferences and sets the text of searchbar to previoulsy entered query
     */
    override fun onResume() {
        super.onResume()
        val spref: SharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        val eText: EditText = findViewById(R.id.search)

        eText.setText(spref.getString("SEARCH", ""))
    }

}