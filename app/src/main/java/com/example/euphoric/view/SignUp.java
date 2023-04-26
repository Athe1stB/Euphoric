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

        signUpButton.setOnClickListener(v -> {
            signUpButton.startAnimation(myAnim);
            if(name!=null && phone!=null && email!=null && password!=null && confirmPassword!=null) {
                if(password.toString().equals(confirmPassword.toString())){
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
                else{
                    Toast.makeText(SignUp.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(SignUp.this, "Fields empty" + password + " " + confirmPassword, Toast.LENGTH_SHORT).show();
        });
    }
}