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

/**
 * CarsList class that loads Model of Manufacturer that was passed in the search query from previous activity, i.e. HomePage
 * Two inner classes , carsQuery that extends AsyncTask to get data from The Url passed in, and load ArrayList elements
 * MyAdapter that extends BaseAdapter to make ListView Functional
 * Has isTabl Boolean variable that will be false if device being used is a phone, or true if its a tablet
 * Based on isTabl, either an empty activity will be launched and the fragment CarOptions will be inflated there
 * and filled with data from the specific item selected, or the fragment will be inflated to the right of ListView in the frameview
 */
class CarsList : AppCompatActivity() {

    companion object {
        val elements = ArrayList<Cars?>()
    }

    private var isTabl : Boolean? = null;
    private var carModels:String? = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"
    private var dFrag = CarOptions()

    /**
     * onCreate Function that sets value of isTabl based on the value of frame, if null then its not a tablet
     * Query is executed using excute function on object of carsQuery
     * Progress bar is visible at first to show progress of the AsyncTask
     */
   @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars_list)
        val frame : FrameLayout? = findViewById(R.id.frame)
        isTabl = frame != null
        val s = carsQuery(this);
        val progressB : ProgressBar = findViewById(R.id.pBar)
        progressB.visibility = View.VISIBLE
        s.execute(carModels)


    }

    @Suppress("DEPRECATION")

    /**
     * inner class carsQuery that extends AsyncTask
     * has methods doInBackground that saves data in the array elements
     * onProgressUpdate updates the progress bar
     * onPostExecute Loads data in the Listview
     * @param context
     */
    inner class carsQuery(context: Context) : AsyncTask<String, Int, String>() {
        /**
         * doInBackground gets HttpUrlCollection using function openConnection on the url passed in the arguments
         * and append the name of manufacturer the user entered in the search and InputStream is obtained
         * XmlPullParserFactory is made and the InputStream is passed in the setInput Function
         * while loop which runs until the eventType is not .END_DOCUMENT
         * If statement that runs on starting tags, another if statement nested inside that looks for tag ModelsForMake
         * and saves the data in variables based on the sequence in the xml. upon each save, progress is published using fucntion
         * publishProgress. nextTag method is used to get the text of next Tag
         * each data of ModelsForMake is stored in array elements
         * @param params
         * @return String
         */
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

        /**
         * onProgressUpdate gets data from the publishProgress method and updates the progress bar based on argument of each time
         * the method is called
         */
        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            var pBar : ProgressBar = findViewById(R.id.pBar)
            pBar.progress = values[0]!!

        }

        /**
         * Upon completion of doInBackground, loads data into the listview by setting adapter to it
         * Sets the progress bar to invisible as its completed
         * Makes a toast showing amount of elements found
         */
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

    /**
     * Adds helpmenu menu item to the optionsMenu if its present
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
     * inner class MyAdapter that extends BaseAdapter
     * has 4 methods that populate the listView
     * @param context
     */
    inner class MyAdapter(context: Context) : BaseAdapter() {

        private val inflator: LayoutInflater = LayoutInflater.from(context)

        /**
         * Returns Size of elements array, determining the quantity of items in the ListView
         * @return Int
         */
        override fun getCount(): Int {
            return elements.size
        }

        /**
         * returns specific toString of elements array
         * @param position
         * @return String?
         */
        override fun getItem(position: Int): String? {
            return elements[position].toString()
        }
        /**
         * returns specific id of elements array
         * @return Long
         * @param position
         */
        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        /**
         * getView method that loads the caritem.xml in the listview and fills in data from the elements array
         * on each item is a onClickListener which if is a tablet, inflates the fragment on the right side of the
         * listview with information based on the item it was clocked on, and a boolean with value false so a button
         * with savetodatabase will be shown which will save the car to database
         * @param position
         * @param convertView
         * @param parent
         * @return View?
         */
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view : View? = null
            if (view == null) {
                view = this.inflator.inflate(R.layout.caritem, parent, false)
                val model : TextView = view.findViewById(R.id.modelName)
                val make : TextView = view.findViewById(R.id.companyName)
                val modelID : TextView = view.findViewById(R.id.modelID)
                val makeID : TextView = view.findViewById(R.id.makeID)

                view?.setOnClickListener{
                    val dataToPass = Bundle()
                    dataToPass.putString("MakeID", elements[position]?.returnMakeID())
                    dataToPass.putString("ModelID", elements[position]?.returnModelID())
                    dataToPass.putString("MakeName", elements[position]?.returnMake())
                    dataToPass.putString("ModelName", elements[position]?.returnModel())
                    dataToPass.putBoolean("Saved",false)
                    if (isTabl == true) {
                        dFrag = CarOptions()
                        dFrag.arguments = dataToPass
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frame, dFrag)
                            .commit();
                    }
                    else {
                        val phoneIntent = Intent(it.context, EmptyActivity::class.java)
                        phoneIntent.putExtras(dataToPass)
                        startActivity(phoneIntent)
                    }
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