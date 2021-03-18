package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class CarsList : AppCompatActivity() {

    val elements = ArrayList<Cars?>()

    private var isTabl : Boolean? = null;
    private var carModels:String? = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"

   @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars_list)

        val s = carsQuery(this);
        val progressB : ProgressBar = findViewById(R.id.pBar)
        progressB.visibility = View.VISIBLE
        s.execute(carModels)


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

                modelID.setOnClickListener{
                    val dataToPass = Bundle()
                    dataToPass.putString("MakeID", elements[position]?.returnMakeID())
                    dataToPass.putString("ModelID", elements[position]?.returnModelID())
                    dataToPass.putString("MakeName", elements[position]?.returnMake())
                    dataToPass.putString("ModelName", elements[position]?.returnModel())
                    dataToPass.putBoolean("Saved",false)
                    val phoneIntent = Intent(it.context, EmptyActivity::class.java)
                    phoneIntent.putExtras(dataToPass)
                    startActivity(phoneIntent)
                }



                make.text = elements[position]?.returnMake()
                model.text = elements[position]?.returnModel()
                modelID.text = elements[position]?.returnModelID()
                makeID.text = elements[position]?.returnMakeID()
            }
            return view;
        }

    }

}