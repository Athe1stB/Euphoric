package com.example.euphoric.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.euphoric.R;
import com.example.euphoric.services.MyBounceInterpolator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Editable email = ((AppCompatEditText)findViewById(R.id.login_email)).getText();
        final Editable password = ((AppCompatEditText)findViewById(R.id.login_password)).getText();
        final Button loginButton = findViewById(R.id.loginButton);
        final Button redirectToSignUp = findViewById(R.id.redirectToSignUpButton);

        redirectToSignUp.setOnClickListener(v->{
            redirectToSignUp.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation(myAnim);
                if(email!=null && password!=null) {
                    mAuth.signInWithEmailAndPassword(email.toString(), password.toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                        Log.d(TAG, "successfully created user");
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Cannot log in", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(LoginActivity.this, "Fields empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}