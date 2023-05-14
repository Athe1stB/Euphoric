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
import android.widget.EditText;
import android.widget.Toast;

import com.example.euphoric.R;
import com.example.euphoric.models.User;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.MyBounceInterpolator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
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
        final EditText name = ((AppCompatEditText) findViewById(R.id.fullName));
        final EditText email = ((AppCompatEditText) findViewById(R.id.userEmailId));
        final EditText phone = ((AppCompatEditText) findViewById(R.id.mobileNumber));
        final EditText password = ((AppCompatEditText) findViewById(R.id.password));
        final EditText confirmPassword = ((AppCompatEditText) findViewById(R.id.confirmPassword));
        final Button signUpButton = findViewById(R.id.signUpButton);
        final Button redirectToLogin = findViewById(R.id.redirectLoginButton);

        redirectToLogin.setOnClickListener(v -> {
            redirectToLogin.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        signUpButton.setOnClickListener(v -> {
            signUpButton.startAnimation(myAnim);
            if (name.getText().toString().equals("") || name.getText().toString().length() == 0
                    || email.getText().toString().equals("") || email.getText().toString().length() == 0
                    || phone.getText().toString().equals("") || phone.getText().toString().length() == 0
                    || password.getText().toString().equals("") || password.getText().toString().length() == 0
                    || confirmPassword.getText().toString().equals("") || confirmPassword.getText().toString().length() == 0) {
                Toast.makeText(SignUpActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (!(password.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))) {
                Toast.makeText(SignUpActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    List<String> songIds = Collections.emptyList();
                                    User user = new User("01/01/1970", phone.getText().toString(), name.getText().toString().trim(), songIds);
                                    FirestoreService.set(user, "Users", email.getText().toString().trim());
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                    Log.d(TAG, "successfully created user");
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Cannot create User", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}