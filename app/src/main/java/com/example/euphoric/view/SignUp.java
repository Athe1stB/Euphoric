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
import com.example.euphoric.services.MyBounceInterpolator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Editable name = ((AppCompatEditText) findViewById(R.id.fullName)).getText();
        final Editable email = ((AppCompatEditText)findViewById(R.id.userEmailId)).getText();
        final Editable phone = ((AppCompatEditText)findViewById(R.id.mobileNumber)).getText();
        final Editable password = ((AppCompatEditText)findViewById(R.id.password)).getText();
        final Editable confirmPassword = ((AppCompatEditText)findViewById(R.id.confirmPassword)).getText();
        final Button signUpButton = findViewById(R.id.signUpButton);
        final Button redirectToLogin = findViewById(R.id.redirectLoginButton);
        redirectToLogin.setOnClickListener(v->{
            redirectToLogin.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), Login.class));
        });

        String name1 = name.toString();
        String email1 = email.toString();
        String phone1 = phone.toString();
        String password1 = password.toString();
        String confirmPassword1 = confirmPassword.toString();

        signUpButton.setOnClickListener(v -> {
            signUpButton.startAnimation(myAnim);
            if (name1.equals("") || name1.length() == 0
                    || email1.equals("") || email1.length() == 0
                    || phone1.equals("") || phone1.length() == 0
                    || password1.equals("") || password1.length() == 0
                    || confirmPassword1.equals("") || confirmPassword1.length() == 0) {

                Toast.makeText(SignUp.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else if (!(password.toString().equals(confirmPassword.toString()))){
                Toast.makeText(SignUp.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                    Log.d(TAG, "successfully created user");
                                }
                                else{
                                    Toast.makeText(SignUp.this, "Cannot create User", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}