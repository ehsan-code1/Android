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
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


class CarOptions : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        val data: Bundle? = arguments;

        val isSaved: Boolean? = data?.getBoolean("Saved")
        val result: View = inflater.inflate(R.layout.fragment_cars, container, false)

        val db = Database(result.context);

        val model : TextView = result.findViewById(R.id.modelName)
        val modelID : TextView = result.findViewById(R.id.modelID)
        val make : TextView = result.findViewById(R.id.companyName)
        val makeID : TextView = result.findViewById(R.id.makeID)
        val buyB : Button = result.findViewById(R.id.buyB)
        val viewD : Button = result.findViewById(R.id.detailsB)
        val saveB : Button = result.findViewById(R.id.saveB)


        model.text = data?.getString("ModelName").toString()
        make.text = data?.getString("MakeName").toString()
        modelID.text = data?.getString("ModelID").toString()
        makeID.text = data?.getString("MakeID").toString()

        viewD.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.google.com/search?q=" + data?.getString("MakeName").toString() + "+" + data?.getString("ModelName").toString()))
            startActivity(browserIntent);
        }


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


        if (isSaved == true) {
            saveB.text = "Remove From DataBase"
            saveB.setOnClickListener {
                db.deleteData(data?.getString("ModelID"))
                val snackbar = Snackbar.make(it, "Car Removed From Database",
                    Snackbar.LENGTH_LONG)
                snackbar.setAction("Go Back to Favorites"
                ) {
                    val intent = Intent(it.context, SavedCars::class.java)
                    startActivity(intent)
                }
                snackbar.show()
            }

        }


        else {


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

    private fun getModels (context: Context) : ArrayList<String> {
        val db = Database(context);

        val model = ArrayList<String>()

        val columns = arrayOf<String>(Database.COL_MODELID)

        val results: Cursor = db.writableDatabase.query(false, Database.TABLENAME, columns, null, null, null, null, null, null)

        val modelIndex: Int = results.getColumnIndex(Database.COL_MODELID)

      //  results.moveToPosition(-1);
        while (results.moveToNext()) {
            model.add(results.getString(modelIndex))
        }
        return model
    }


}