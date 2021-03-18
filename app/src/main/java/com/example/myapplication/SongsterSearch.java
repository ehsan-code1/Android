package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //upload the search layout
        setContentView(R.layout.songster_search);
        //get id from xml
        userInput=findViewById(R.id.textinput);

        searchButton=findViewById(R.id.searchButton);

        progressBar=findViewById(R.id.progressSearch);

       //initialize SharedPreferences to private mode
        prefs=getSharedPreferences("SavedPref",MODE_PRIVATE);
        //save artist name
        savedArtistName=prefs.getString("ArtistName","");
        userInput.setText(savedArtistName);







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