package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.android.material.snackbar.Snackbar;

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
    static SQLiteDatabase db;
    private static final String SONG_URL="https://www.songsterr.com/a/ra/songs.xml?pattern=";

    @Override
    @SuppressWarnings( "deprecation" )
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //upload the layout of the song list
        setContentView(R.layout.activity_songster_list);
        //extract the id from xml
        backButton = findViewById(R.id.GoBackButton);
        progressBar = findViewById(R.id.progressBar);
        artistListView = findViewById(R.id.SongView);
        artistAdapter = new MySongListAdapter();
        artistListView.setAdapter(artistAdapter);


        //get Intent from searchpage
        Bundle searchPage = getIntent().getExtras();
        artistName = searchPage.getString(SongsterSearch.ARTIST_KEYWORD);
       // progressBar.setVisibility(View.VISIBLE);//set progressBar to visible


        //execute the url by using the artistName from searchpage
        String artistURL = SONG_URL+artistName;
        ArtistQuery qs = new ArtistQuery(this);
        qs.execute(artistURL);

        View view = findViewById(R.id.main_layout_id);


        //dismiss going back to main page
       // final boolean[] isClicked = {false};

        backButton.setOnClickListener((e)->
        {
//            snackbar.setAction("DISMISS",b->{
//
//                onResume();
//                Log.e("reached resume","resume");
//                if(snackbar.)
//
//            }).show();

                finish();

        });

        //long
        artistListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            Bundle dataTopass=new Bundle();
           Artist selectedArtist=artistList.get(position);
            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            alert.setTitle("Would Like to know more about this Artist?").
                    setMessage("you selected: "+selectedArtist.getSongTitle()).
                    setPositiveButton("yes",(click,arg)->
                    {
                        dataTopass.putString("id",String.valueOf(selectedArtist.getId()));
                        dataTopass.putString("ArtistName",selectedArtist.getArtistName());
                        dataTopass.putString("ArtistId",selectedArtist.getArtistId());
                        dataTopass.putString("SongId",selectedArtist.getSongId());
                        dataTopass.putString("SongTitle",selectedArtist.getSongTitle());
                        //dataTopass.putString("SongFav",selectedArtist.getFavouriteSong());

                        Intent nextActivity= new Intent(this,FragmentPhoneActivity.class);
                        nextActivity.putExtras(dataTopass);
                        startActivity(nextActivity);

                    }).setNegativeButton("No",(click,arg)->{
                        Snackbar.make(view,"Nothing changed",Snackbar.LENGTH_LONG).show();

            }).create().show();
            return true;

        });
    }





    private void loadDataFromDB(){
        //clear current arraylist
        artistList=new ArrayList<>();
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
            String artistNameDB=results.getString(artistNameColIndex);
            String artistId=results.getString(artistIdColIndex);
            String songId=results.getString(songIdColIndex);
            String songTitle=results.getString(songTitleIndex);
            //String isFavourite=results.getString(isFavouriteColIndex);
            artistList.add(new Artist(id,artistName,artistId,songId,songTitle));

        }

    }





   /**
    * AsyncTask takes care of sycronization between Gui and background thread
    *
    */
   @SuppressWarnings( "deprecation" )
   public class ArtistQuery extends AsyncTask<String, Integer, String> {




       public ArtistQuery(Context context){

       }


       @Override
       protected String doInBackground(String... params) {

           try {

               //create a URL object of what server to contact:
               URL url = new URL(params[0]);
               Log.e("url", String.valueOf(url));
               Log.e("url2", params[0]);
               //open the connection
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

               Log.e("response", String.valueOf(urlConnection.getResponseCode()));

               InputStream response = urlConnection.getInputStream();
             //BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);


       //wait for data:

               XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
               factory.setNamespaceAware(false);
               XmlPullParser xpp = factory.newPullParser();
               xpp.setInput(response, "UTF-8");
               int eventType = xpp.getEventType();

            int counter=0;
               while (xpp.next() != XmlPullParser.END_DOCUMENT) {


                   Artist artist = new Artist();
                   if ( xpp.getEventType() == XmlPullParser.START_TAG) {

                       if(xpp.getName().equals("Song")) {
                            artist.setSongId(xpp.getAttributeValue(null, "id"));
                            String songId=xpp.getAttributeValue(null, "id");
                           xpp.nextTag();
                           artist.setSongTitle(xpp.nextText());
                           xpp.nextTag();
                           artist.setArtistId(xpp.getAttributeValue(null, "id"));

                           xpp.nextTag();
                           artist.setArtistName(xpp.nextText());
                           artistList.add(artist);
                       }



                   }

               }


               } catch(Exception e){
                       Log.e("Exceptions",e.getMessage());
                       e.printStackTrace();
               }


           return "finish";
       }




       @Override
       protected void onPostExecute(String s) {
              super.onPostExecute(s);
              artistAdapter.notifyDataSetChanged();
           progressBar.setVisibility(View.INVISIBLE);

       }

       @Override
       protected void onPreExecute() {

           super.onPreExecute();
           progressBar.setVisibility(View.VISIBLE);
       }

       @Override
       protected void onProgressUpdate(Integer... values) {

           progressBar.setProgress(values[0]);
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
        public Artist getItem(int position) {
            return artistList.get(position);
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
            songId.setText(artist.getSongId()+"");
            title.setText(artist.getSongTitle());
            artistId.setText(artist.getArtistId()+"");
            artistName.setText(artist.getArtistName());


            return artistView;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == resultCode) {
            loadDataFromDB();
            artistAdapter.notifyDataSetChanged();
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