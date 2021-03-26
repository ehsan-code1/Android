<<<<<<< HEAD:app/src/main/java/com/example/myapplication/SongsterSearch/FavouriteList.java
package com.example.myapplication.SongsterSearch;
=======
package com.example.myapplication.songstar;
>>>>>>> origin/master:app/src/main/java/com/example/myapplication/songstar/FavouriteList.java

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD:app/src/main/java/com/example/myapplication/SongsterSearch/FavouriteList.java
=======
import com.example.myapplication.R;

>>>>>>> origin/master:app/src/main/java/com/example/myapplication/songstar/FavouriteList.java
import java.util.ArrayList;

import com.example.myapplication.R;

/**
 * FavouritList Class holds a List View of Favourite songs
 * when user Long click on one item in the List it display a fragment
 * that has a delete button, search Artist button and search song button
 */

public class FavouriteList extends AppCompatActivity {
    //define arrayList of Artist to save Fav Songster
    ArrayList<Artist> favList=new ArrayList<>();
    MySongListAdapter myAdapter;
    static SQLiteDatabase db;
    ListView listview;
    //isSaveButton is a boolean variable that is passed to fragment clas
    boolean isSaveButton=true;
    //save postion when item from ListView is clicked
    int positionSaved;
    boolean isTablet;
    SwipeRefreshLayout pullRefresher=null;




    @Override
    @SuppressWarnings( "deprecation" )
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        listview=findViewById(R.id.Listid);
        loadDataFromDB();
        myAdapter=new MySongListAdapter();
        listview.setAdapter(myAdapter);
        //define swipe refresher
       pullRefresher=findViewById(R.id.refresher);

       isTablet=findViewById(R.id.frameLayout)!=null;// check if it is tablet
        //get String from R.string to be added in setMessage
        //in alert dialog
        String str=getString(R.string.youSelected);
        if(isTablet){
            pullRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){


                @Override
                public void onRefresh() {
                    favList.clear();
                    loadDataFromDB();
                    myAdapter.notifyDataSetChanged();
                    pullRefresher.setRefreshing(false);
                }
            });
        }

        listview.setOnItemLongClickListener((parent, view1, position, id) -> {
            //create new Bundle to pass activity to next activity(Fragment)
            positionSaved=position;
            Bundle dataTopass=new Bundle();
            //get specific Artist fom List
            Artist selectedArtist=favList.get(position);
            /*
             ** generate an alert dialog to user to ask if he/she wants more information about the
             * selected Artist, if yes clicked pass data to new activity if it is a phone
             * if it is a tablet display it on the right hand side
             * if no stay on this activity
             */

            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            //set title for the alertDialog
            alert.setTitle(R.string.Songster_moreInfoAboutSong).
                    //set message to be displayed to user
                            setMessage(str+" "+selectedArtist.getSongTitle()).
                            //set button for confirmation
                            setPositiveButton(R.string.confirm,(click,arg)->
                    {
                        //send data to next activity
                        dataTopass.putString("id",String.valueOf(selectedArtist.getId()));
                        dataTopass.putString("ArtistName",selectedArtist.getArtistName());
                        dataTopass.putString("ArtistId",selectedArtist.getArtistId());
                        dataTopass.putString("SongId",selectedArtist.getSongId());
                        dataTopass.putString("SongTitle",selectedArtist.getSongTitle());
                        dataTopass.putBoolean("SaveButton",true);
                        dataTopass.putBoolean("isTablet",isTablet);


                        //checks if it is tablet
                        if(isTablet){
                            //create new instance of SongDetail for the information to be
                            //displayed to the right-side of the screan by creating fragment
                            // //if already exist just replace it and commit
                            //for changes to take efect
                            SongDetail dfragment= new SongDetail();
                            dfragment.setArguments(dataTopass);
                            getSupportFragmentManager().
                                    beginTransaction().
                                    replace(R.id.frameLayout,dfragment)
                                    .commit();

                            //else if it is a phone then go new activity(FragmentPhoneActivity)
                            // //and pass data to that activity
                        }else {

                            Intent nextActivity = new Intent(this, FragmentPhoneActivity.class);
                            nextActivity.putExtras(dataTopass);
                            startActivityForResult(nextActivity,1); //start activity
                        }
                        //if it is no button just remain in this page
                        //by displaying a snackbar to the user
                    }).setNegativeButton(R.string.decline,(click,arg)->{

                        //create toast for user
                       Toast toast= Toast.makeText(getApplicationContext(), R.string.nothingChanged, Toast.LENGTH_LONG);
                       //style Toast
                        View view = toast.getView();
                        view.setBackgroundColor(Color.parseColor("#FF000000"));
                        TextView text = (TextView) view.findViewById(android.R.id.message);
                        text.setTextColor(Color.parseColor("#FFFFFFFF"));
                        text.setTextSize(20);
                        toast.show();

                //show alert dialog
            }).create().show();
            return true;

        });

    }

    /*
    ** get requestCode from fragment when delete button pressed and result deleted from database
    * if its =1 and resultCode =Result.OK then remove from the Favourite List as well
    * and notify the ListView
    *
     */
    @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
      super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == 1) {
           if(resultCode== Activity.RESULT_OK) {
                 favList.remove(positionSaved);
                 myAdapter.notifyDataSetChanged();

               }



           }

       }





    /*
     ** Load Data saved from Database
     */
    private void loadDataFromDB(){

        //create instance of db
        SongOpener songDB=new SongOpener(this);
        //Create and/or open a database that will be used for reading and writing, if it is the first time
        //call onCreate(), else db will be cached
        db=songDB.getWritableDatabase();
        //create array of db columns
        String [] columns={ SongOpener.COL_ID, SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID, SongOpener.COL_SONGID, SongOpener.COL_SONGTITLE};
       //query from db
        Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //get the index for each column
        int idColIndex = results.getColumnIndex( SongOpener.COL_ID);
        int artistNameColIndex = results.getColumnIndex( SongOpener.COL_ARTISTNAME);
        int artistIdColIndex = results.getColumnIndex(SongOpener.COL_ARTISTID);
        int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
        int songTitleIndex = results.getColumnIndex(SongOpener.COL_SONGTITLE);

        //loop through the table and save columns values
        while(results.moveToNext()){
            long id=results.getLong(idColIndex);
            String artistName=results.getString(artistNameColIndex);
            String artistId=results.getString(artistIdColIndex);
            String songId=results.getString(songIdColIndex);
            String songTitle=results.getString(songTitleIndex);

           //add values from db to array
            favList.add(new Artist(id,artistName,artistId,songId,songTitle));

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

    private class MySongListAdapter extends BaseAdapter {

        //get ArrayList size
        @Override
        public int getCount() {
            return favList.size();
        }

        //get item at specific position
        @Override
        public Artist getItem(int position) {
            return favList.get(position);
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
            songId.setText(artist.getSongId());
            title.setText(artist.getSongTitle());
            artistId.setText(artist.getArtistId());
            artistName.setText(artist.getArtistName());


            return artistView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}