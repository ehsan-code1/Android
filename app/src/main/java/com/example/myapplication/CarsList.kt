package com.example.myapplication

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class CarsList : AppCompatActivity() {

    val elements = ArrayList<Cars?>()
    private var carModels:String? = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"
   // private var googleModel:String? = "http://www.google.com/search?q="
    //private var autoTraderbuy : String? = "https://www.autotrader.ca/cars/?"
   @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars_list)

        val s = carsQuery(this);
        val progressB : ProgressBar = findViewById(R.id.pBar)
        progressB.visibility = View.VISIBLE
        s.execute(carModels)

        val list : ListView = findViewById(R.id.carList)



        list.setOnItemLongClickListener { _, _, position, id ->
            val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            alertDialog.setTitle("Do you want to delete this?").setMessage("the selected row is $position")
                    .setNeutralButton("Yes") { _, _ ->
                    }
                    .setNegativeButton("No") { _, _ -> }
                    .create().show()
            true
        }

    }

    inner class Cars() {

        private var makeName: String? = null
        private var modelName: String? = null
        private var modelID: String? = null
        private var makeID: String? = null


        constructor(makeID: String?, makeName: String?, modelId: String?, modelName: String?) : this() {
            this.makeID = makeID;
            this.makeName = makeName;
            this.modelID = modelId;
            this.modelName = modelName
        }

        fun returnMake(): String? {
            return this.makeName
        }

        fun returnModel(): String? {
            return this.modelName
        }
        fun returnModelID(): String? {
            return modelID
        }
        fun returnMakeID(): String? {
            return makeID
        }
    }
    @Suppress("DEPRECATION")

    inner class carsQuery(context: Context) : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {
            try {
                var model : String? = null;
                var make : String? = null;
                var modelID : String? = null;
                var makeID : String? = null;

                val modelSeach: HttpURLConnection =
                    URL(params[0] + intent.getStringExtra("search")).openConnection() as HttpURLConnection

                Log.i("response",modelSeach.responseCode.toString())

                val modelTemp: InputStream = modelSeach.inputStream
                val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = false
                val xpp = factory.newPullParser()
                xpp.setInput(modelTemp, "UTF-8")

                var etype = xpp.eventType


                while (etype !== XmlPullParser.END_DOCUMENT) {
                    if (etype === XmlPullParser.START_TAG) {
                        if (xpp.name.equals("ModelsForMake")) {
                            xpp.nextTag()
                            makeID = xpp.nextText()
                            publishProgress(25)
                            xpp.nextTag()
                            make = xpp.nextText()
                            publishProgress(50)
                            xpp.nextTag()
                            modelID = xpp.nextText()
                            publishProgress(75)
                            xpp.nextTag()
                            model = xpp.nextText()
                            val carItems = Cars(makeID, make, modelID, model)
                            elements.add(carItems)
                            publishProgress(100)
                        }
                    }

                    etype = xpp.next() //move to the next xml event and store it in a variable
                }


            } catch (e: Exception) {
                e.printStackTrace();
            }
            return "Done"

        }
        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            var pBar : ProgressBar = findViewById(R.id.pBar)
            pBar.progress = values[0]!!

        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val list : ListView = findViewById(R.id.carList)
            var pBar : ProgressBar = findViewById(R.id.pBar)
            pBar.visibility = View.INVISIBLE
            Toast.makeText(applicationContext, "${elements.size.toString()} results found", Toast.LENGTH_SHORT).show()
            val adapter = MyAdapter(list.context)
            list.adapter = adapter

        }
    }

    inner class MyAdapter(context: Context) : BaseAdapter() {

        private val inflator: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return elements.size
        }

        override fun getItem(position: Int): String? {
            TODO()
        }

        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view : View? = null
            if (view == null) {
                view = this.inflator.inflate(R.layout.caritem, parent, false)
                val model : TextView = view.findViewById(R.id.modelName)
                val make : TextView = view.findViewById(R.id.companyName)
                val modelID : TextView = view.findViewById(R.id.modelID)
                val makeID : TextView = view.findViewById(R.id.makeID)
                make.text = elements[position]?.returnMake()
                model.text = elements[position]?.returnModel()
                modelID.text = elements[position]?.returnModelID()
                makeID.text = elements[position]?.returnMakeID()
            }
            return view;
        }

    }

}