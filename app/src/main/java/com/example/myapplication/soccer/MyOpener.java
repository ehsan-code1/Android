package com.example.myapplication.soccer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyOpener extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "articles.db";
    private static final String ARTICLES_TBL = "articles";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_PUBDATE = "pubdate";
    private static final String COL_URL = "url";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_IMAGEURL = "imageurl";
    private static final String COL_THUMBNAILIMAGEURL = "thumbailimageurl";


    public MyOpener(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB = "CREATE TABLE " + ARTICLES_TBL + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TITLE + " TEXT," + COL_PUBDATE + " TEXT," + COL_URL + " TEXT," +
                COL_DESCRIPTION + " TEXT," + COL_IMAGEURL + " TEXT," + COL_THUMBNAILIMAGEURL + " TEXT" + ")";
        db.execSQL(createDB);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLES_TBL);
        onCreate(db);
    }

    public long addArticles(Article art) {
        ContentValues newRow = new ContentValues();
        newRow.put(COL_TITLE, art.getTitle());
        newRow.put(COL_PUBDATE, art.getPubDate());
        newRow.put(COL_URL, art.getUrl());
        newRow.put(COL_DESCRIPTION, art.getDescription());
        newRow.put(COL_IMAGEURL, art.getImageUrl());
        newRow.put(COL_THUMBNAILIMAGEURL, art.getThumbnailImageUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        long newId = db.insert(ARTICLES_TBL, null, newRow);
        db.close();


        //art.setId(newId);
        return newId;
    }

    public boolean deleteArticles(long artID) {
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();
        int rowsNbr = db.delete(ARTICLES_TBL, COL_ID + " = " + String.valueOf(artID), null);

        if (rowsNbr == 1) {
            result = true;
        }

        return result;
    }

    public void drop() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + ARTICLES_TBL);
        onCreate(db);

    }

    public ArrayList<Article> readAll() {
        ArrayList<Article> articles = new ArrayList<Article>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ARTICLES_TBL, null);

        if(c.moveToFirst()) {
            while(!c.isAfterLast()) {

                // get new msg attributes
                long id = c.getInt(c.getColumnIndex(COL_ID));
                String title = c.getString(c.getColumnIndex(COL_TITLE));
                String pubDate = c.getString(c.getColumnIndex(COL_PUBDATE));
                String url = c.getString(c.getColumnIndex(COL_URL));
                String descr = c.getString(c.getColumnIndex(COL_DESCRIPTION));
                String imgUrl = c.getString(c.getColumnIndex(COL_IMAGEURL));
                String thUrl = c.getString(c.getColumnIndex(COL_THUMBNAILIMAGEURL));

                Article temp = new Article(title, pubDate, url, descr, imgUrl, thUrl, id);
                articles.add(temp);
                // move to next row
                c.moveToNext();
            }
        }

        return articles;
    }
}
