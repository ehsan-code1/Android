package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class SavedCars : AppCompatActivity() {

    val elements = ArrayList<Cars?>()
    private val db = Database(this);
    private var adapter : MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_cars)
        loadDataFromDatabase()
        val list : ListView = findViewById(R.id.carList)
        adapter = MyAdapter(list.context)
        list.adapter = adapter
        if (elements.isEmpty()) {
            Toast.makeText(applicationContext, "No Favourites Added", Toast.LENGTH_SHORT).show()
        }
    }



    private fun loadDataFromDatabase() {

        val columns = arrayOf(Database.COL_MAKEID, Database.COL_MAKE, Database.COL_MODELID,Database.COL_MODEL)

        val results: Cursor = db.writableDatabase.query(false, Database.TABLENAME, columns, null, null, null, null, null, null)

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

            val cars = Cars(makeID,make,modelID,model)
            elements.add((cars))
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
                    dataToPass.putBoolean("Saved",true)

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

    override fun onResume() {
        super.onResume()
        elements.clear()
        loadDataFromDatabase()
        adapter?.notifyDataSetChanged()

    }
}