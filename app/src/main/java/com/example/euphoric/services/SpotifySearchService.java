package com.example.euphoric.services;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.euphoric.models.SpotifySong;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpotifySearchService {
    private ArrayList<SpotifySong> songs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String mood;

    public SpotifySearchService(RequestQueue queue, SharedPreferences sharedPreferences, String mood) {
        this.sharedPreferences = sharedPreferences;
        this.queue = queue;
        this.mood = mood;
    }

    public ArrayList<SpotifySong> getSongs() {
        return songs;
    }

    public void getTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/search?q=bollywood%20" + mood + "%20songs&type=track";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONObject("tracks").optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            String songId = object.getString("id");
                            String songName = object.getString("name");
                            String songUrl = object.getString("href");
                            String songArtists = "";
                            JSONArray artists = object.getJSONArray("artists");
                            for(int i=0; i<artists.length(); i++){
                                songArtists+= ((i>0)?", ":"") + object.getString("name");
                            }
                            String songAlbum = object.getJSONObject("album").getString("name");
                            SpotifySong song = new SpotifySong(songId, songName, songUrl, songArtists, songAlbum);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error
                    System.out.println("Error fetching songs");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

}