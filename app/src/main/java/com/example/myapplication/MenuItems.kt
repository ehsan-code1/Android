package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

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
            t2.text = "Enter Name of Manufacturer in Search Field." +
                    " Press Go to Load All Models from That Manufacturer." +
                    " Click on Model Id Of Car, Bottom Right, to View Options for Car." +
                    " Click Save to Add Car To Database. " +
                    " On HomePage, Click On Favourite Cars to see your cars you have Saved." +
                    " Click On Buy to open Auto trader Link for that Model." +
                    " Click on Details to Google Search that model. "
        }
        else {
            t1.text = "About"
            t2.text = "Architect =" +
                    " Muhammad Ehsan Khan." +
                    " Version 1.0, Final Stable Release." +
                    " Project initiated on March 15th 2021." +
                    " Project Finalized on March 17th 2021."
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
}