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

        ArrayList<SpotifySong> songs = (ArrayList<SpotifySong>) getIntent().getSerializableExtra("SongList");

        ListView listView = (ListView) findViewById(R.id.basic_list);
        final LikedSongsAdapter cAdapter = new LikedSongsAdapter(this, songs);
        listView.setAdapter(cAdapter);
    }
}