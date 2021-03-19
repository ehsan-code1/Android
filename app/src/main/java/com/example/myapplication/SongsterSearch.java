package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SongsterSearch extends AppCompatActivity {
    static final String ARTIST_KEYWORD="ARTISTNAME";
    private Button searchButton;
    private EditText userInput;
    ProgressBar progressBar;
    public static final String ACTIVITY_NAME = "SEARCH_ACTIVITY";
    public SharedPreferences prefs=null;
    private  String savedArtistName;
    private Button favButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //upload the search layout
        setContentView(R.layout.songster_search);
        //get id from xml
        userInput=findViewById(R.id.textinput);

        searchButton=findViewById(R.id.searchButton);

        progressBar=findViewById(R.id.progressSearch);
        favButton=findViewById(R.id.favouriteSongListButton);

       //initialize SharedPreferences to private mode
        prefs=getSharedPreferences("SavedPref",MODE_PRIVATE);
        //save artist name
        savedArtistName=prefs.getString("ArtistName","");
        userInput.setText(savedArtistName);




     favButton.setOnClickListener(s->{
         Intent listFav=new Intent(this,FavouriteList.class);
         startActivity(listFav);
     });


       //click search uplaod List of songs
        //if user input is blank through a toast message to inform the user
         searchButton.setOnClickListener(e->

        {

            if(userInput.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Must Enter Artist or band Name", Toast.LENGTH_LONG).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);

                Intent ListViewPage = new Intent(this, SongsterList.class);
                ListViewPage.putExtra(ARTIST_KEYWORD, userInput.getText().toString());

                startActivity(ListViewPage);


            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate the menu items for the action bar
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_songster,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       String song=null;
       switch(item.getItemId()){
           case R.id.about:
               AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                       alertDialog.setTitle("Songster app created by Nouran Nouh  V1.0").setMessage("search your favorite Artist Songs to know more about the app click help icon")
                       .setPositiveButton("Ok",null).create().show();
                       break;
           case R.id.help:
               AlertDialog.Builder alertHelp=new AlertDialog.Builder(this);
               alertHelp.setMessage("Enter Artist or band name that you want to search for Artist Name\nClick Search button or view your favourite Artist List")
                .setNeutralButton("ok",null).create().show();
               break;

       }

       return true;

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME,"in the function onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME,"in the function onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME,"in the function onDestory");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit=prefs.edit();
        savedArtistName= userInput.getText().toString();

        edit.putString("ArtistName", savedArtistName);

        edit.commit();

        Log.e(ACTIVITY_NAME,"in the function onPause");


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);

        Log.e(ACTIVITY_NAME,"in the function onResume");
    }
}