package com.example.rana.moviesapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Rana on 9/29/16.
 */
public class MovieDetails implements Serializable{

    private String movieString;
    private String poster_path;
    private String overview;
    private String release_date;
    private String original_title;
    private double vote_average;


    private int movieID;

    public MovieDetails(String movieString){
        this.movieString = movieString;
        try {
            JSONObject movieObject = new JSONObject(movieString);
            poster_path = movieObject.getString("poster_path");
            overview = movieObject.getString("overview");
            release_date = movieObject.getString("release_date");
            original_title = movieObject.getString("original_title");
            vote_average = movieObject.getDouble("vote_average");
            movieID = movieObject.getInt("id");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MovieDetails","could not parse string to json object");
        }

    }


    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getMovieString() {
        return movieString;
    }

    public void setMovieString(String movieString) {
        this.movieString = movieString;
    }


    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }


}

