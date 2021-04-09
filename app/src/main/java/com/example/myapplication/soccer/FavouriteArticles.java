package com.example.myapplication.soccer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;

public class FavouriteArticles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_articles);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourites_actions, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.all:
                //message = "You clicked all";
                finish();
                break;
        }
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }
}
