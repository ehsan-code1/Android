package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Database class that extends SQLiteOpenHelper to make a database
 * upon making of its object, a database is created with Columns provided as static variables in Companion object
 * has 5 functions, 3 overridden and 2 functions to add or delete data from Database
 * @param context
 */
class Database(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null, 4) {

    companion object {
        val DATABASENAME = "MessagesDB"
        val VERSION_NUM = 5;
        val TABLENAME = "Cars"
        val COL_ID = "_ID"
        val COL_MODELID = "MODEL_ID"
        val COL_MODEL = "MODEL"
        val COL_MAKEID = "MAKE_ID"
        val COL_MAKE = "MAKE"
    }

    /**
     * onCeate Method that creates a database using execSQL method
     * called whenever an instance of class Database is made
     * @param db
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "Create TABLE $TABLENAME " +
                    "($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_MAKEID TEXT, $COL_MAKE TEXT, $COL_MODELID TEXT, $COL_MODEL TEXT);"
        )
    }

    /**
     * onUpgrade method that drops existing table and makes another one with new version number
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLENAME");
        //Create the new table:
        onCreate(db);    }

    /**
     * onDowngrade method that drops existing table and makes another one with new version number
     */
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS $TABLENAME")
        //Create the new table:
        onCreate(db)

    }

    /**
     * insertData method that takes in required column data and creates a row in the database
     * makes writeableData of current database and uses ContentValues to put passed in arguments in their specific colums
     * @return Long?
     */
    fun insertData(model: String?, modelId: String?, make: String?, makeId: String?) : Long? {
        val database = this.writableDatabase
        val nm = ContentValues();
        nm.put(Database.COL_MAKEID, makeId);
        nm.put(Database.COL_MAKE, make);
        nm.put(Database.COL_MODELID, modelId);
        nm.put(Database.COL_MODEL, model);
        return database.insert(TABLENAME, null, nm)
    }

    /**
     * deleteData method that takes in modelID of item that needs to be deleted from database
     * creates writeableDatabase and calls method delete on it, passing table name, where Clause and Arguments
     * @return Int?
     */
    fun deleteData(modelId: String?) : Int? {
        val database = this.writableDatabase
        val whereArgs = arrayOf(modelId)
        return database.delete(TABLENAME, "$COL_MODELID=?", whereArgs)
    }
}