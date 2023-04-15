package com.example.euphoric.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.euphoric.R;
import com.example.euphoric.models.User;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.MyBounceInterpolator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Editable email = ((AppCompatEditText)findViewById(R.id.signup_email)).getText();
        final Editable password = ((AppCompatEditText)findViewById(R.id.signup_password)).getText();
        final Button signUpButton = findViewById(R.id.signUpButton);
        final Button redirectToLogin = findViewById(R.id.redirectLoginButton);

        redirectToLogin.setOnClickListener(v->{
            redirectToLogin.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        signUpButton.setOnClickListener(v -> {
            signUpButton.startAnimation(myAnim);
            if(email!=null && password!=null) {
                mAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                List<Integer> songIds = Collections.singletonList((1));
                                User user = new User(12, "Biswa", songIds);
                                FirestoreService.set(user, "Users", email.toString());
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                Log.d(TAG, "successfully created user");
                            }
                            else{
                                Toast.makeText(SignUpActivity.this, "Cannot create User", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
            else
                Toast.makeText(SignUpActivity.this, "Fields empty", Toast.LENGTH_SHORT).show();
        });
    }
}