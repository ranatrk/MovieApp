package com.example.rana.moviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rana on 10/2/16.
 */
public class TrailersAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> trailersArray;


    public TrailersAdapter(Context c, int layoutID,int viewID,ArrayList<String> trailersArray) {
        super(c, layoutID,trailersArray);
        mContext = c;
        this.trailersArray = trailersArray;

    }

    public int getCount() {
        return trailersArray.size();
    }

    public String getTrailer(int position) {
        return trailersArray.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);

        } else {
//            Log.v("convertView",convertView.findViewById(imageViewID).toString());
            textView = (TextView) convertView;
        }

        textView.setText("Trailer "+(position+1));

        return textView;
    }



}
