package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.euphoric.R;
import com.example.euphoric.services.MyBounceInterpolator;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        final TextView likedSongsButton = findViewById(R.id.liked_songs_profile);
        likedSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likedSongsButton.startAnimation(myAnim);
                startActivity(new Intent(getApplicationContext(), LikedSongsActivity.class));
            }
        });

        final TextView updateProfileButton = findViewById(R.id.update_profile);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileButton.startAnimation(myAnim);
//                startActivity(new Intent(getApplicationContext(), LikedSongsActivity.class));
            }
        });

        final TextView signOutButton = findViewById(R.id.sign_out_profile);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutButton.startAnimation(myAnim);
//                startActivity(new Intent(getApplicationContext(), LikedSongsActivity.class));
            }
        });
    }
}