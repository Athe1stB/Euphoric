package com.example.euphoric.services;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EmotionApiService {
    private String ENDPOINT = "https://7272-115-112-81-246.ngrok-free.app/recommend1";

    public String getMood(String urlImage) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "{\"url_image\":\""+ urlImage + "\"}");

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();

        return (response.body() == null)? "No face" : Objects.requireNonNull(response.body()).string();
    }
}
