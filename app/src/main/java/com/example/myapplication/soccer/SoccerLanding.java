package com.example.myapplication.soccer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SoccerLanding extends AppCompatActivity {

    ArrayList<Article> articles;
    ListView articleBox;
    ArticleList artAdapter;
    ProgressBar progressBar;

    float ratingValue;
    SharedPreferences shared;
    String PREF_NAME = "rating_file";
    String keyChannel = "rating";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_landing);
        setTitle(getResources().getText(R.string.soccer_Landing_Title));
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        articleBox = (ListView) findViewById(R.id.articleBox);

        //Article(String title, String pubDate, String url, String description, String imageUrl, String thumbnailImageUrl)


        articles = new ArrayList<Article>();

        // call asynch task to add images from the imageURL and thumbnailImageURL
        DrawableQuery temp = new DrawableQuery();
        temp.execute();


        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        Resources res = getResources();
        String noRating = String.format(res.getString(R.string.noPreviousRating));
        String previousRating = String.format(res.getString(R.string.PreviousRating));
        // retrieve rating
        shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        float prevRatingValue = (shared.getFloat(keyChannel, -1));
        //Toast.makeText(soccer_landing.this, "prev val: " + String.valueOf(ratingValue), Toast.LENGTH_LONG).show();
        String ratingValueMessage;
        if (prevRatingValue == -1) {
            // display message in dialog indicating the rating value doesn't exist
            ratingValueMessage = noRating;
        } else {
            ratingValueMessage = previousRating + ": " + String.valueOf(prevRatingValue);
        }

        // get new rating
        // UNCOMMENT THE DIALOG CODE ONCE DONE SETTING UP OTHER PARTS
        showRatingDialog(ratingValueMessage);

        //on click for each item in the list
        articleBox.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            Article curr = articles.get(position);

            dataToPass.putString("title", curr.getTitle());
            dataToPass.putString("pubDate", curr.getPubDate());
            dataToPass.putString("url", curr.getUrl());
            dataToPass.putString("descr", curr.getDescription());
            dataToPass.putString("imageURL", curr.getImageUrl());
            dataToPass.putString("thumbnailImageURL", curr.getThumbnailImageUrl());


                Intent nextActivity = new Intent(SoccerLanding.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //go to next activity


            String openingArticle = String.format(res.getString(R.string.openingArticle));
            Toast.makeText(getApplicationContext(),openingArticle,
                    Toast.LENGTH_LONG).show();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Snackbar snackbar=Snackbar.make(findViewById(R.id.progressBar),"Loading",Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    public Article getItem(int position) {
        return articles.get(position);
    }

    public long getId(int position) {
        Article temp = articles.get(position);
        long articleId = temp.getId();
        return articleId;
    }

    //method for app rating pop up
    private void showRatingDialog(String prevValueMsg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rating_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView prevRating = (TextView) dialogView.findViewById(R.id.prevRating);
        final RatingBar ratingBar = dialogView.findViewById(R.id.rating);
        final Button posButton = (Button) dialogView.findViewById(R.id.posButton);
        final Button negButton = (Button) dialogView.findViewById(R.id.negButton);

        Resources res = getResources();
        String dialogTitle = String.format(res.getString(R.string.dialogBoxTitle));
        dialogBuilder.setTitle(dialogTitle);

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

                editor.putFloat(keyChannel, ratingValue);
                editor.commit();// commit is important here.

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
        Resources res = getResources();
        String goToFavourites = String.format(res.getString(R.string.goTofavourite));


        //Look at menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {

            case R.id.favourites:
                Intent goFavourite = new Intent(this, FavouriteArticles.class);
                Toast.makeText(getApplicationContext(),goToFavourites,
                        Toast.LENGTH_LONG).show();
                startActivity(goFavourite);
                break;

            case R.id.help:
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                alertDialog.setTitle("Soccer News Help").setMessage("Click on any article to see more details about it. Click" +
                        "Save to save article in database, Click open to view article in browser").setPositiveButton("OK",null).show();


        }

        return true;
    }

    public class DrawableQuery extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {



            try {

                //connection to XML
                URL articlesURL = new URL("https://www.goal.com/feeds/en/news");

                HttpURLConnection articlesHttp=(HttpURLConnection)articlesURL.openConnection();
                articlesHttp.setDoInput(true);
                articlesHttp.connect();
                InputStream is = articlesHttp.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(is, null);

                //article items
                int eventType = xpp.getEventType();
                boolean isItem = false;
                String title = "";
                String pubDate = "";
                String url = "";
                String description = "";
                String imageUrl = "";
                String thumbnailImageUrl = "";

                //while loop to go through xml tags and get information
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {

                    }

                    if  (eventType == XmlPullParser.START_TAG) {
                        //Log.d("Parser", "Start tag "+xpp.getName());
                        String tagname = xpp.getName();
                        if (tagname.equals("item")) {

                                isItem = true;
                        }

                        if(isItem) {
                            if (tagname.equals("title")) {
                                //Log.d("Parser", "within title");
                                if (xpp.next() == XmlPullParser.TEXT) {
                                    title = xpp.getText();
                                    //Thread.sleep(50);
                                    publishProgress(15);

                                }

                            }
                            if (tagname.equals("pubDate")) {
                                //Log.d("Parser", "within pubDate");
                                if (xpp.next() == XmlPullParser.TEXT) {
                                    pubDate = xpp.getText();
                                    publishProgress(25);
                                    //Thread.sleep(50);

                                }
                            }
                            if (tagname.equals("link")) {
                                //Log.d("Parser", "within link");
                                if (xpp.next() == XmlPullParser.TEXT) {
                                    url = xpp.getText();
                                    Thread.sleep(50);
                                    publishProgress(45);

                                }
                            }
                            if (tagname.equals("description")) {
                                //Log.d("Parser", "within description");
                                if (xpp.next() == XmlPullParser.TEXT) {
                                    description = xpp.getText();
                                    publishProgress(65);
                                    //Thread.sleep(50);

                                }
                            }
                            if (tagname.equals("content")) {
                                //Log.d("Parser", "within media:content");
                                imageUrl = xpp.getAttributeValue(null, "url");
                                //Thread.sleep(50);
                                publishProgress(85);

                            }
                            if (tagname.equals("thumbnail")) {
                               //Log.d("Parser", "within media:thumbnail");
                                thumbnailImageUrl = xpp.getAttributeValue(null, "url");
                                //Thread.sleep(50);
                                publishProgress(100);

                            }
                        }



                    }
                    if  (eventType == XmlPullParser.END_TAG) {
                        //Log.d("Parser", "Start tag "+xpp.getName());
                        String tagname = xpp.getName();
                        if (tagname.equals("item")) {
                            Article temp = new Article(title, pubDate, url, description, imageUrl, thumbnailImageUrl);
                            articles.add(temp);
                            isItem = false;

                        }

                    }

                    eventType = xpp.next();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


            // get images for the articles retrieved

            for (int i = 0; i < articles.size(); i++) {
                Article curr = articles.get(i);

                try {
                    URL imgURL = new URL(curr.getImageUrl());
                    URL thURL = new URL(curr.getThumbnailImageUrl());

                    Bitmap img;
                    Bitmap thImg;

                    Drawable imgD;
                    Drawable thImgD;


                    HttpURLConnection imgConnection = (HttpURLConnection) imgURL.openConnection();
                    HttpURLConnection thConnection = (HttpURLConnection) thURL.openConnection();


                    imgConnection.connect();
                    thConnection.connect();


                    InputStream imgInput = imgConnection.getInputStream();
                    InputStream thInput = thConnection.getInputStream();


                    img = BitmapFactory.decodeStream(imgInput);

                    thImg = BitmapFactory.decodeStream(thInput);


                    //converts bitmap img to drawable

                    imgD = new BitmapDrawable(Resources.getSystem(), img);
                    thImgD = new BitmapDrawable(Resources.getSystem(), thImg);


                    curr.setImage(imgD);
                    curr.setThumbnailImage(thImgD);


                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            artAdapter = new ArticleList(SoccerLanding.this, articles);
            articleBox.setAdapter(artAdapter);
            progressBar.setVisibility(View.INVISIBLE);
        }
        protected void onProgressUpdate(Integer... value) {
            // update progress bar to value[0]
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }
    }
}