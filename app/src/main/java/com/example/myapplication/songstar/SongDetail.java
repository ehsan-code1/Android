package com.example.myapplication.songstar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

/**
 * Class SongDetail create the fragment that shows
 * ArtistId , ArtistName, SongId, SongName
 * also has save or delete button depends if it is SongsterList.java or FavouriteList.java
 * as well as search artist and search song that allow user to search the specific artist on google
 *
 */

public class SongDetail extends Fragment {
    //define variable
    private TextView artistNameView;
    private TextView artistIdView;
    private TextView songIdView;
    private TextView songTitleView;
    public Button SaveOrDeleteButton;
    Button searchSong;
    Button searchArtist;

    public SQLiteDatabase db;
    Bundle prevData;
    private AppCompatActivity parentApp;

    String artistName;
    String artistId;
    String songId;
    String songTitle;
    private static final String SearchSongUrl="http://www.songsterr.com/a/wa/song?id=";
    private static final String SearchArtistUrl="http://www.songsterr.com/a/wa/song?id=";
    boolean isDeleteButton;
    boolean isTablet;
    //int position;



    @SuppressWarnings( "deprecation" )

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get data when fragment was instiated
        prevData=getArguments();
        artistName=prevData.getString("ArtistName");
        artistId=prevData.getString("ArtistId");
        songId=prevData.getString("SongId");
        songTitle=prevData.getString("SongTitle");
        isDeleteButton = prevData.getBoolean("SaveButton");
        isTablet=prevData.getBoolean("isTablet");



        //get result back
        int result=0;

        //get the view Layout
        View view=inflater.inflate(R.layout.activity_song_details_page,container,false);

        //find id from layout

        artistNameView=view.findViewById(R.id.artistName);
        artistIdView=view.findViewById(R.id.artistId);
        songIdView=view.findViewById(R.id.songId);
        songTitleView=view.findViewById(R.id.SongTitle);
         SaveOrDeleteButton =view.findViewById(R.id.SaveDB);
         searchSong=view.findViewById(R.id.searchButton);
         searchArtist=view.findViewById(R.id.ArtistIdbtn);
         //set the Texts the will be displayed on the fragments
         artistNameView.setText("ArtistName: "+artistName);
         artistIdView.setText("ArtistId: "+artistId);
         songIdView.setText("SongId: "+songId);
         songTitleView.setText("Song title: "+songTitle);



         //go to song page
        searchSong.setOnClickListener(d->{
                String url=SearchSongUrl+songId;
                Intent searchGoogle=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(searchGoogle);

                }
                );

       //search artist page in google
        searchArtist.setOnClickListener(d->{
                    String urlArtist=SearchArtistUrl+songId;
                    Intent searchGoogleArtist=new Intent(Intent.ACTION_VIEW, Uri.parse(urlArtist));
                    startActivity(searchGoogleArtist);

                }
        );

        //check if isDeleteButton, if it not then it is Save button
        //display it in SongsterList fragment and set the button text to Save
        //if user clicked no stay in this page
        if(isDeleteButton ==false){
            SaveOrDeleteButton.setText(R.string.Songster_Save);

            //if user clicked on save button display alert dialog to user
            //if user clicked yes save it into db
            SaveOrDeleteButton.setOnClickListener(v->{

                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.SongDetail_FavouriteListConfirmation_Add).
                        setPositiveButton(R.string.confirm,(Click,args)->{
                            //create instance of SongsterList
                            SongsterList songster= new SongsterList();
                            ContentValues cv= new ContentValues();
                            SongOpener songDB=new SongOpener(getActivity());
                            db=songDB.getWritableDatabase();

                            //check for duplicates, dont add duplicate row
                            boolean isSaved=false;
                            String [] columns={ SongOpener.COL_ID, SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID, SongOpener.COL_SONGID, SongOpener.COL_SONGTITLE};
                            Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);
                            //check by songId
                            int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
                            while(results.moveToNext()){
                                String songIdOld=results.getString(songIdColIndex);
                                if(songIdOld.equals(songId)){
                                    Toast.makeText(getActivity(),R.string.duplicateMessage,Toast.LENGTH_LONG).show();
                                    isSaved=true;
                                    break;
                                }


                            }
                            //if it is not saved and not duplicate save it
                            if(!isSaved) {
                                cv.put(SongOpener.COL_ARTISTNAME, artistName);
                                cv.put(SongOpener.COL_ARTISTID, artistId);
                                cv.put(SongOpener.COL_SONGID, songId);
                                cv.put(SongOpener.COL_SONGTITLE, songTitle);
                                db.insert(SongOpener.TABLE_NAME, null, cv);
                                Toast toast=Toast.makeText(getActivity(),R.string.ConfirmationMessageofSave,Toast.LENGTH_LONG);
                                View view1 = toast.getView();
//                                view1.setBackgroundColor(Color.parseColor("#FF000000"));
   //                             TextView text = (TextView) view1.findViewById(android.R.id.message);
        //                        text.setTextColor(Color.parseColor("#FFFFFFFF"));
        //                        text.setTextSize(20);
                                toast.show();
                            }

                        }).
                        setNegativeButton(R.string.decline,(click,args)->{
                            Toast toast=Toast.makeText(getActivity(),R.string.nothingChanged,Toast.LENGTH_LONG);
                            View view1 = toast.getView();
//                            view1.setBackgroundColor(Color.parseColor("#FF000000"));
//                            TextView text = (TextView) view1.findViewById(android.R.id.message);
//                            text.setTextColor(Color.parseColor("#FFFFFFFF"));
//                            text.setTextSize(20);
                            toast.show();


                        }).create().show();
            });


      // else if it is Delete button display it in FavouriteList Fragment
            // and set button text to Delete
            //display alert dialog to user asking about delete confirmation
            //if yes delete from db and return result to previous activity (FavouriteList Activity)
        }else{
            SaveOrDeleteButton.setText(R.string.Songster_Delete);

            SaveOrDeleteButton.setOnClickListener(s->
            {

                AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.AskingMessageToDelete).
                        setPositiveButton(R.string.confirm,(Click,args)->{
                            SongOpener songDB=new SongOpener(getActivity());
                            db=songDB.getWritableDatabase();

                           songDB.deleteFromDB(songId);

                           if(!isTablet){


                           Intent returnIntent = new Intent();
                           returnIntent.putExtra("result", 1);
                           getActivity().setResult(Activity.RESULT_OK, returnIntent);
                           getActivity().finish();


                           }
                            String str = getString(R.string.ConfirmationMessage_Of_Deletion);
                            Toast.makeText(getActivity(), songTitle + " " + str, Toast.LENGTH_LONG).show();
                            parentApp.getSupportFragmentManager().beginTransaction().remove(this).commit();
                        }).
                        setNegativeButton(R.string.decline,(click,args)->{
                            Toast toast=Toast.makeText(getActivity(),R.string.nothingChanged,Toast.LENGTH_LONG);
                            View view1 = toast.getView();
   //                         view1.setBackgroundColor(Color.parseColor("#FF000000"));
                     //       TextView text = (TextView) view1.findViewById(android.R.id.message);
                     //       text.setTextColor(Color.parseColor("#FFFFFFFF"));
                      //      text.setTextSize(20);
                            toast.show();
                        }).create().show();
            });


        }


         return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context will either be CityDetails for a tablet, or EmptyActivity for phone
        parentApp = (AppCompatActivity)context;
    }




}
