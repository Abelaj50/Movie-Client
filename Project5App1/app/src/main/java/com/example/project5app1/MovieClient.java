package com.example.project5app1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.moviecommon.MovieAPI;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

/* Credits to https://www.youtube.com/watch?v=741l_fPKL3Y&ab_channel=Stevdza-San for guide on drop down menu styling. */
/* Credits to Freepik for Application Icon: https://www.flaticon.com/free-icon/clapperboard_1038100 */

public class MovieClient extends AppCompatActivity {

    public boolean isBound = false;
    public static MovieAPI movieAPI;
    public Button bindButton;
    public Button unbindButton;
    public Button downloadButton;
    public Button requestListButton;
    public Button requestSelectedButton;
    public TextView statusText;
    public TextView movieNameText;
    public TextView movieDirectorText;
    public TextView movieYearText;
    public TextView movieBudgetText;
    public TextView movieBoxOfficeText;
    public TextInputLayout moviesDropdown;
    public AutoCompleteTextView moviesTextDropdown;
    public ArrayAdapter<String> arrAdapter;
    public VideoView videoView;
    public static int currMovieSelected = -1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Referencing UI elements. */
        moviesDropdown = findViewById(R.id.moviesDropdown);
        moviesTextDropdown = findViewById(R.id.moviesTextDropdown);
        bindButton = findViewById(R.id.bindButton);
        unbindButton = findViewById(R.id.unbindButton);
        downloadButton = findViewById(R.id.downloadButton);
        requestListButton = findViewById(R.id.requestListButton);
        requestSelectedButton = findViewById(R.id.requestSelectedButton);
        statusText = findViewById(R.id.statusText);
        movieNameText = findViewById(R.id.movieNameText);
        movieDirectorText = findViewById(R.id.directorNameText);
        movieYearText = findViewById(R.id.yearText);
        movieBudgetText = findViewById(R.id.budgetText);
        movieBoxOfficeText = findViewById(R.id.boxOfficeText);
        videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        /* On launch, most items are disabled. Once bound, elements are enabled. */
        moviesDropdown.setEnabled(false);
        moviesTextDropdown.setEnabled(false);
        unbindButton.setEnabled(false);
        downloadButton.setEnabled(false);
        requestListButton.setEnabled(false);
        requestSelectedButton.setEnabled(false);

        /* Bind button's listener. */
        bindButton.setOnClickListener(e->{

            /* Enable and disable UI elements. */
            bindButton.setEnabled(false);
            moviesDropdown.setEnabled(false);
            moviesTextDropdown.setEnabled(false);
            unbindButton.setEnabled(true);
            downloadButton.setEnabled(true);
            requestListButton.setEnabled(false);
            requestSelectedButton.setEnabled(false);

            /* If unbound, go ahead and bind to the service. */
            if (!isBound) {
                boolean b = false;
                Intent i = new Intent(MovieAPI.class.getName());
                ResolveInfo info = getPackageManager().resolveService(i, 0);
                i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
                b = bindService(i, this.connection, Context.BIND_AUTO_CREATE);

                if(b) {
                    statusText.setText("Status: Bound");
                    statusText.setTextColor(Color.rgb(61, 220, 132));
                }
            }
        });

        /* Unbind button's listener. */
        unbindButton.setOnClickListener(e->{

            /* If bound, go ahead and unbind to the service. */
            if (isBound) { unbindService(this.connection); }

            statusText.setText("Status: Unbound");
            statusText.setTextColor(Color.rgb(128, 128, 128));

            /* Enable and disable UI elements, end video playback. */
            bindButton.setEnabled(true);
            moviesDropdown.setEnabled(false);
            moviesTextDropdown.setEnabled(false);
            moviesTextDropdown.setText("Select Movie");
            unbindButton.setEnabled(false);
            downloadButton.setEnabled(false);
            requestListButton.setEnabled(false);
            requestSelectedButton.setEnabled(false);
            videoView.stopPlayback();
            videoView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            movieNameText.setText("");
            movieDirectorText.setText("");
            movieYearText.setText("");
            movieBudgetText.setText("");
            movieBoxOfficeText.setText("");
            currMovieSelected = -1;
            isBound = false;
        });

    }


     /* Bind to MovieAPI Service */
    @Override
    protected void onStart() {
        super.onStart();
    }

     /* Unbind from MovieAPI Service */
    @Override
    protected void onStop() {
        super.onStop();
//        if (isBound) { unbindService(this.connection); }
//        moviesTextDropdown.setText("Select Movie");
    }

    private final ServiceConnection connection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            movieAPI = MovieAPI.Stub.asInterface(binder);
            isBound = true;
            handleServiceConnected();
        }

        public void onServiceDisconnected(ComponentName className) {
            movieAPI = null;
            isBound = false;
        }
    };

    /* Method which will be executed once the service has been bound and connected to.
        NOTE: This method also defines certain listeners which require use of the service's API.
        They are enclosed within this method to ensure that they are called after onServiceConnected,
        otherwise the client is not guaranteed to have a reference to the API. */
    private void handleServiceConnected() {

        /* Download button's listener. Downloads movie name information from the service's API. */
        downloadButton.setOnClickListener(e->{
            moviesDropdown.setEnabled(true);
            moviesTextDropdown.setEnabled(true);
            requestListButton.setEnabled(true);
            requestSelectedButton.setEnabled(true);
            downloadButton.setEnabled(false);
            try {
                arrAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, movieAPI.getMovieNames());
                moviesTextDropdown.setAdapter(arrAdapter);
            } catch (RemoteException f) { f.printStackTrace(); }
        });


        /* Movie selector dropdown listener. Changes video view to selected movie trailer. */
        moviesTextDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    currMovieSelected = i;
                    videoView.setVideoPath(movieAPI.getMovieLinks()[i]);
                    videoView.start();
                } catch (RemoteException e) { e.printStackTrace(); }
            }
        });

        /* Request Selected button listener. Gets quick facts for selected movie. */
        requestSelectedButton.setOnClickListener(e->{
            if(currMovieSelected != -1) {
                try {
                    movieNameText.setText(movieAPI.getMovieNames()[currMovieSelected]);
                    movieDirectorText.setText(movieAPI.getMovieDirectors()[currMovieSelected]);
                    movieYearText.setText(movieAPI.getMovieYears()[currMovieSelected]);
                    movieBudgetText.setText(movieAPI.getMovieBudgets()[currMovieSelected]);
                    movieBoxOfficeText.setText(movieAPI.getMovieBoxOffice()[currMovieSelected]);

                } catch (RemoteException remoteException) { remoteException.printStackTrace(); }
            }
        });

        /* Request List button listener. Starts new Activity and shows entire list. */
        requestListButton.setOnClickListener(e->{
            Intent i = new Intent(this, Requests.class);
            startActivity(i);

        });




    }


}
