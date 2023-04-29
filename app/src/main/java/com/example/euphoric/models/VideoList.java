package com.example.euphoric.models;

import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class VideoList implements Serializable {
    public ArrayList<Video> videoList;

    public VideoList(JsonNode jsonNode) {
        videoList = new ArrayList<>();
        getVideoList(jsonNode);
    }

    private void getVideoList(JsonNode jsonNode) {
        Iterator<JsonNode> itr = jsonNode.get("items").elements();
        JsonNode current = null;
        while (itr.hasNext()) {
            current = itr.next();
            this.videoList.add(new Video(current));
        }
    }

    public ArrayList<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<Video> videoList) {
        this.videoList = videoList;
    }
}
