package com.example.euphoric.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeService {
    private final String urlSuffix = "&type=video&key=AIzaSyAJd9FlE9pWQkoUXn3628Luhml4ZBPSTkU";
    private final String urlPrefix = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=200&q=";
    private final String genre;
    private final String language;
    private final Map<String, String> genreMap = new HashMap<String, String>() {{
        put("angry", "peaceful");
        put("fear", "devotional");
        put("disgust", "peaceful");
        put("sad", "peaceful");
        put("happy", "party");
        put("surprise", "dance");
        put("neutral", "popular");
    }};

    public YoutubeService(String mood, String language) {
        this.genre = genreMap.containsKey(mood) ? genreMap.get(mood) : "popular";
        this.language = language;
    }

    private String getQueryString() {
        return language.equals("No Preference") ?
                genre + " songs"
                : language + " " + genre + " songs";
    }

    public JsonNode suggest() throws IOException {
        String queryUrl = urlPrefix + getQueryString() + urlSuffix;
        return getRequest(queryUrl);
    }

    private JsonNode getRequest(String queryUrl) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();
        Request request = new Request.Builder()
                .url(queryUrl)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.code() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(Objects.requireNonNull(response.body()).string());
        } else {
            System.out.println(response.code() + " : " + Objects.requireNonNull(response.body()).string());
            return null;
        }
    }
}
