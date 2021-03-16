package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class CarsList : AppCompatActivity() {

    val elements = ArrayList<Cars?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars_list)
        val carsAmount : Int = Random.nextInt(1000)
        Toast.makeText(applicationContext,"$carsAmount results found",Toast.LENGTH_SHORT).show()
        val list : ListView = findViewById(R.id.carList)

        val adapter = myAdapter(this)

        list.adapter = adapter
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
        private var modeID: Int? = 0
        private var makeID: Int? = 0


        constructor(makeName: String?, modelName: String?, modeId: Int?, makeID : Int?) : this() {
            this.makeName = makeName;
            this.modelName = modelName;
            this.modeID = modeId;
            this.makeID = makeID
        }

        fun returnMake(): String? {
            return this.makeName
        }

        fun returnModel(): String? {
            return this.modelName
        }
        fun returnModelID(): Int? {
            return modeID
        }
        fun returnMakeID(): Int? {
            return makeID
        }
    }

    inner class myAdapter(context:Context) : BaseAdapter() {

        private val inflator: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return 20
        }

        override fun getItem(position: Int): Any {
            TODO("Not yet implemented")
        }

        override fun getItemId(position: Int): Long {
            return 1;
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view : View? = null
            if (view == null) {
                view = this.inflator.inflate(R.layout.caritem,parent,false)
                val make : TextView = view.findViewById(R.id.companyName)
                make.text = intent.getStringExtra("search")
            }
            return view;
        }

    }

}