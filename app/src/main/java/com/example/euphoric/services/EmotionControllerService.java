package com.example.euphoric.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.models.Video;
import com.example.euphoric.models.VideoList;
import com.example.euphoric.view.LikedSongsActivity;
import com.example.euphoric.view.VideoActivity;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmotionControllerService {
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    private final Map<String, String> emotionMap = new HashMap<String, String>() {{
        put("angry", "negative");
        put("fear", "negative");
        put("disgust", "negative");
        put("sad", "negative");
        put("happy", "positive");
        put("surprise", "positive");
        put("neutral", "neutral");
    }};

    public EmotionControllerService(RequestQueue requestQueue, SharedPreferences sharedPreferences) {
        this.requestQueue = requestQueue;
        this.sharedPreferences = sharedPreferences;
    }

    public void controller(Boolean isVideoInput, Context context, String mood, String language) {
        if (isVideoInput) {
            YoutubeService youtubeService = new YoutubeService(mood, language);
            try {
                JsonNode result = youtubeService.suggest();
                ArrayList<Video> videoList = new VideoList(result).getVideoList();
                Intent i = new Intent(context, VideoActivity.class);
                i.putExtra("VideoList", videoList);
                context.startActivity(i);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "YoutubeService: Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            SpotifySearchService ss = new SpotifySearchService(requestQueue, sharedPreferences);
            Set<SpotifySong> songs = Collections.synchronizedSet(new HashSet<>());
            ss.searchTracks(emotionMap.containsKey(mood) ? emotionMap.get(mood) : "neutral", language, () -> {
                ArrayList<SpotifySong> s = ss.getSongs();
                songs.addAll(s);
                ss.setTotalCounts(ss.getTotalCounts() - 1);
                if (ss.getTotalCounts() == 0) {
                    System.out.println("mood: " + mood + " language: " + language + " total songs: " + songs.size());
                    ArrayList<SpotifySong> songsList = new ArrayList<>(songs);
                    Intent i = new Intent(context, LikedSongsActivity.class);
                    i.putExtra("SongList", songsList);
                    i.putExtra("caller_type", "search");
                    i.putExtra("contains_songs", songsList.size() > 0);
                    i.putExtra("error_msg", context.getResources().getString(R.string.no_tracks));
                    context.startActivity(i);
                }
            });
        }
    }
}
