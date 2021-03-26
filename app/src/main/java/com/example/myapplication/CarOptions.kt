package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 *  CarOptions Class extends Fragment to inflate fragment fragment_cars.xml
 *  Takes arguments in form of bundle from CarsList or SavedCars Activities and populates the Fragment
 *  Has 3 buttons, Buy,View Details and Save. First two having intents that open Browser with url based on
 *  Information on the Fragment. Save Button saves Make,MakeID,Model and ModelID in a Database
 */
class CarOptions : Fragment() {


    // Will be set later to the ParentActivity
    private var parentActivity: AppCompatActivity? = null

    /**
     * onCreateView Function which receives data from either CarsList, or SavedCars in form of Bundle
     * Bundle containing make,makeid,model,modelID and a Boolean which is used to determine whether previous activity
     * was CarsList or SavedCars.
     * If CarsList, then Save Button will be shown, else Remove Button will be shown
     * Instnace of DB to either insert into Database if Save Button is pressed, or Remove From database if Remove Button is Pressed
     * ViewDetails button will launch Default Browser, and parse a custom google URL based on model and make name to show car details
     * Buy Button will prompt an alert Dialog, upon pressing yes, an autotrader page for the specific car model will be opened

     */
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        // Data to pass
        val data: Bundle? = arguments;
        // to determine if previous activity was from Search or Favourite
        val isSaved: Boolean? = data?.getBoolean("Saved")
        // get fragment View
        val result: View = inflater.inflate(R.layout.fragment_cars, container, false)
        // Database Obkect
        val db = Database(result.context);
        // Textviews and Buttons
        val model : TextView = result.findViewById(R.id.modelName)
        val modelID : TextView = result.findViewById(R.id.modelID)
        val make : TextView = result.findViewById(R.id.companyName)
        val makeID : TextView = result.findViewById(R.id.makeID)
        val buyB : Button = result.findViewById(R.id.buyB)
        val viewD : Button = result.findViewById(R.id.detailsB)
        val saveB : Button = result.findViewById(R.id.saveB)

        // Assigning data passed in argument to textviews
        model.text = data?.getString("ModelName").toString()
        make.text = data?.getString("MakeName").toString()
        modelID.text = data?.getString("ModelID").toString()
        makeID.text = data?.getString("MakeID").toString()
        // click Listener to search for specific model on google
        viewD.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.google.com/search?q=" + data?.getString("MakeName").toString() + "+" + data?.getString("ModelName").toString()))
            startActivity(browserIntent);
        }

        // click Listener to search for specific model on autoTrader
        buyB.setOnClickListener {
            val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(it.context)
            alertDialog.setTitle("Do you want to Open AutoTrader?").setMessage("You will be redirected to AutoTrader Wesbsite")
                .setNeutralButton("Yes") { _, _ ->
                    val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.autotrader.ca/cars/?mdl=" + data?.getString("ModelName").toString() + "&make=" + data?.getString("MakeName").toString() + "&loc=K2G1V8"))
                    startActivity(browserIntent);
                }
                .setNegativeButton("No") { _, _ -> }
                .create().show()

        }

        // Determine if the car is already saved, if so will instead of having a save button, there will be a delete button
        if (isSaved == true) {
            saveB.text = "Remove From DataBase"
            saveB.setOnClickListener {
                db.deleteData(data?.getString("ModelID"))
                val snackbar = Snackbar.make(it, "Car Removed From Database", Snackbar.LENGTH_LONG)
                parentActivity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                snackbar.show()
            }

        }


        else {
            // since car is not saved, We will add it to database
            saveB.setOnClickListener {
                val model: ArrayList<String> = getModels(result.context)

                if (!model.contains(data?.getString("ModelID").toString())) {
                    db.insertData(
                            data?.getString("ModelName"),
                            data?.getString("ModelID"),
                            data?.getString("MakeName"),
                            data?.getString("MakeID")
                    )
                } else
                    Toast.makeText(result.context, "Car already Saved", Toast.LENGTH_SHORT).show()
                saveB.text = "Saved"
                saveB.setBackgroundColor(Color.parseColor("#97d992"))

            }
        }
        return result
    }


    /**
     * Helper Method that runs query on database and returns already saved cars in an ArrayList
     * runs query on writableDatabase having just the ModelID,
     * Can be used to avoid double entries or verify if a car is in database
     * @param context
     * @return ArrayList<String>
     */
    private fun getModels(context: Context) : ArrayList<String> {
        val db = Database(context)

        val model = ArrayList<String>()

        val columns = arrayOf(Database.COL_MODELID)

        val results: Cursor = db.writableDatabase.query(false, Database.TABLENAME, columns, null, null, null, null, null, null)

        val modelIndex: Int = results.getColumnIndex(Database.COL_MODELID)

      //  results.moveToPosition(-1);
        while (results.moveToNext()) {
            model.add(results.getString(modelIndex))
        }
        results.close()
        return model
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as AppCompatActivity
    }


}