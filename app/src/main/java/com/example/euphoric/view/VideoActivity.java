package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.euphoric.R;
import com.example.euphoric.models.Video;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_list);

        ArrayList<Video> model = (ArrayList<Video>) getIntent().getSerializableExtra("VideoList");

        ListView listView = (ListView) findViewById(R.id.basic_list);
        final VideoAdapter cAdapter = new VideoAdapter(this, model);
        listView.setAdapter(cAdapter);
    }
}