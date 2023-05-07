package com.example.euphoric.services;

import static java.lang.Math.min;

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
import java.util.List;
import java.util.Map;

public class SpotifyTracksService {
    private ArrayList<SpotifySong> songs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public SpotifyTracksService(RequestQueue queue, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.queue = queue;
    }

    public ArrayList<SpotifySong> getSongs() {
        return songs;
    }

    public void getTracksWithTrackIds(final VolleyCallBack callBack, ArrayList<String> trackIds) {
        if (trackIds.size() > 0) {
            StringBuilder tracks = new StringBuilder();
            for (int i = 0; i < trackIds.size(); i++)
                tracks.append((i > 0) ? "," : "").append(trackIds.get(i));

            String endpoint = "https://api.spotify.com/v1/tracks?ids=" + tracks;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, endpoint, null, response -> {
                        Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("tracks");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                String songId = object.getString("id");
                                String songName = object.getString("name");
                                String songUrl = object.getString("uri");
                                Long duration = object.getLong("duration_ms");
                                String thumbnailUrl = object.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                                String songArtists = "";
                                JSONArray artists = object.getJSONArray("artists");
                                for (int i = 0; i < artists.length(); i++) {
                                    songArtists += ((i > 0) ? ", " : "") + artists.getJSONObject(i).getString("name");
                                }
                                String songAlbum = object.getJSONObject("album").getString("name");
                                SpotifySong song = new SpotifySong(songId, songName, songUrl, songArtists, songAlbum, duration, thumbnailUrl);
                                songs.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccess();
                    }, error -> {
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
        } else
            callBack.onSuccess();
    }

    public void getRecommendations(final VolleyCallBack callBack, ArrayList<String> trackIds) {
        if (trackIds.size() > 0) {
            StringBuilder tracks = new StringBuilder();
            for (int i = 0; i < min(5, trackIds.size()); i++)
                tracks.append((i > 0) ? "," : "").append(trackIds.get(i));

            String endpoint = "https://api.spotify.com/v1/recommendations?market=IN&limit=50&seed_tracks=" + tracks;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, endpoint, null, response -> {
                        Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("tracks");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                String songId = object.getString("id");
                                String songName = object.getString("name");
                                String songUrl = object.getString("uri");
                                Long duration = object.getLong("duration_ms");
                                String thumbnailUrl = object.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                                String songArtists = "";
                                JSONArray artists = object.getJSONArray("artists");
                                for (int i = 0; i < artists.length(); i++) {
                                    songArtists += ((i > 0) ? ", " : "") + artists.getJSONObject(i).getString("name");
                                }
                                String songAlbum = object.getJSONObject("album").getString("name");
                                SpotifySong song = new SpotifySong(songId, songName, songUrl, songArtists, songAlbum, duration, thumbnailUrl);
                                songs.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccess();
                    }, error -> {
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
        } else
            callBack.onSuccess();
    }


}