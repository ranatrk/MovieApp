package com.example.rana.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
public class MovieDetailActivityFragment extends Fragment {
    private MovieDetails movieDetails = null;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    public MovieDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MovieDetails")) {
            String movieString =intent.getStringExtra("MovieDetails");
            movieDetails = new MovieDetails(movieString);

            ((TextView) rootView.findViewById(R.id.movie_title))
                    .setText(movieDetails.getOriginal_title());

            ((TextView) rootView.findViewById(R.id.movie_year))
                    .setText(movieDetails.getRelease_date().substring(0,4));

            ((TextView) rootView.findViewById(R.id.movie_avg_rating))
                    .setText(movieDetails.getVote_average()+"/10");

            ((TextView) rootView.findViewById(R.id.movie_overview))
                    .setText(movieDetails.getOverview());

            ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_poster);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/"+movieDetails.getPoster_path()).into(imageView);
        }

        trailersAdapter =
                new TrailersAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.fragment_movie_detail, // The name of the layout ID.
                        R.id.trailer_textview, // The ID of the textview to populate.
                        new ArrayList());
        ListView trailersView = (ListView)rootView.findViewById(R.id.listViewTrailers);
        trailersView.setAdapter(trailersAdapter);
        trailersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String trailerStr = trailersAdapter.getTrailer(position);
                try {
                    JSONObject movieObject = new JSONObject(trailerStr);
                    int movieId = movieObject.getInt("id");
                    Intent intentApp = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieId));
//                    Intent intentWeb = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://www.youtube.com/watch?v=" + movieId));
//                    try {
                        startActivity(intentApp);
//                    } catch (ActivityNotFoundException ex) {
//                        startActivity(intentWeb);
//                    }
                } catch (JSONException e) {
                    Log.e("JSONException",e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        reviewsAdapter =
                new ReviewsAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.fragment_movie_detail, // The name of the layout ID.
                        R.id.review_textview, // The ID of the textview to populate.
                        new ArrayList());
        ListView reviewsView = (ListView)rootView.findViewById(R.id.listViewReviews);
        reviewsView.setAdapter(reviewsAdapter);
//        reviewsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String reviewStr = reviewsAdapter.getReview(position);
////                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
////                        .putExtra("MovieDetails", movieStr);
////                startActivity(intent);
//            }
//        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail, menu);

        // Retrieve the share menu item
//        MenuItem menuItem = menu.findItem(R.id.action_share);

//        // Get the provider and hold onto it to set/change the share intent.
//        ShareActionProvider mShareActionProvider =
//                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//        // Attach an intent to this ShareActionProvider.  You can update this at any time,
//        // like when the user selects a new piece of data they might like to share.
//        if (mShareActionProvider != null ) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        } else {
//            Log.d(LOG_TAG, "Share Action Provider is null?");
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        switch (id) {
//            case R.id.most_popular:
//                return true;
//            case R.id.top_rated:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);

//        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart(){
        super.onStart();
        FetchTrailers trailersTask = new FetchTrailers();
        FetchReviews reviewsTask = new FetchReviews();

        trailersTask.execute();
        reviewsTask.execute();
    }

    private String fetchJSON(HttpURLConnection urlConnection,BufferedReader reader,String MOVIE_BASE_URL) throws IOException {
        // Construct the URL for the query
        //"http://api.themoviedb.org/3/movie/{id}/videos?"

        final String APPID_PARAM = "api_key";

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(APPID_PARAM, BuildConfig.Movies_App_API_KEY)
                .build();
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
        return buffer.toString();

    }

    private String[] getStrArrayFromJSON(String JSONStr) throws JSONException {
        JSONObject JSONObject = new JSONObject(JSONStr);
        JSONArray JSONArray = JSONObject.getJSONArray("results");

        String[] resultStrs = new String[JSONArray.length()];

        for(int i = 0; i < JSONArray.length(); i++) {
            JSONObject singleObject = JSONArray.getJSONObject(i);
            resultStrs[i] = singleObject.toString();
        }
        return resultStrs;
    }

    public class FetchTrailers extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchTrailers.class.getSimpleName();


        @Override
        protected String[] doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String trailersJSON = null;
            String format = "json";
            //Fetching Trailers
            try {
                final String MOVIE_TRAILERS_BASE_URL =  "http://api.themoviedb.org/3/movie/"+movieDetails.getMovieID()+"/videos?";
                trailersJSON = fetchJSON(urlConnection,reader,MOVIE_TRAILERS_BASE_URL);

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
            Log.v("trailers",trailersJSON);
            try {
//                Log.v(LOG_TAG,popularMoviesJSON.toString());
                return getStrArrayFromJSON(trailersJSON);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                trailersAdapter.clear();
                for(String trailerStr : result) {
                    Log.v("trailer",trailerStr);
                    trailersAdapter.add(trailerStr);
                }
                trailersAdapter.notifyDataSetChanged();

            }
        }
    }

    public class FetchReviews extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchReviews.class.getSimpleName();

        @Override
        protected String[] doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String reviewsJSON = null;

            String format = "json";

            //Fetching reviews
            try{
                final String MOVIE_REVIEWS_BASE_URL =  "http://api.themoviedb.org/3/movie/"+movieDetails.getMovieID()+"/reviews?";
                reviewsJSON = fetchJSON(urlConnection,reader,MOVIE_REVIEWS_BASE_URL);

            } catch (IOException e){
                Log.e(LOG_TAG, "Error:"+e.getMessage(), e);
                return null;
            } finally{
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
            Log.v("reviews",reviewsJSON);

            try {
//                Log.v(LOG_TAG,popularMoviesJSON.toString());
                return getStrArrayFromJSON(reviewsJSON);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                reviewsAdapter.clear();
                for(String reviewStr : result) {
                    Log.v("Review",reviewStr);
                    reviewsAdapter.add(reviewStr);
                }
                reviewsAdapter.notifyDataSetChanged();

            }
        }
    }





}
