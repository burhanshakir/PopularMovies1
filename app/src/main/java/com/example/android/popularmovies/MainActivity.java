package com.example.android.popularmovies;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    GridView gridview;

    String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=352a1b1c2de1621bf936440151a839f5&language=en-US",
            TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=352a1b1c2de1621bf936440151a839f5&language=en-US",
            THUMBNAIL_URL = "http://image.tmdb.org/t/p/w185/",
            json_string;
    String[] title,year,synopsis,rating,thumbnail;

    JSONObject jsonObject;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gvMain);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.selection,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        spinner.setAdapter(adapter);

        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {

            FetchData fetchData = new FetchData(MainActivity.this);
            fetchData.execute(POPULAR_URL);
        }
        else
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting()) {
                    FetchData fetchData = new FetchData(MainActivity.this);
                    if (i == 0)
                        fetchData.execute(POPULAR_URL);
                    else
                        fetchData.execute(TOP_RATED_URL);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private class  FetchData extends AsyncTask<String,Void,String> {
        String add_info_url,JSON_STRING;
        Context ctx;


        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        FetchData(Context ctx) {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute() {
            progress.setMessage("Please Wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... args) {

            add_info_url = args[0];

            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();


                InputStream inputStream = httpURLConnection.getInputStream();
                if(inputStream == null){
                    return null;
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(JSON_STRING+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            json_string = result;
            //Log.d("json_string", json_string);

            try {

                jsonObject = new JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("results");

                title = new String[jsonArray.length()];
                synopsis = new String[jsonArray.length()];
                rating = new String[jsonArray.length()];
                year = new String[jsonArray.length()];
                thumbnail = new String[jsonArray.length()];

                for(int count = 0;count<jsonArray.length();count++)
                {
                    JSONObject jo = jsonArray.getJSONObject(count);

                    title[count] = jo.getString("title");
                    synopsis[count] = jo.getString("overview");
                    year[count] = jo.getString("release_date");
                    thumbnail[count] = THUMBNAIL_URL.concat(jo.getString("poster_path"));
                    rating[count] = jo.getString("vote_average");
                    //Log.d("image",thumbnail[count]);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            gridview.setAdapter(new ImageAdapter(MainActivity.this,thumbnail));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(MainActivity.this,MovieInfo.class);
                    intent.putExtra("title",title[position]);
                    intent.putExtra("synopsis",synopsis[position]);
                    intent.putExtra("rating",rating[position]);
                    intent.putExtra("year",year[position]);
                    intent.putExtra("pic",thumbnail[position]);
                    startActivity(intent);
                }
            });
        }

    }
}

