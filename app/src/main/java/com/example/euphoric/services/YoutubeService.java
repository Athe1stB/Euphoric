package com.example.euphoric.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeService {
    private final String urlSuffix = "&type=video&key=AIzaSyAJd9FlE9pWQkoUXn3628Luhml4ZBPSTkU";
    private final String urlPrefix = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=200&q=";
    private final String genre;
    private final String[] filters;

    public YoutubeService(String genre, String[] filters){
        this.genre = genre;
        this.filters = filters;
    }

    private String getQueryString(){
        StringBuilder str = new StringBuilder(genre + "+songs");
        for (String filter : filters) {
            str.append("+").append(filter);
        }
        return str.toString();
    }

    public JsonNode suggest() throws IOException {
        String query = getQueryString();
        String queryUrl = urlPrefix + query + urlSuffix;
        return getRequest(queryUrl);
    }

    private JsonNode getRequest(String queryUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(queryUrl)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        if(response.code() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(Objects.requireNonNull(response.body()).string());
        }
        else{
            System.out.println(response.code() + " : " + Objects.requireNonNull(response.body()).string());
            return null;
        }
    }
}
