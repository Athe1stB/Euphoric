package com.example.euphoric.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.euphoric.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        AppCompatEditText currentPassword = findViewById(R.id.upass_currentpass);
        AppCompatEditText password = findViewById(R.id.upass_password);
        AppCompatEditText confirmPassword = findViewById(R.id.upass_confirmPassword);
        Button submit = findViewById(R.id.upass_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPassword.getText().toString().equals("") || currentPassword.getText().toString().length() == 0 ||
                password.getText().toString().equals("") || password.getText().toString().length() == 0
                        || confirmPassword.getText().toString().equals("") || confirmPassword.getText().toString().length() == 0) {
                    Toast.makeText(UpdatePasswordActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!(password.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))) {
                    Toast.makeText(UpdatePasswordActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(
                            firebaseUser.getEmail(),
                            currentPassword.getText().toString()
                    );
                    firebaseUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        firebaseUser.updatePassword(password.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }
}