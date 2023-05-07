package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.MyBounceInterpolator;
import com.example.euphoric.services.SpotifyTracksService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final LinearLayout emotionControllerView = findViewById(R.id.emotion_controller_view);
        emotionControllerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionControllerView.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), EmotionControllerActivity.class));
            }
        });

        final ImageButton emotionControllerButton = findViewById(R.id.emotion_controller_button);
        emotionControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionControllerButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), EmotionControllerActivity.class));
            }
        });

        final TextView profileButton = findViewById(R.id.my_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        final TextView contentRecommendationButton = findViewById(R.id.recommendations);
        contentRecommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentRecommendationButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), ContentRecommendationActivity.class));
            }
        });

        final TextView likedSongsButton = findViewById(R.id.liked_songs);
        likedSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likedSongsButton.startAnimation(myAnim);
                Map<String, Object> ob = FirestoreService.get("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                ArrayList<String> songIds = (ArrayList<String>)ob.get("songIds");
                SpotifyTracksService ss = new SpotifyTracksService(requestQueue, sharedPreferences);
                ss.getTracksWithTrackIds(() -> {
                    ArrayList<SpotifySong> songs = ss.getSongs();
                    Intent i = new Intent(getApplicationContext(), LikedSongsActivity.class);
                    i.putExtra("SongList", songs);
                    i.putExtra("caller_type", "Dashboard");
                    i.putExtra("contains_songs", songs.size()>0);
                    i.putExtra("error_msg", getResources().getString(R.string.no_liked_songs));
                    startActivity(i);
                }, songIds);
            }
        });

        final TextView feedbackButton = findViewById(R.id.feedback);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackButton.startAnimation(myAnim);
//                startActivity(new Intent(getApplicationContext(), LikedSongsActivity.class));
            }
        });

        final TextView signOutButton = findViewById(R.id.signOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutButton.startAnimation(myAnim);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}