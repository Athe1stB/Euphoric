package com.example.euphoric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.euphoric.services.FirebaseAuthService;
import com.example.euphoric.view.Dashboard;
import com.example.euphoric.view.SignUp;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuthService firebaseAuthService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuthService = new FirebaseAuthService();
    }

    @Override
    protected  void onStart(){
        super.onStart();
        FirebaseUser currentUser = firebaseAuthService.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            intent.putExtra("FIREBASE_AUTH_SERVICE", firebaseAuthService);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            intent.putExtra("FIREBASE_AUTH_SERVICE", firebaseAuthService);
            startActivity(intent);
        }
    }
}