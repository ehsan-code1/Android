package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SongsterList extends AppCompatActivity {
    Button backButton;
    ArrayList<Artist> artistList=new ArrayList<> ();
    String artistName;
    Artist artist;
    MySongListAdapter artistAdapter;
    ListView artistListView;
    ProgressBar progressBar;
    private static final String SONG_URL="http://www.songsterr.com/a/ra/songs.json?pattern=";

    @Override
    @SuppressWarnings( "deprecation" )
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //upload the layout of the song list
        setContentView(R.layout.activity_songster_list);
        //extract the id from xml
        backButton = findViewById(R.id.GoBackButton);
       // progressBar = findViewById(R.id.progressBar);
        artistListView = findViewById(R.id.SongView);
        artistAdapter = new MySongListAdapter();
        artistListView.setAdapter(artistAdapter);

        //get Intent from searchpage
        Bundle searchPage = getIntent().getExtras();
        artistName = searchPage.getString(SongsterSearch.ARTIST_KEYWORD);
       // progressBar.setVisibility(View.VISIBLE);//set progressBar to visible


        //execute the url by using the artistName from searchpage
        String artistURL = "https://www.songsterr.com/a/ra/songs.json?pattern="+artistName;
        ArtistQuery qs = new ArtistQuery(this);
        qs.execute(artistURL);


        backButton.setOnClickListener(this::onClick);
    }
    private void onClick(View e) {

        finish();
    }


   /**
    * AsyncTask takes care of sycronization between Gui and background thread
    *
    */
   @SuppressWarnings( "deprecation" )
   public class ArtistQuery extends AsyncTask<String, Integer, String> {

//         Long artistId;
//         Long songId;
//         String title;
//         String name;


       public ArtistQuery(Context context){

       }


       @Override
       protected String doInBackground(String... params) {

           try {

               //create a URL object of what server to contact:
               URL url = new URL(params[0]);
               Log.e("url", String.valueOf(url));
               Log.e("url", params[0]);
               //open the connection
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
               Log.e("url", urlConnection.getResponseMessage());
              InputStream response = urlConnection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);


       //wait for data:

//               XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//               factory.setNamespaceAware(false);
//               XmlPullParser xpp = factory.newPullParser();
//               xpp.setInput(response, "UTF-8");



           StringBuilder sb = new StringBuilder();
           String line = null;
           while ((line = reader.readLine()) != null) {
               sb.append(line + "\n");
           }
           String result = sb.toString(); //result is the whole string
           JSONArray jsonArray = new JSONArray(result);

           for (int i = 0; i < jsonArray.length(); i++) {
               JSONObject jObject = jsonArray.getJSONObject(i);
               Artist artist = new Artist();
               artist.setSongId(jObject.getString("id"));
               artist.setSongTitle(jObject.getString("title"));
               artist.setArtistId(jObject.getString("id"));
               artist.setArtistName(artistName);
               artist.setFavouriteSong("false");
               artistList.add(artist);
           }

//            int counter=0;
//               while (xpp.next() != XmlPullParser.END_DOCUMENT) {
//                   //Artist artist = new Artist();
//                   if (xpp.getEventType() == XmlPullParser.START_TAG) {
//                       if(xpp.getName().equals("Song")){
//                           artist.setSongId(xpp.getAttributeValue(null,"id"));
//                          counter++;
//                          Log.i("counter",counter+"");
//                       }else if(xpp.getName().equals("title")){
//                           artist.setSongTitle(xpp.nextText());
//                           counter++;
//                           Log.i("counter",counter+"");
//                       }else if(xpp.getName().equals("artist")){
//                           artist.setArtistId(xpp.getAttributeValue(null,"id"));
//                           counter++;
//                           Log.i("counter",counter+"");
//                       }
//                       artist.setFavouriteSong("false");
//                       artistList.add(artist);
//                   }
//               }


               } catch(Exception e){

               }


           return "finish";
       }




           @Override
       protected void onPostExecute(String s) {
              super.onPostExecute(s);
          // progressBar.setVisibility(View.INVISIBLE);

       }

       @Override
       protected void onPreExecute() {

           super.onPreExecute();
           //progressBar.setVisibility(View.VISIBLE);
       }

       @Override
       protected void onProgressUpdate(Integer... values) {

          // progressBar.setProgress(values[0]);
           artistAdapter.notifyDataSetChanged();

       }
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




    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SongList","in the function onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SongList","in the function onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SongList","in the function onDestory");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("SongList","in the function onPause");
    }
}