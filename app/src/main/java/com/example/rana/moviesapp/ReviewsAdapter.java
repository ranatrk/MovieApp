package com.example.rana.moviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rana on 10/2/16.
 */
public class ReviewsAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String> reviewsArray;


    public ReviewsAdapter(Context c, int layoutID,int viewID,ArrayList<String> reviewsArray) {
        super(c, layoutID,reviewsArray);
        mContext = c;
        this.reviewsArray = reviewsArray;

    }

    public int getCount() {
        return reviewsArray.size();
    }

    public String getReview(int position) {
        return reviewsArray.get(position);
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
        try {
            String author = new JSONObject(reviewsArray.get(position)).getString("author");
            String reviewContent = new JSONObject(reviewsArray.get(position)).getString("content");
            textView.setText(author+" : "+ reviewContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return textView;
    }
}
