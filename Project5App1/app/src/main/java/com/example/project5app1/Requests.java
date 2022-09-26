package com.example.project5app1;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class Requests extends AppCompatActivity {

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    String[] movieNames;
    String[] movieDirs;
    String[] movieLinks;
    ListView theListView;
    int listLength;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);

        theListView = findViewById(R.id.theListView);
        arrayList = new ArrayList<>();

        try {
            listLength = MovieClient.movieAPI.getMovieNames().length;
            movieNames = MovieClient.movieAPI.getMovieNames();
            movieDirs = MovieClient.movieAPI.getMovieDirectors();
            movieLinks = MovieClient.movieAPI.getMovieLinks();
        } catch (RemoteException e) { e.printStackTrace(); }

        for(int i = 0; i < listLength; i++)
            arrayList.add(i + 1 + ". Name: " + movieNames[i] + ", \n\t  Director: " + movieDirs[i]);

        adapter = new ArrayAdapter<String>(this, R.layout.listview_item, arrayList);
        theListView.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();


        Intent j = new Intent(this, MovieView.class);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                j.putExtra("LINK", movieLinks[i]);
                startActivity(j);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
