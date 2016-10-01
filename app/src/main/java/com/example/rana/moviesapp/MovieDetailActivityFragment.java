package com.example.rana.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        if (intent != null) {
            String movieString =intent.getStringExtra("MovieDetails");
            MovieDetails movieDetails = new MovieDetails(movieString);

            ((TextView) rootView.findViewById(R.id.movie_title))
                    .setText(movieDetails.getOriginal_title());

            ((TextView) rootView.findViewById(R.id.movie_year))
                    .setText(movieDetails.getRelease_date());

            ((TextView) rootView.findViewById(R.id.movie_avg_rating))
                    .setText(movieDetails.getVote_average()+"");

            ((TextView) rootView.findViewById(R.id.movie_overview))
                    .setText(movieDetails.getOverview());
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



}
