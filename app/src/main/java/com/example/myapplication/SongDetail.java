package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.w3c.dom.Text;

public class SongDetail extends Fragment {
    private TextView artistNameView;
    private TextView artistIdView;
    private TextView songIdView;
    private TextView songTitleView;
    Button save;
    Button searchSong;
    Button searchArtist;
    Button delete;
    public SQLiteDatabase db;
    Bundle prevData;
    private AppCompatActivity parentApp;
    int id;
    String artistName;
    String artistId;
    String songId;
    String songTitle;
    private static final String SearchSongUrl="http://www.songsterr.com/a/wa/song?id=";
    private static final String SearchArtistUrl="http://www.songsterr.com/a/wa/song?id=";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prevData=getArguments();
        artistName=prevData.getString("ArtistName");
        artistId=prevData.getString("ArtistId");
        songId=prevData.getString("SongId");
        songTitle=prevData.getString("SongTitle");

        //get the view Layout
        View view=inflater.inflate(R.layout.activity_song_details_page,container,false);
        //find id from layout

        artistNameView=view.findViewById(R.id.artistName);
        artistIdView=view.findViewById(R.id.artistId);
        songIdView=view.findViewById(R.id.songId);
        songTitleView=view.findViewById(R.id.SongTitle);
         save=view.findViewById(R.id.SaveDB);
         searchSong=view.findViewById(R.id.searchButton);
         searchArtist=view.findViewById(R.id.ArtistIdbtn);
         delete=view.findViewById(R.id.Delete);
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


        searchArtist.setOnClickListener(d->{
                    String urlArtist=SearchArtistUrl+songId;
                    Intent searchGoogleArtist=new Intent(Intent.ACTION_VIEW, Uri.parse(urlArtist));
                    startActivity(searchGoogleArtist);

                }
        );

        save.setOnClickListener(v->{
            AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
            alert.setTitle("Do you want to save this to your favourite List?").
                    setPositiveButton("Yes",(Click,args)->{
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
                                Toast.makeText(getActivity(),"this Song is already saved",Toast.LENGTH_LONG).show();
                                isSaved=true;
                                break;
                            }


                        }
                        if(!isSaved) {
                            cv.put(SongOpener.COL_ARTISTNAME, artistName);
                            cv.put(SongOpener.COL_ARTISTID, artistId);
                            cv.put(SongOpener.COL_SONGID, songId);
                            cv.put(SongOpener.COL_SONGTITLE, songTitle);
                            db.insert(SongOpener.TABLE_NAME, null, cv);
                            Toast.makeText(getActivity(),"Your Favourite Song is saved",Toast.LENGTH_LONG).show();
                        }

                    }).
                    setNegativeButton("No",(click,args)->{
                        Toast.makeText(getActivity(),"Nothing saved",Toast.LENGTH_LONG).show();
                    }).create().show();
        });

        delete.setOnClickListener(s->
        {
            AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
            alert.setTitle("Are you sure you want to delete this song?").
                    setPositiveButton("Yes",(Click,args)->{
                        SongOpener songDB=new SongOpener(getActivity());
                        //db=songDB.getWritableDatabase();
                        songDB.deleteFromDB(songId);
                        Toast.makeText(getActivity(),"has been removed from favourite",Toast.LENGTH_LONG).show();
                        parentApp.getSupportFragmentManager().beginTransaction().remove(this).commit();
                    }).
                    setNegativeButton("No",(click,args)->{
                        Toast.makeText(getActivity(),"Nothing changed",Toast.LENGTH_LONG).show();
                    }).create().show();
        });




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

//    protected void deleteFromDB(Long id){
//        db.delete(SongOpener.TABLE_NAME,SongOpener.COL_ID+ "= ?",new String[]{Long.toString(id)});
//    }

}
