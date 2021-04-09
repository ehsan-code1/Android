package com.example.myapplication.soccer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SoccerLanding extends AppCompatActivity {

    ArrayList<Article> articles;
    ListView articleBox;
    ArticleList artAdapter;

    float ratingValue;
    SharedPreferences shared;
    String PREF_NAME = "rating_file";
    String keyChannel = "rating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_landing);

        articleBox = (ListView) findViewById(R.id.articleBox);

        //Article(String title, String pubDate, String url, String description, String imageUrl, String thumbnailImageUrl)
        Article sampleArticle1 = new Article("title1", "date1", "url1", "descr1", "http://images.daznservices.com/di/library/GOAL/aa/51/victor-osimhen-simy-napolicrotone_ajvhpap63mue16yw1my4a845h.jpeg?t=-1684566856", "http://images.daznservices.com/di/library/GOAL/aa/51/victor-osimhen-simy-napolicrotone_ajvhpap63mue16yw1my4a845h.jpeg?t=-1684566856");
        Article sampleArticle2 = new Article("title2", "date2", "url2", "descr2", "http://images.daznservices.com/di/library/GOAL/aa/51/victor-osimhen-simy-napolicrotone_ajvhpap63mue16yw1my4a845h.jpeg?t=-1684566856", "http://images.daznservices.com/di/library/GOAL/aa/51/victor-osimhen-simy-napolicrotone_ajvhpap63mue16yw1my4a845h.jpeg?t=-1684566856");
        Article sampleArticle3 = new Article("title3", "date3", "url3", "descr3", "http://images.daznservices.com/di/library/GOAL/aa/51/victor-osimhen-simy-napolicrotone_ajvhpap63mue16yw1my4a845h.jpeg?t=-1684566856", "http://images.daznservices.com/di/library/GOAL/aa/51/victor-osimhen-simy-napolicrotone_ajvhpap63mue16yw1my4a845h.jpeg?t=-1684566856");

        articles = new ArrayList<Article>();

        articles.add(sampleArticle1);
        articles.add(sampleArticle2);
        articles.add(sampleArticle3);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        // retrieve rating
        shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        float prevRatingValue = (shared.getFloat(keyChannel, -1));
        //Toast.makeText(soccer_landing.this, "prev val: " + String.valueOf(ratingValue), Toast.LENGTH_LONG).show();
        String ratingValueMessage;
        if (prevRatingValue == -1) {
            // display message in dialog indicating the rating value doesn't exist
            ratingValueMessage = "No previous rating values found";
        } else {
            ratingValueMessage = "Previous rating value: " + String.valueOf(prevRatingValue);
        }

        // get new rating
        // UNCOMMENT THE DIALOG CODE ONCE DONE SETTING UP OTHER PARTS
        //showRatingDialog(ratingValueMessage);

    }

    @Override
    protected void onStart() {
        super.onStart();
        artAdapter = new ArticleList(SoccerLanding.this, articles);
        articleBox.setAdapter(artAdapter);
    }

    public Article getItem(int position) {
        return articles.get(position);
    }

    public long getId(int position) {
        Article temp = articles.get(position);
        long articleId = temp.getId();
        return articleId;
    }

    private void showRatingDialog(String prevValueMsg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rating_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView prevRating = (TextView) dialogView.findViewById(R.id.prevRating);
        final RatingBar ratingBar = dialogView.findViewById(R.id.rating);
        final Button posButton = (Button) dialogView.findViewById(R.id.posButton);
        final Button negButton = (Button) dialogView.findViewById(R.id.negButton);

        dialogBuilder.setTitle("How would you rate our application?");

        final AlertDialog b = dialogBuilder.create();
        b.show();

        prevRating.setText(prevValueMsg);

        // set up  the rating bar

        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get value from rating bar
                ratingValue = ratingBar.getRating();
                // saving the new rating
                shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                //Toast.makeText(soccer_landing.this, String.valueOf(ratingValue), Toast.LENGTH_LONG).show();
                editor.putFloat(keyChannel, ratingValue);
                editor.commit();// commit is important here.
                //Toast.makeText(soccer_landing.this, String.valueOf(ratingValue), Toast.LENGTH_LONG).show();
                b.dismiss();
            }
        });

        negButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.articles_actions, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {

            case R.id.favourites:
                Intent goFavourite = new Intent(this, FavouriteArticles.class);
                //message = "You clicked favourites";
                startActivity(goFavourite);
                break;
        }
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }
}