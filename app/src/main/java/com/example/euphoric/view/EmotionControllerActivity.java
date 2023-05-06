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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.R;
import com.example.euphoric.services.CameraService;
import com.example.euphoric.services.EmotionControllerService;
import com.google.android.gms.tasks.Task;

import java.io.File;

public class EmotionControllerActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    LinearLayout cameraLL;
    LinearLayout controllerContainer;
    FrameLayout progressBarHolder;
    TextView progressBarText;
    ActivityResultLauncher<Uri> mGetContent;
    Task<Uri> task;
    Uri uri;
    MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_controller);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        myTask = new MyTask();
        controllerContainer = findViewById(R.id.controller_content);
        progressBarHolder = findViewById(R.id.progressBarHolder);
        progressBarText = findViewById(R.id.progress_text);
        cameraLL = findViewById(R.id.camera_ll);
        progressBarText.setVisibility(View.GONE);
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
        File file = new File(getFilesDir(), "external_files");
        uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        mGetContent = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            CameraService cs = new CameraService(EmotionControllerActivity.this, requestQueue, sharedPreferences);
                            task = cs.persistImageAndCallApi(uri, switchCompat.isChecked());
                            myTask.execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not capture image", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        cameraLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch(uri);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBarHolder.setVisibility(View.GONE);
        progressBarText.setVisibility(View.GONE);
        controllerContainer.setVisibility(View.VISIBLE);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cameraLL.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            progressBarText.setVisibility(View.VISIBLE);
            progressBarText.setText("Detecting mood...");
            controllerContainer.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarText.setText("Fetching content for you...");
            cameraLL.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(!task.isComplete());
            return null;
        }
    }
}