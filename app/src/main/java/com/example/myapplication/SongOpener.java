package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public  class SongOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "FavouriteSongsDB";
    protected final static int VERSION_NUM = 1;
    protected final static String TABLE_NAME = "Songster";
    protected final static String COL_ARTISTNAME = "artistName";
    protected final static String COL_ARTISTID = "artistId";
    protected final static String COL_SONGID = "songId";
    protected final static String COL_SONGTITLE = "songTitle";
    protected final static String COL_ID = "_id";
  //  protected final static String COL_IsFavorite = "favouriteSong";

    public SongOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ARTISTNAME + " TEXT,"
                + COL_ARTISTID+ " TEXT,"
                + COL_SONGID + " TEXT,"
                + COL_SONGTITLE + " TEXT);");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //create new table
        onCreate(db);

    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //Create the new table:
        onCreate(db);
    }

    protected void deleteFromDB(String SongId){
        SQLiteDatabase write=getWritableDatabase();
          write.delete(SongOpener.TABLE_NAME,SongOpener.COL_SONGID+ "= ?",new String[]{SongId});

    }


}
