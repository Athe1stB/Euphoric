package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.euphoric.R;
import com.example.euphoric.services.FirebaseAuthService;
import com.example.euphoric.services.MyBounceInterpolator;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        FirebaseAuthService firebaseAuthService = (FirebaseAuthService) getIntent().getSerializableExtra("FIREBASE_AUTH_SERVICE");

        final Editable email = ((AppCompatEditText)findViewById(R.id.email)).getText();
        if(email == null)
        {
            Toast.makeText(SignUp.this, "Enter Email", Toast.LENGTH_SHORT).show();
        }

        final Editable password = ((AppCompatEditText)findViewById(R.id.password)).getText();
        if(password == null)
        {
            Toast.makeText(SignUp.this, "Enter password", Toast.LENGTH_SHORT).show();
        }

        final Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton.startAnimation(myAnim);
                if(email!=null && password!=null)
                    firebaseAuthService.signUpUser(email.toString(), password.toString());
                startActivity(new Intent(getApplicationContext(), LikedSongsActivity.class));
            }
        });
    }
}