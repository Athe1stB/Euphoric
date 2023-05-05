package com.example.euphoric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.models.SpotifyUser;
import com.example.euphoric.services.SpotifyUserService;
import com.example.euphoric.view.DashboardActivity;
import com.example.euphoric.view.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private static final int REQUEST_CODE = 1234;

    private static final String SCOPES
            = "user-read-email,user-read-private,playlist-modify-private,playlist-modify-public";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        authenticateSpotify();
        initializeFirebaseAuth();

        requestQueue = Volley.newRequestQueue(this);
    }

    private void initializeFirebaseAuth() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (currentUser != null) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        }, 2000);
    }

    private void authenticateSpotify() {
        // Initialize shared preferences and volley request queue
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(getString(R.string.CLIENT_ID), AuthorizationResponse.Type.TOKEN, getString(R.string.REDIRECT_URI));
        builder.setScopes(new String[]{SCOPES});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    editor = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE).edit();
                    editor.putString("token", response.getAccessToken());
                    System.out.println(response.getAccessToken());
                    editor.apply();
                    waitForUserInfo();
                    Toast.makeText(this, "Spotify Auth successful", Toast.LENGTH_SHORT).show();
                    break;

                case ERROR:
                    Toast.makeText(this, "Spotify Auth Error", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(this, "Spotify Auth: Unexpected Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void waitForUserInfo() {
        SpotifyUserService userService = new SpotifyUserService(requestQueue, sharedPreferences);
        userService.get(() -> {
            SpotifyUser user = userService.getUser();
            editor = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE).edit();
            editor.putString("userid", user.id);
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();
        });
    }
}