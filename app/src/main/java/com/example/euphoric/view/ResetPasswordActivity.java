package com.example.euphoric.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.euphoric.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final EditText email = ((AppCompatEditText) findViewById(R.id.rp_email));
        final Button resetButton = findViewById(R.id.rp_button);
        final Button login = findViewById(R.id.rp_login);
        final TextView msg = findViewById(R.id.rp_msg);
        final ImageView logo = findViewById(R.id.rp_logo);
        final ImageView success = findViewById(R.id.rp_success);
        final TextView title = findViewById(R.id.rp_title);
        msg.setVisibility(View.GONE);
        success.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();

                if (emailStr.equals("") || emailStr.length() == 0) {
                    Toast.makeText(ResetPasswordActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(emailStr)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        email.setVisibility(View.GONE);
                                        resetButton.setVisibility(View.GONE);
                                        title.setVisibility(View.GONE);
                                        logo.setVisibility(View.GONE);
                                        success.setVisibility(View.VISIBLE);
                                        msg.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });
    }
}