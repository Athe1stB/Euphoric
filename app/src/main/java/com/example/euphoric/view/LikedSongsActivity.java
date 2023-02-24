package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.euphoric.R;
import com.example.euphoric.models.SongList;

import java.util.ArrayList;

public class LikedSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_list);

        ArrayList<SongList> songListView= new ArrayList<SongList>();
        String[] gList = {"asdf", "sadf"};
        songListView.add(new SongList("Song 1", gList, "Arijit", "2mins", "AlbumName1"));
        songListView.add(new SongList("Song 2", gList, "artist1", "2mins", "AlbumName2"));
        songListView.add(new SongList("Song 3", gList, "artist2", "2mins", "AlbumName3"));
        songListView.add(new SongList("Song 4", gList, "artist3", "2mins", "AlbumName4"));

        ListView listView = (ListView) findViewById(R.id.basic_list);
        final LikedSongsAdapter cAdapter = new LikedSongsAdapter(this,songListView);
        listView.setAdapter(cAdapter);
    }
}