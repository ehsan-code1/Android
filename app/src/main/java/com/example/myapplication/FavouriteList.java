package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavouriteList extends AppCompatActivity {
    ArrayList<Artist> favList=new ArrayList<>();
    MySongListAdapter myAdapter;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        ListView listview=findViewById(R.id.Listid);
        loadDataFromDB();
        myAdapter=new MySongListAdapter();
        listview.setAdapter(myAdapter);

    }


    private void loadDataFromDB(){
        //clear current arraylist

        SongOpener songDB=new SongOpener(this);
        db=songDB.getWritableDatabase();
        String [] columns={ SongOpener.COL_ID, SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID, SongOpener.COL_SONGID, SongOpener.COL_SONGTITLE};
        Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        int idColIndex = results.getColumnIndex( SongOpener.COL_ID);
        int artistNameColIndex = results.getColumnIndex( SongOpener.COL_ARTISTNAME);
        int artistIdColIndex = results.getColumnIndex(SongOpener.COL_ARTISTID);
        int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
        int songTitleIndex = results.getColumnIndex(SongOpener.COL_SONGTITLE);
        // int isFavouriteColIndex = results.getColumnIndex(SongOpener.COL_IsFavorite);
        while(results.moveToNext()){
            long id=results.getLong(idColIndex);
            String artistName=results.getString(artistNameColIndex);
            String artistId=results.getString(artistIdColIndex);
            String songId=results.getString(songIdColIndex);
            String songTitle=results.getString(songTitleIndex);
            //String isFavourite=results.getString(isFavouriteColIndex);
            favList.add(new Artist(id,artistName,artistId,songId,songTitle));

        }

    }



    private class MySongListAdapter extends BaseAdapter {

        //get ArrayList size
        @Override
        public int getCount() {
            return favList.size();
        }

        //get item at specific position
        @Override
        public Artist getItem(int position) {
            return favList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            //get artist name from arraylist
            Artist artist= (Artist) getItem(position);

            View artistView=inflater.inflate(R.layout.artist_row,parent,false);
            TextView songId=artistView.findViewById(R.id.SongIdRow);
            TextView title=artistView.findViewById(R.id.artistTitleRow);
            TextView artistId=artistView.findViewById(R.id.artistIdRow);
            TextView artistName=artistView.findViewById(R.id.artistName);
            songId.setText(artist.getSongId());
            title.setText(artist.getSongTitle());
            artistId.setText(artist.getArtistId());
            artistName.setText(artist.getArtistName());


            return artistView;
        }
    }
}