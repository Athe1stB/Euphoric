package com.example.euphoric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.euphoric.view.DashboardActivity;
import com.example.euphoric.view.SignUpActivity;
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
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
            else{
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        }, 2000);

    }
}