package com.example.project5app1;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MovieView extends AppCompatActivity {
    public VideoView movieView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_view);

        movieView = findViewById(R.id.movieView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(movieView);
        movieView.setMediaController(mediaController);
    }


    @Override
    protected void onStart() {
        super.onStart();
        String link = getIntent().getStringExtra("LINK");
        movieView.setVideoPath(link);
        movieView.start();
    }
}
