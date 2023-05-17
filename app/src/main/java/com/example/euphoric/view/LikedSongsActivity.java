package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;

import java.util.ArrayList;

public class LikedSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_list);

        Intent i = getIntent();
        ArrayList<SpotifySong> songs = (ArrayList<SpotifySong>) i.getSerializableExtra("SongList");
        String callerType = i.getStringExtra("caller_type");
        boolean containsSongs = i.getBooleanExtra("contains_songs", false);
        String errorMsg = i.getStringExtra("error_msg");

        if (containsSongs) {
            setContentView(R.layout.basic_list);
            ListView listView = (ListView) findViewById(R.id.basic_list);
            final LikedSongsAdapter cAdapter = new LikedSongsAdapter(this, songs, callerType);
            listView.setDivider(null);
            listView.setAdapter(cAdapter);
        } else {
            setContentView(R.layout.no_tracks);
            TextView tv = findViewById(R.id.track_error_msg);
            tv.setText(errorMsg);
        }
    }
}