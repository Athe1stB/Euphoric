package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.euphoric.R;
import com.example.euphoric.services.MyBounceInterpolator;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

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
                startActivity(new Intent(getApplicationContext(), LikedSongsActivity.class));
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