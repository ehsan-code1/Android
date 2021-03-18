package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "Create TABLE $TABLENAME " +
                    "($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_MAKEID TEXT, $COL_MAKE TEXT, $COL_MODELID TEXT, $COL_MODEL TEXT);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLENAME");
        //Create the new table:
        onCreate(db);    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS $TABLENAME")
        //Create the new table:
        onCreate(db)

    }
    fun insertData(model: String?, modelId: String?, make: String?, makeId: String?) : Long? {
        val database = this.writableDatabase
        val nm = ContentValues();
        nm.put(Database.COL_MAKEID, makeId);
        nm.put(Database.COL_MAKE, make);
        nm.put(Database.COL_MODELID, modelId);
        nm.put(Database.COL_MODEL, model);
        return database.insert(TABLENAME, null, nm)
    }

    fun deleteData(modelId: String?) : Int? {
        val database = this.writableDatabase
        val whereArgs = arrayOf(modelId)
        return database.delete(TABLENAME, "$COL_MODELID=?", whereArgs)
    }
}