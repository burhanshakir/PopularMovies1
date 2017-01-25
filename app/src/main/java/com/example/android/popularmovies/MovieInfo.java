package com.example.android.popularmovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieInfo extends AppCompatActivity {

    TextView title,rating,synopsis,year;
    ImageView pic;
    String t,r,s,y,p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        title = (TextView) findViewById(R.id.tvTitle);
        rating = (TextView) findViewById(R.id.tvRating);
        synopsis = (TextView) findViewById(R.id.tvSynopsis);
        year = (TextView) findViewById(R.id.tvYear);
        pic = (ImageView) findViewById(R.id.ivPoster);

        t = getIntent().getStringExtra("title");
        r = getIntent().getStringExtra("rating");
        s = getIntent().getStringExtra("synopsis");
        y = getIntent().getStringExtra("year");
        p = getIntent().getStringExtra("pic");

        title.setText(t);
        rating.setText(r.concat("/10"));
        synopsis.setText(s);
        year.setText(y.substring(0,4));
        Picasso.with(this).load(p).fit().into(pic);
    }
}
