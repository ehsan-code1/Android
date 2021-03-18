package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FragmentPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_phone);
        Bundle dataToPass = getIntent().getExtras();
        SongDetail dFragment= new SongDetail();
        dFragment.setArguments(dataToPass);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,dFragment).commit();

    }
}