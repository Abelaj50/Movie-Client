package com.example.project5app2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.moviecommon.MovieAPI;


public class MovieCentral extends Service {

    // Implement the Stub for this Object
    private final MovieAPI.Stub theAPI = new MovieAPI.Stub() {

        public String[] getMovieNames() {
            return getResources().getStringArray(R.array.movieNames);
        }

        public String[] getMovieDirectors() {
            return getResources().getStringArray(R.array.movieDirectors);
        }

        public String[] getMovieLinks() {
            return getResources().getStringArray(R.array.movieLinks);
        }

        public String[] getMovieYears() {
            return getResources().getStringArray(R.array.movieYears);
        }

        public String[] getMovieBudgets() {
            return getResources().getStringArray(R.array.movieBudgets);
        }

        public String[] getMovieBoxOffice() {
            return getResources().getStringArray(R.array.movieBoxOffice);
        }


    };

    public IBinder onBind(Intent intent) { return theAPI; }
}
