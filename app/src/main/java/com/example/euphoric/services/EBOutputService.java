package com.example.euphoric.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.models.Video;
import com.example.euphoric.models.VideoList;
import com.example.euphoric.view.LikedSongsActivity;
import com.example.euphoric.view.VideoActivity;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;

public class EBOutputService {
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    public EBOutputService(RequestQueue requestQueue, SharedPreferences sharedPreferences) {
        this.requestQueue = requestQueue;
        this.sharedPreferences = sharedPreferences;
    }


    public void getResponseOutput(Boolean isVideoInput, Context context){
        if (isVideoInput) {
            YoutubeService youtubeService = new YoutubeService("calm", new String[]{"evergreen", "bollywood"});
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
            ss.getTracks(() -> {
                ArrayList<SpotifySong> songs = ss.getSongs();
                Intent i = new Intent(context, LikedSongsActivity.class);
                i.putExtra("SongList", songs);
                context.startActivity(i);
            });
        }
    }
}
