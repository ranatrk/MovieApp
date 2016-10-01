package com.example.rana.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

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
            MovieDetails movieDetails = new MovieDetails(movieString);

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

        switch (id) {
            case R.id.most_popular:
                return true;
            case R.id.top_rated:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
