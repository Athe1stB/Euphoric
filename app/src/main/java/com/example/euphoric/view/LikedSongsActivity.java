package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.euphoric.R;
import com.example.euphoric.models.Song;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.models.Video;

import java.util.ArrayList;
import java.util.List;

public class LikedSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_list);

//        List<Integer> songIds = (List<Integer>) i.getExtras().get("SONG_IDS");
        // get songs details using ids.
//        ArrayList<Song> songView = new ArrayList<Song>();
//        String[] gList = {"asdf", "sadf"};
//        songView.add(new Song("Song 1", "hyd", gList, "Arijit", "2mins", "AlbumName1"));
//        songView.add(new Song("Song 2","hyd", gList, "artist1", "2mins", "AlbumName2"));
//        songView.add(new Song("Song 3","hyd", gList, "artist2", "2mins", "AlbumName3"));
//        songView.add(new Song("Song 4","hyd", gList, "artist3", "2mins", "AlbumName4"));

        ArrayList<SpotifySong> songs = (ArrayList<SpotifySong>) getIntent().getSerializableExtra("SongList");

        ListView listView = (ListView) findViewById(R.id.basic_list);
        final LikedSongsAdapter cAdapter = new LikedSongsAdapter(this, songs);
        listView.setAdapter(cAdapter);
    }
}