package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.SpotifyTracksService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

public class ContentRecommendationActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    Activity activity;
    FrameLayout progressBarHolder;
    TextView progressBarMsg;
    Animation inAnimation;
    Animation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_recommendation);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);
        activity = ContentRecommendationActivity.this;
        progressBarHolder = findViewById(R.id.content_progressBarHolder);
        progressBarMsg = findViewById(R.id.content_progress_title);
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        progressBarMsg.setVisibility(View.VISIBLE);
        progressBarMsg.setText(R.string.fetchingSongs);
        new BackgroundTask().execute();
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarMsg.setVisibility(View.GONE);
                    progressBarHolder.setVisibility(View.GONE);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, Object> ob = FirestoreService.get("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            ArrayList<String> songIds = (ArrayList<String>) ob.get("songIds");

            SpotifyTracksService ss = new SpotifyTracksService(requestQueue, sharedPreferences);
            ss.getRecommendations(() -> {
                ArrayList<SpotifySong> songs = ss.getSongs();
                if (songs.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.basic_list);
                            ListView listView = (ListView) findViewById(R.id.basic_list);
                            final LikedSongsAdapter cAdapter = new LikedSongsAdapter(activity, songs, "recommendations");
                            listView.setDivider(null);
                            listView.setAdapter(cAdapter);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.no_tracks);
                            TextView tv = findViewById(R.id.track_error_msg);
                            tv.setText(getResources().getString(R.string.no_recommendation));
                        }
                    });
                }
            }, songIds);
            return null;
        }
    }
}