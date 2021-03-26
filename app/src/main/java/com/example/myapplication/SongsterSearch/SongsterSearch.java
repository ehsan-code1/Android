package com.example.myapplication.SongsterSearch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;

/**
 * com.example.myapplication.SongsterSearch is main page of the application where user can input artist name and click search
 * in the Input box or go to Favorite page List
 * also it has a menu of help icon and About overflow menu items
 * it then send data to next activity
 */

public class SongsterSearch extends AppCompatActivity {
    //define variables
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


     //when FavouriteList Button is clicked start Activity which load FavouriteList Activity
     favButton.setOnClickListener(s->{
         Intent listFav=new Intent(this, FavouriteList.class);
         startActivity(listFav);
     });


       //click search uplaod List of songs
        //if user input is blank through a toast message to inform the user
         searchButton.setOnClickListener(e->

        {

            if(userInput.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.SongsterSearch_EnterValidName, Toast.LENGTH_LONG).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);

                Intent ListViewPage = new Intent(this, SongsterList.class);
                ListViewPage.putExtra(ARTIST_KEYWORD, userInput.getText().toString());

                startActivity(ListViewPage);


            }


        });

    }

    /**
     *
     * @param menu
     * @return boolean
     * create ann optionMenu and inflate the menu Layout
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate the menu items for the action bar
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_songster,menu);
        return true;
    }

    /**
     * select an item from a menu and display alertDialog for the user
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       String song=null;
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
       switch(item.getItemId()){
           case R.id.about:

                       alertDialog.setTitle("Songster app by Nouran Nouh  V1.0").setMessage(R.string.SongsterSearch_AboutMenuItem)
                       .setPositiveButton(R.string.agree,null).create().show();
                       break;
           case R.id.help:

               alertDialog.setMessage(R.string.SongsterSearch_HelpMenuItem)
                       .setPositiveButton(R.string.agree,null).create().show();
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