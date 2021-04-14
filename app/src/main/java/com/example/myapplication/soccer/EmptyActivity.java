package com.example.myapplication.soccer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EmptyActivity extends AppCompatActivity {

    Bundle dataToPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);


        ImageView articleImg = (ImageView) findViewById(R.id.articleImg);
        TextView pubDate = (TextView) findViewById(R.id.pubDate);
        TextView articleDesc = (TextView) findViewById(R.id.articleDescr);
        TextView articleTitle = (TextView) findViewById(R.id.articleTitle2);

        //set text to info in bundle
        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        articleTitle.setText(getIntent().getStringExtra("title"));

        pubDate.setText(getIntent().getStringExtra("pubDate"));
        articleDesc.setText(getIntent().getStringExtra("descr"));

        //get image url and set image
        Intent intent = getIntent();
        String strImage = String.valueOf(intent.getStringExtra("imageURL"));

        Log.d("LOG_TAG", strImage);
        Picasso.with(this)
                .load(strImage)
                .into(articleImg);

        Button finishButton = (Button) findViewById(R.id.saveButton);
        finishButton.setOnClickListener( clk -> {
            this.finish();
        });
    }
    //opens up article in emulator browser
    public void openToBrowser(View v){
        Bundle dataToPass = getIntent().getExtras();
        String url = getIntent().getStringExtra("url");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    //save article in database for favourite articles
    public void save(View v){
        MyOpener db = new MyOpener(this);
        Article art = new Article(getIntent().getStringExtra("title"), getIntent().getStringExtra("pubDate"), getIntent().getStringExtra("url"), getIntent().getStringExtra("description"), getIntent().getStringExtra("imageURL"), getIntent().getStringExtra("thumbnailImageUrl"));
        db.addArticles(art);
    }
}