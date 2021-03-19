
package com.example.myapplication;
/**
 * @Author: Nouran Nouh
 * Date:18/03/2021
 * SongLister.class has 2 inner class AsyncTask that handle the thread on the background which will
 * be executing xml and display that in main UI
 * the second inner class is ArtistListAdpater which extend base adapter
 * which inflate ListView and display List view
 * Songlister class receive the user input from SongsterSearch class and then
 * display all the song from xml url sang by this artist like
 * song id, song title, and Artist Id.
 * this class also has back button which will go back to SongsterSearch page to perform another search
 * of another Artist.
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    //define variables
    private Button backButton;
    private ArrayList<Artist> artistList=new ArrayList<> ();
    private String artistName;
    private Artist artist;
    private MySongListAdapter artistAdapter;
    private ListView artistListView;
    private ProgressBar progressBar;
   private static SQLiteDatabase db;
    private static final String SONG_URL="https://www.songsterr.com/a/ra/songs.xml?pattern=";

   /*
    onCreate application start from onCreate that has some Logic setup
    like getting the objects id from xml layout as well as uploading the xml layout
    and get data from previous activity
    */
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
        boolean isTablet=findViewById(R.id.frameLayout)!=null;// check if it is tablet


        //get Artist Name from searchpage
        Bundle searchPage = getIntent().getExtras();
        artistName = searchPage.getString(SongsterSearch.ARTIST_KEYWORD);

        //execute the url by using the artistName from searchpage
        String artistURL = SONG_URL+artistName;
        //create new instance of ArtistQuery that extends the AsyncTask
        ArtistQuery qs = new ArtistQuery(this);
        //execute url
        qs.execute(artistURL);
        //create view that is used to set the view for snackbar
        View view = findViewById(R.id.main_layout_id);

        //clicking back button goes back to SongsterSearch class by calling finish() method
        //that finishes this activity
        backButton.setOnClickListener((e)->
        {
                finish();

        });

        /**
         * click Long click on item in the list display an alert dialog that asks user
         * if he/she wants to know more about the song
         * if he click yes button send the song data like Artist Name
         * id, ArtisId, SongId and songTitle to SongDetail class
         * if no stay in this page
         */

        artistListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            //create new Bundle to pass activity to next activity
            Bundle dataTopass=new Bundle();
           Artist selectedArtist=artistList.get(position);
            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            //set title for the alertDialog
            alert.setTitle(R.string.Songster_moreInfoAboutSong).
                    //set message to be displayed to user
                    setMessage("You selected"+" "+selectedArtist.getSongTitle()).
                    //set button for confirmation
                    setPositiveButton(R.string.confirm,(click,arg)->
                    {
                        //send data to next activity
                        dataTopass.putString("id",String.valueOf(selectedArtist.getId()));
                        dataTopass.putString("ArtistName",selectedArtist.getArtistName());
                        dataTopass.putString("ArtistId",selectedArtist.getArtistId());
                        dataTopass.putString("SongId",selectedArtist.getSongId());
                        dataTopass.putString("SongTitle",selectedArtist.getSongTitle());

                        //checks if it is tablet
                        if(isTablet){
                            //create new instance of SongDetail for the information to be
                            //displayed to the right-side of the screan by creating fragment
                            // //if already exist just replace it and commit
                            //for changes to take eefect
                            SongDetail dfragment= new SongDetail();
                            dfragment.setArguments(dataTopass);
                            getSupportFragmentManager().
                                    beginTransaction().
                                    replace(R.id.frameLayout,dfragment)
                                    .commit();
                        //if it is a phone then go new activity(FragmentPhoneActivity)
                            // //and pass data to that activity
                        }else {

                            Intent nextActivity = new Intent(this, FragmentPhoneActivity.class);
                            nextActivity.putExtras(dataTopass);
                            startActivity(nextActivity); //start activity
                        }
                    //if it is no button just remain in this page
                        //by displaying a snackbar to the user
                    }).setNegativeButton("No",(click,arg)->{
                        //create snackbar and style it
                        Snackbar snackbar=Snackbar.make(view,R.string.nothingChanged,Snackbar.LENGTH_LONG);
                        View snackbarView=snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.pink));
                                TextView textView= snackbarView.findViewById(R.id.toastText);
                                textView.setTextSize(20);
                                snackbar.show();
             //show alert dialog
            }).create().show();
            return true;

        });
    }





