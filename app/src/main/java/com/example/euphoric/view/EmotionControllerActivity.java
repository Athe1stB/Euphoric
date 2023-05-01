package com.example.euphoric.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.R;
import com.example.euphoric.services.CameraService;
import com.example.euphoric.services.EmotionControllerService;

import java.io.File;

public class EmotionControllerActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_controller);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        SwitchCompat switchCompat = findViewById(R.id.input_type_switch);
        handleManualSuggestion(switchCompat);
        initialiseCameraActions(switchCompat);
    }

    private void handleManualSuggestion(SwitchCompat switchCompat){
        AppCompatEditText moodText = findViewById(R.id.mood_input);
        Button searchButton = findViewById(R.id.search_on_mood);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moodText.getText() == null || moodText.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Enter mood/genre", Toast.LENGTH_SHORT).show();
                else {
                    new EmotionControllerService(requestQueue, sharedPreferences).controller(switchCompat.isChecked(), EmotionControllerActivity.this, moodText.getText().toString(), new String[]{"bollywood"});
                }
            }
        });
    }

    private void initialiseCameraActions(SwitchCompat switchCompat){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final LinearLayout cameraLL = findViewById(R.id.camera_ll);
        File file = new File(getFilesDir(), "external_files");
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        ActivityResultLauncher<Uri> mGetContent = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result){
                            CameraService.persistImageAndCallApi(EmotionControllerActivity.this, uri, switchCompat.isChecked(), requestQueue, sharedPreferences);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Could not capture image", Toast.LENGTH_LONG).show();
                    }
                });
        cameraLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch(uri);
            }
        });
    }
}