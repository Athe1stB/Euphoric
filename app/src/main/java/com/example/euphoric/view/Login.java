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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.euphoric.R;
import com.example.euphoric.services.MyBounceInterpolator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final EditText email = ((AppCompatEditText)findViewById(R.id.login_email));
        final EditText password = ((AppCompatEditText)findViewById(R.id.login_password));
        final TextView forgotPassword = (TextView) findViewById(R.id.forgot_password);
        final CheckBox show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        final Button loginButton = findViewById(R.id.loginButton);
        final Button redirectToSignUp = findViewById(R.id.redirectToSignUpButton);


        redirectToSignUp.setOnClickListener(v->{
            redirectToSignUp.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), SignUp.class));
        });

        show_hide_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    show_hide_password.setText(R.string.hide_pwd);
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    show_hide_password.setText(R.string.show_pwd);// change
                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation(myAnim);
                if (email.getText().equals("") || email.getText().length() == 0
                        || password.getText().equals("") || password.getText().length() == 0) {
                    Toast.makeText(Login.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        Log.d(TAG, "successfully created user");
                                    }
                                    else{
                                        Toast.makeText(Login.this, "Cannot log in", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}