//    private void loadDataFromDB(){
//        //clear current arraylist
//        artistList=new ArrayList<>();
//        SongOpener songDB=new SongOpener(this);
//        db=songDB.getWritableDatabase();
//        String [] columns={ SongOpener.COL_ID, SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID, SongOpener.COL_SONGID, SongOpener.COL_SONGTITLE};
//        Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);
//
//
//        int idColIndex = results.getColumnIndex( SongOpener.COL_ID);
//        int artistNameColIndex = results.getColumnIndex( SongOpener.COL_ARTISTNAME);
//        int artistIdColIndex = results.getColumnIndex(SongOpener.COL_ARTISTID);
//        int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
//        int songTitleIndex = results.getColumnIndex(SongOpener.COL_SONGTITLE);
//       // int isFavouriteColIndex = results.getColumnIndex(SongOpener.COL_IsFavorite);
//        while(results.moveToNext()){
//            long id=results.getLong(idColIndex);
//            String artistNameDB=results.getString(artistNameColIndex);
//            String artistId=results.getString(artistIdColIndex);
//            String songId=results.getString(songIdColIndex);
//            String songTitle=results.getString(songTitleIndex);
//            //String isFavourite=results.getString(isFavouriteColIndex);
//            artistList.add(new Artist(id,artistName,artistId,songId,songTitle));
//
//        }
//
//    }





   /**
    * AsyncTask takes care of sycronization between Gui and background thread
    * it has 4 methods
    * doInBackground: set the connection and handle reading xml tags
    * onPostExecute: this method we can update ui of background operation result.
    * OnPreExecute: Called Before doing background operation to set the progressBar to visible
    *onProgressUpdate:if some information need to be updated like
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
               //debugging
               Log.e("url", String.valueOf(url));
               Log.e("url2", params[0]);
               //open the connection
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

               Log.e("response", String.valueOf(urlConnection.getResponseCode()));

               //get the response from the connection
               InputStream response = urlConnection.getInputStream();


               //wait for data:
              //defining parsing functionality
               XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
               factory.setNamespaceAware(false);
               XmlPullParser xpp = factory.newPullParser();
               xpp.setInput(response, "UTF-8");
               //return the type of event(start tage, text or End_Tag)
               int eventType = xpp.getEventType();
               //loop until it is End_Of the document
               while (xpp.next() != XmlPullParser.END_DOCUMENT) {

                  //create new instance of Artist to set Artist info
                   Artist artist = new Artist();
                   //if it is the start of the tage
                   if ( xpp.getEventType() == XmlPullParser.START_TAG) {
                       //and tag equals song read
                      // text and other tags inside Song Tag
                       if(xpp.getName().equals("Song")) {
                           //get Song id
                            artist.setSongId(xpp.getAttributeValue(null, "id"));
                          //move to next tag
                           xpp.nextTag();
                           //get Song title text
                           artist.setSongTitle(xpp.nextText());
                           //move to next tage
                           xpp.nextTag();
                           //get ArtistId
                           artist.setArtistId(xpp.getAttributeValue(null, "id"));
                          //go to next tag
                           xpp.nextTag();
                           //get Artist Name
                           artist.setArtistName(xpp.nextText());
                           //and artist object to artistList Array
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
              //notify ListView of changes so changes can be applied
              artistAdapter.notifyDataSetChanged();
              //after getting the result set progress bar to invisble
              progressBar.setVisibility(View.INVISIBLE);

       }

       @Override
       protected void onPreExecute() {

           super.onPreExecute();
           progressBar.setVisibility(View.VISIBLE);
       }

       @Override
       protected void onProgressUpdate(Integer... values) {
          //setProgress bar
           progressBar.setProgress(values[0]);
           artistAdapter.notifyDataSetChanged();

       }
   }







    /**
     * BaseAdapter used to display the artist view of List view
     * and do basic getters
     * getCount:get size of the arrayList
     * getItem:get the artistList item
     * getItemId: get Id for database
     * getView: Inflate the view List and populate it with Artist info
     * @Return the view to the user
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
            //infalte the view
            View artistView=inflater.inflate(R.layout.artist_row,parent,false);
            //get TextView for each artist info
            TextView songId=artistView.findViewById(R.id.SongIdRow);
            TextView title=artistView.findViewById(R.id.artistTitleRow);
            TextView artistId=artistView.findViewById(R.id.artistIdRow);
            TextView artistName=artistView.findViewById(R.id.artistName);
            //setText that is displayed in the ListView
            songId.setText(artist.getSongId()+"");
            title.setText(artist.getSongTitle());
            artistId.setText(artist.getArtistId()+"");
            artistName.setText(artist.getArtistName());


            return artistView;
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == resultCode) {
//            loadDataFromDB();
//            artistAdapter.notifyDataSetChanged();
//        }
//    }


   //called when this activity start
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SongList","in the function onStart");
    }

    //called when application stopped
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SongList","in the function onStop");
    }
  //called if application is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SongList","in the function onDestory");
    }
    //Pause activity if intrupption occurs like starting new activity
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("SongList","in the function onPause");
    }
}