package com.example.euphoric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.euphoric.view.Dashboard;
import com.example.euphoric.view.SignUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected  void onStart(){
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(currentUser != null){
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
            else{
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        }, 2000);

    }
}