package com.example.myapplication.soccer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.myapplication.R.layout.article_layout;

public class ArticleList extends ArrayAdapter<Article> {
    private Activity context;
    private List<Article> articles;

    public ArticleList(Activity context, List<Article> articles) {
        super(context, article_layout, articles);
        this.context = context;
        this.articles = articles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Article art = articles.get(position);

        View listViewItem = inflater.inflate(article_layout, null, true);
        ImageView thumbnail = (ImageView) listViewItem.findViewById(R.id.thumbnail);
        TextView date = (TextView) listViewItem.findViewById(R.id.date);
        TextView title = (TextView) listViewItem.findViewById(R.id.articleTitle);

        //if thumbnail already loaded
        if (art.hasThumbnailImage()) {
            Drawable d = art.getThumbnailImage();
            thumbnail.setImageDrawable(d);
        }


        date.setText(art.getPubDate());
        title.setText(art.getTitle());
        return listViewItem;
    }

    public int getCount() {
        return articles.size();
    }


}


