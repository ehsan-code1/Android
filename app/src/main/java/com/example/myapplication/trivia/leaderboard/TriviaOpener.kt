package com.example.myapplication.trivia.leaderboard

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TriviaOpener(ctx: Context) : SQLiteOpenHelper(ctx, DATABASE_NAME, null, VERSION_NUM) {
    companion object {
        const val DATABASE_NAME : String = "triviaDB"
        const val VERSION_NUM : Int = 1
        const val TABLE_NAME : String = "LEADERBOARD"
        const val COL_USERNAME : String = "USERNAME"
        const val COL_SCORE : String = "SCORE"
        const val COL_DIFFICULTY : String = "DIFFICULTY"
        const val COL_ID : String = "_id"
        val COLUMNS : Array<String> = arrayOf(COL_ID, COL_USERNAME, COL_SCORE, COL_DIFFICULTY)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "$COL_USERNAME text, $COL_SCORE integer, $COL_DIFFICULTY text);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}