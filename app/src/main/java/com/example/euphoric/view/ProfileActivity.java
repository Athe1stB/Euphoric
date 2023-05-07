package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.MyBounceInterpolator;
import com.example.euphoric.services.SpotifyTracksService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;

        Map<String, Object> user = FirestoreService.get("Users", firebaseUser.getEmail());
        TextView name = findViewById((R.id.user_profile_name));
        name.setText(Objects.requireNonNull(user.get("name")).toString());

        TextView email = findViewById((R.id.user_email));
        email.setText(Objects.requireNonNull(user.get("email")).toString());

        final TextView likedSongsButton = findViewById(R.id.liked_songs_profile);
        likedSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likedSongsButton.startAnimation(myAnim);
                Map<String, Object> ob = FirestoreService.get("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                ArrayList<String> songIds = (ArrayList<String>) ob.get("songIds");
                SpotifyTracksService ss = new SpotifyTracksService(requestQueue, sharedPreferences);
                ss.getTracksWithTrackIds(() -> {
                    ArrayList<SpotifySong> songs = ss.getSongs();
                    Intent i = new Intent(getApplicationContext(), LikedSongsActivity.class);
                    i.putExtra("SongList", songs);
                    i.putExtra("caller_type", "Profile");
                    i.putExtra("contains_songs", songs.size() > 0);
                    i.putExtra("error_msg", getResources().getString(R.string.no_liked_songs));
                    startActivity(i);
                }, songIds);
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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}