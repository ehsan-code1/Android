package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
/*
 ** creating fragment for the phone
 * create instance of fragment class
 * pass data to fragment
 * then begin transaction and replace the fragment with the old one
 */

public class FragmentPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_phone);
        Bundle dataToPass = getIntent().getExtras();

        SongDetail dFragment= new SongDetail();

        dFragment.setArguments(dataToPass);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,dFragment,"songlist").commit();


    }
}