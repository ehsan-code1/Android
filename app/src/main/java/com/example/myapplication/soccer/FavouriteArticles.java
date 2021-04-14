package com.example.myapplication.soccer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.R.layout.article_layout;
import static com.example.myapplication.R.layout.favourite_article_layout;

public class FavouriteArticles extends AppCompatActivity {

    private Activity context;
    ArrayList<Article> articles;
    ListView articleBox;
    ArticleList artAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_articles);

        //title of layout
        setTitle(getResources().getText(R.string.favourite_Articles_Title));

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        articleBox = (ListView) findViewById(R.id.articleBox);

        //Article(String title, String pubDate, String url, String description, String imageUrl, String thumbnailImageUrl)


        /*
        articles = new ArrayList<Article>();
        articles.add(sampleArticle1);
        */

        MyOpener db = new MyOpener(this);

        /* TEST: add messages to the db */
        //db.addArticles(sampleArticle1);


        articles = db.readAll();


    }

    @Override
    protected void onStart() {
        super.onStart();
        artAdapter = new ArticleList(FavouriteArticles.this, articles);
        articleBox.setAdapter(artAdapter);
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


        //Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.all:

                finish();
                break;
        }

        return true;
    }
}
