package com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    String[] mThumbsIds;

    public ImageAdapter(Context c,String[] t) {
        mContext = c;
        mThumbsIds = t;
    }

    public int getCount() {
        return mThumbsIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,800));
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
            imageView.setLayoutParams(marginLayoutParams);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        Picasso.with(mContext).load(mThumbsIds[position]).fit().into(imageView);
        return imageView;
    }

    /*private Integer[] mThumbIds = {
            R.drawable.mechanic, R.drawable.call,
            R.drawable.key, R.drawable.navigation,
            R.drawable.share, R.drawable.transport
    };*/
}
