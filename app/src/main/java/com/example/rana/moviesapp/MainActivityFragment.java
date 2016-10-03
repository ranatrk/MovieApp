package com.example.rana.moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private GridViewAdapter moviesAdapter;
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_main, container, false);
        moviesAdapter = new GridViewAdapter(
                getActivity(), // The current context (this activity)
                R.layout.fragment_main, // The name of the layout ID.
                R.id.poster_image_view,
                new ArrayList()
        );


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String movieStr = moviesAdapter.getMovie(position);
//                Log.v(LOG_TAG,"the movie:  "+movie);
                //   http://image.tmdb.org/t/p/w185/movie

                //movieStr is the whole JSON object without parsing and getting the data
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("MovieDetails", movieStr);
                startActivity(intent);
//                Intent i = getIntent();
//                Deneme dene = (Deneme)i.getSerializableExtra("sampleObject");
            }
        });


        return rootView;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.most_popular:
//                return true;
//            case R.id.top_rated:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    private void updateMovies(){
        FetchMoviesTask moviesTask = new FetchMoviesTask();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String preferenceorder = prefs.getString(getString(R.string.pref_default_order);
        moviesTask.execute();

    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        private String[] getMoviesDataFromJSON(String popularMoviesJSONStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
//            final String  results= "results";
//            overview,release_date,original_title,backdrop_path
            JSONObject popularMoviesJSON = new JSONObject(popularMoviesJSONStr);
            JSONArray moviesArray = popularMoviesJSON.getJSONArray("results");

            String[] resultStrs = new String[moviesArray.length()];

            // we start storing the values in a database.
            //Preferences showing app by most popular or top rated
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String orderPref = sharedPrefs.getString(
                    getString(R.string.pref_popular),
                    getString(R.string.pref_top_rated));

            for(int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                resultStrs[i] = movieObject.toString();
//                resultStrs[i] = movieObject.getString("poster_path");
            }

            return resultStrs;

        }


        @Override
        protected String[] doInBackground(Void... params) {


            // If there's no params, there's nothing to look up.  Verify size of params.
//            if (params.length == 0) {
//                return null;
//            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String popularMoviesJSON = null;

            String format = "json";
            try {
                final String PopularMovies_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(PopularMovies_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.Movies_App_API_KEY)
                        .build();
                Log.v("builtUri",builtUri.toString());
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popularMoviesJSON = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error:"+e.getMessage(), e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
//                Log.v(LOG_TAG,popularMoviesJSON.toString());
                return getMoviesDataFromJSON(popularMoviesJSON);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the movie.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                moviesAdapter.clear();
                for(String movieStr : result) {
//                    Log.v("rana",movieStr);
                    moviesAdapter.add(movieStr);
                }
                moviesAdapter.notifyDataSetChanged();

            }
        }
    }
}
