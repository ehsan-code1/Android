package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

/**
 * EmptyActivity extending AppCompatActivity which inflates the fragment CarOptions in the Frame and the data it received in intent
 * is sent as arguments to the fragment
 * Used for Mobile Phones only as frame and listview on same activity is not feasible for smaller dp displays
 */
class EmptyActivity : AppCompatActivity() {
    /**
     * onCreate Function which makes instance of fragment CarOptions and the intent extra bundle it received is passed to it as
     * arguments. The fragment is then inflated in the frame using supportFragmentManager
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        val dataToPass = intent.extras
        val dFrag = CarOptions()

        dFrag.arguments = dataToPass

        supportFragmentManager.beginTransaction().replace(R.id.frame,dFrag).commit()
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