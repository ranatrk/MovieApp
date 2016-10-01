package com.example.rana.moviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> postersArray;
//    private ArrayList<MovieDetails> movieDetailsArray;
//    private int imageViewID;

    public GridViewAdapter(Context c, int layoutID,int imageViewID,ArrayList<String> postersArray) {
        super(c, layoutID,postersArray);
        mContext = c;
        this.postersArray = postersArray;
//        this.movieDetailsArray = new ArrayList<>();
//        this.imageViewID=imageViewID;

    }
//
//    public static void add(String movieStr){
//        super(movieStr);
//    }
//
//    public static void clear(){
//        postersArray.clear();
//    }

    public int getCount() {
        return postersArray.size();
    }
//    public void clear(){
//        super.clear();
////        movieDetailsArray.clear();
//    }
//
//    public void add(String movieStr){
//        postersArray.add(movieStr);
//        MovieDetails movie = new MovieDetails(movieStr);
//        movieDetailsArray.add(movie);
//    }


    public String getMovie(int position) {
        return postersArray.get(position);
    }

//    public MovieDetails getMovieDetails(int position){
//        return movieDetailsArray.get(position);
//    }

//    public long getItemId(int position) {
//        return 0;
//    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
//            Log.v("convertView",convertView.findViewById(imageViewID).toString());
            imageView = (ImageView) convertView;
        }
//        imageView.setImageResource(postersArray[position]);
//        imageView.setImageResource();
//        Log.v("posterUrl","http://image.tmdb.org/t/p/w185/"+postersArray.get(position));
//        Log.v("imageView",imageView.toString());
        try {
            String path = new JSONObject(postersArray.get(position)).getString("poster_path");
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+path).into(imageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+postersArray.get(position)).into(imageView);
//        Picasso.with(mContext).load("/movie_icon.png").into(imageView);

        return imageView;
    }

}