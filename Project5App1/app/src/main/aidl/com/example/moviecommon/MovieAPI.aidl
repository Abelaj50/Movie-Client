// MovieAPI.aidl
package com.example.moviecommon;

// Declare any non-default types here with import statements

interface MovieAPI {
    String[] getMovieNames();
    String[] getMovieDirectors();
    String[] getMovieLinks();
    String[] getMovieYears();
    String[] getMovieBudgets();
    String[] getMovieBoxOffice();
}