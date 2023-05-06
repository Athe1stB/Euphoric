package com.example.euphoric.services;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.euphoric.models.SpotifySong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpotifySearchService {
    private String[] positiveMoodSongs = new String[]{"afrobeat", "alt-rock", "alternative", "black-metal", "bluegrass", "blues", "bossanova", "brazil", "breakbeat", "cantopop", "chicago-house", "club", "comedy", "dance", "dancehall", "death-metal", "deep-house", "detroit-techno", "disco", "drum-and-bass", "dub", "dubstep", "edm", "electro", "electronic", "forro", "funk", "garage", "goth", "grindcore", "groove", "grunge", "hard-rock", "hardcore", "hardstyle", "heavy-metal", "hip-hop", "house", "idm", "indie-pop", "industrial", "iranian", "j-dance", "j-idol", "j-pop", "j-rock", "jazz", "k-pop", "latin", "latino", "malay", "mandopop", "metal", "metal-misc", "metalcore", "minimal-techno", "mpb", "opera", "pagode", "party", "philippines-opm", "pop", "pop-film", "post-dubstep", "power-pop", "progressive-house", "psych-rock", "punk", "punk-rock", "r-n-b", "reggae", "reggaeton", "road-trip", "rock", "rock-n-roll", "rockabilly", "salsa", "samba", "ska", "soul", "summer", "synth-pop", "tango", "techno", "trance", "trip-hop", "work-out" };
    private String[] negativeMoodSongs = new String[]{"romance", "acoustic", "ambient", "children", "chill", "classical", "country", "gospel", "guitar", "happy", "holidays", "honky-tonk", "kids", "new-age", "piano", "sleep", "study", "folk" };
    private String[] sad = new String[]{"emo", "sad", "sertanejo", "rainy-day" };
    private ArrayList<SpotifySong> songs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public void setTotalCounts(int totalCounts) {
        this.totalCounts = totalCounts;
    }

    private int totalCounts;

    public SpotifySearchService(RequestQueue queue, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.queue = queue;
    }

    public ArrayList<SpotifySong> getSongs() {
        return songs;
    }

    public int getTotalCounts(){
        return totalCounts;
    }

    private ArrayList<String> getSpotifyQueryString(String genreMapping) {
        ArrayList<String> queries = new ArrayList<>();
        if (genreMapping.equals("positive")) {
            for (String positiveMoodSong : positiveMoodSongs) {
                queries.add("https://api.spotify.com/v1/search?q=genre%3a" + positiveMoodSong + "&type=track&limit=5");
            }
        } else if (genreMapping.equals("negative")) {
            for (String negativeMoodSong : negativeMoodSongs) {
                queries.add("https://api.spotify.com/v1/search?q=genre%3a" + negativeMoodSong + "&type=track&limit=5");
            }
        } else
            queries.add("https://api.spotify.com/v1/search?type=track&limit=50");
        return queries;
    }

    private JsonObjectRequest getJsonObjectRequest(String endpoint, final VolleyCallBack callBack) {
        return new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray = response.optJSONObject("tracks").optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            String songId = object.getString("id");
                            String songName = object.getString("name");
                            String songUrl = object.getString("uri");
                            StringBuilder songArtists = new StringBuilder();
                            JSONArray artists = object.getJSONArray("artists");
                            for (int i = 0; i < artists.length(); i++) {
                                songArtists.append((i > 0) ? ", " : "").append(object.getString("name"));
                            }
                            String songAlbum = object.getJSONObject("album").getString("name");
                            SpotifySong song = new SpotifySong(songId, songName, songUrl, songArtists.toString(), songAlbum);
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
    }

    public void searchTracks(String genreMapping, final VolleyCallBack callBack) {
        ArrayList<String> endpoints = getSpotifyQueryString(genreMapping);
        totalCounts = endpoints.size();
        for (String endpoint : endpoints) {
            queue.add(getJsonObjectRequest(endpoint, callBack));
        }
    }
}
