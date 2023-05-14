package com.example.euphoric.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.euphoric.R;
import com.example.euphoric.services.FirestoreService;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        TextView email = findViewById(R.id.up_emailId);
        TextView name = findViewById(R.id.up_fullName);
        TextView phone = findViewById(R.id.up_phone);
        AppCompatEditText dob = findViewById(R.id.up_dob);
        Button submit = findViewById(R.id.up_update);

        Intent i = getIntent();
        String emailStr = i.getStringExtra("email");
        String nameStr = i.getStringExtra("name");
        String phoneStr = i.getStringExtra("phone");
        String dobStr = i.getStringExtra("dob");

        email.setText(emailStr);
        name.setText(nameStr);
        phone.setText(phoneStr);
        dob.setText(dobStr);
        email.setEnabled(false);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                Date date = myCalendar.getTime();
                dob.setText(dateFormat.format(date));
            }
        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateProfileActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updatedData = new HashMap<String, Object>() {{
                    put("name", name.getText().toString());
                    put("dob", dob.getText().toString());
                    put("phone", phone.getText().toString());
                }};
                boolean updated = FirestoreService.update(updatedData, "Users", FirebaseAuth.getInstance().getCurrentUser().getEmail(), UpdateProfileActivity.this);
            }
        });
    }
}