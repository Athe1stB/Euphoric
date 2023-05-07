package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.models.Video;
import com.example.euphoric.models.VideoList;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.SpotifyTracksService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

public class ContentRecommendationActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        Map<String, Object> ob = FirestoreService.get("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ArrayList<String> songIds = (ArrayList<String>) ob.get("songIds");

        SpotifyTracksService ss = new SpotifyTracksService(requestQueue, sharedPreferences);
        ss.getRecommendations(() -> {
            ArrayList<SpotifySong> songs = ss.getSongs();
            if (songs.size() > 0) {
                setContentView(R.layout.basic_list);
                ListView listView = (ListView) findViewById(R.id.basic_list);
                final LikedSongsAdapter cAdapter = new LikedSongsAdapter(this, songs, "Recommendations");
                listView.setAdapter(cAdapter);
            } else {
                setContentView(R.layout.no_tracks);
                TextView tv = findViewById(R.id.track_error_msg);
                tv.setText(getResources().getString(R.string.no_recommendation));
            }
        }, songIds);
    }
}