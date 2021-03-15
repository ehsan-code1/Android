package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongsterList extends AppCompatActivity {
    Button backButton;
    ArrayList<Artist> artistList=new ArrayList<> ();
    String artistName;
    Artist artist;
    MySongListAdapter artistAdapter;
    ListView artistListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //upload the layout of the song list
        setContentView(R.layout.activity_songster_list);
        //extract the id from xml
        backButton=findViewById(R.id.GoBackButton);
        artistListView=findViewById(R.id.SongView);

        //get Intent from searchpage
        Bundle searchPage=getIntent().getExtras();
        artistName=searchPage.getString(SongsterSearch.ARTIST_KEYWORD);


        //go back to search page
        backButton.setOnClickListener(this::onClick);
        //set list view
        artistAdapter=new MySongListAdapter();
        artist=new Artist(artistName,0,0,"");
         artistList.add(artist);
         artistListView.setAdapter(artistAdapter);
         artistAdapter.notifyDataSetChanged();


    }




    private void onClick(View e) {

        finish();
    }






    /**
     * BaseAdapter used to display the artist view of List view
     */
    private class MySongListAdapter extends BaseAdapter{

        //get ArrayList size
        @Override
        public int getCount() {
            return artistList.size();
        }

        //get item at specific position
        @Override
        public Object getItem(int position) {
            return artistList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return artistList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            //get artist name from arraylist
            Artist artist= (Artist) getItem(position);

            View artistView=inflater.inflate(R.layout.artist_row,parent,false);
            TextView artistText=artistView.findViewById(R.id.artistRow);
            artistText.setText(artist.getArtistName());
            return artistView;
        }
    }
}