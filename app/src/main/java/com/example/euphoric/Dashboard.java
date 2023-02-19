package com.example.euphoric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final ImageButton emotionControllerButton = findViewById(R.id.emotion_controller_button);
        emotionControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                infoButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), EmotionControllerActivity.class));
            }
        });

        final TextView profileButton = findViewById(R.id.my_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                infoButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        final TextView contentRecommendationButton = findViewById(R.id.recommendations);
        contentRecommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                infoButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), ContentRecommendationActivity.class));
            }
        });

        final TextView likedSongsButton = findViewById(R.id.liked_songs);
        likedSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                infoButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), LikedSongsActivity.class));
            }
        });
    }
}