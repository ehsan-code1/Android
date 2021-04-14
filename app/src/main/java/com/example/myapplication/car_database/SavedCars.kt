package com.example.myapplication.car_database

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R


/**
 * SavedCars class that loads all the cars which were saved from the database, and loads them in the listView
 * elements array that stores each car's information
 */
class SavedCars : AppCompatActivity() {

    val elements = ArrayList<Cars?>()
    private val db = Database(this)
    private var adapter : MyAdapter? = null
    private var isTabl : Boolean? = null;
    private var dFrag = CarOptions()

    /**
     * onCreate which loads activity saved_cars
     * Determines if device being used is a tablet using isTabl
     * Sets adapter to ListView
     * If the array elements is empty, shows a toast with text that no cars are saved
     * pullToRefresh will empty the elements array, load data again and notify the adapter for Data Change
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_cars)
        val frame : FrameLayout? = findViewById(R.id.frame)
        isTabl = frame != null
        loadDataFromDatabase()
        val list : ListView = findViewById(R.id.carList)
        adapter = MyAdapter(list.context)
        list.adapter = adapter
        if (elements.isEmpty()) {
            Toast.makeText(applicationContext, "No Favourites Added", Toast.LENGTH_LONG).show()
        }
            val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
            pullToRefresh.setOnRefreshListener {
                elements.clear()
                loadDataFromDatabase()
                adapter?.notifyDataSetChanged()
                pullToRefresh.isRefreshing = false
            }


    }


    /**
     * loadDataFromDatabase function which gets all the data from the database, and stores in the array elements
     * makes an array of required columns data
     * Stores the Cursor which is obtained from running query on the writeable database in the variable results
     * Get columnIndex of each column
     * While loop that gets data of each column and stores in variables
     * Makes a Car object and puts it in the elements array
     */
    private fun loadDataFromDatabase() {

        val columns = arrayOf(
                Database.COL_MAKEID,
                Database.COL_MAKE,
                Database.COL_MODELID,
                Database.COL_MODEL
        )

        val results: Cursor = db.writableDatabase.query(
            false,
                Database.TABLENAME,
            columns,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val makeID: Int = results.getColumnIndex(Database.COL_MAKEID)
        val make: Int = results.getColumnIndex(Database.COL_MAKE)
        val modelID: Int = results.getColumnIndex(Database.COL_MODELID)
        val model: Int = results.getColumnIndex(Database.COL_MODEL)


        results.moveToPosition(-1);
        while (results.moveToNext()) {
            val makeID: String = results.getString(makeID)
            val make: String = results.getString(make)
            val modelID: String = results.getString(modelID)
            val model: String = results.getString(model)

            val cars = Cars(makeID, make, modelID, model)
            elements.add((cars))
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
                val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
                    this
                )
                alertDialog.setTitle(
                    "Enter Car Manufacturer's Name and click go "
                )
                    .setMessage("To view your saved cars, click on View Saved Cars. \nFor More help click More")
                    .setPositiveButton("More") { _, _ ->
                        val intent = Intent(this, MenuItems::class.java)
                        intent.putExtra("help", true)
                        startActivity(intent)
                    }.setNeutralButton("Close Help") { _, _ ->

                    }
                alertDialog.show()
                true
            }
            R.id.about -> {
                val intent = Intent(this, MenuItems::class.java)
                intent.putExtra("help", false)
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
         * listview with information based on the item it was clocked on, and a boolean with value true  so a button
         * with removeFromDatabase will be shown which will remove the car from database
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
                    dataToPass.putBoolean("Saved", true)
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

    /**
     * onResume function so when you return from CarOptions activity, the list is updated and the car removed is not in the listview
     */
    override fun onResume() {
        super.onResume()
        elements.clear()
        loadDataFromDatabase()
        adapter?.notifyDataSetChanged()
        if (elements.isEmpty()) {
            Toast.makeText(applicationContext, "No Favourites Added", Toast.LENGTH_LONG).show()
        }
    }
}