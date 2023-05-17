package com.example.euphoric.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.R;
import com.example.euphoric.services.CameraService;
import com.example.euphoric.services.EmotionControllerService;
import com.google.android.gms.tasks.Task;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EmotionControllerActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    TextView language;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    LinearLayout cameraLL;
    LinearLayout controllerContainer;
    LinearLayout progressTextContainer;
    FrameLayout progressBarHolder;
    TextView progressBarMsg;
    TextView progressBarTitle;
    Button searchButton;
    ActivityResultLauncher<Uri> mGetContent;
    List<String> waitTexts;
    Task<Uri> task;
    Uri uri;
    Handler handler;
    Runnable runnable;
    boolean shouldStopHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_controller);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        controllerContainer = findViewById(R.id.controller_content);
        progressTextContainer = findViewById(R.id.progress_text_container);
        progressBarHolder = findViewById(R.id.progressBarHolder);
        cameraLL = findViewById(R.id.camera_ll);
        progressBarMsg = findViewById(R.id.progress_msg);
        progressBarTitle = findViewById(R.id.progress_title);
        SwitchCompat switchCompat = findViewById(R.id.input_type_switch);
        language = findViewById(R.id.language);
        String[] languages = getResources().getStringArray(R.array.languages);
        waitTexts = Arrays.asList(getResources().getStringArray(R.array.waitTexts));
        handler = new Handler(Looper.myLooper());
        shouldStopHandler = true;
        runnable = new Runnable() {
            @Override
            public void run() {
                int max = waitTexts.size();
                int index = (int) (Math.random() * (max));
                progressBarMsg.setText(waitTexts.get(index));
                if (!shouldStopHandler) {
                    handler.postDelayed(this, 1500);
                }
            }
        };

        progressTextContainer.setVisibility(View.GONE);
        handleSpinnerDropdown(languages);
        handleManualSuggestion(switchCompat);
        initialiseCameraActions(switchCompat);
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBarHolder.setVisibility(View.GONE);
        progressTextContainer.setVisibility(View.GONE);
        controllerContainer.setVisibility(View.VISIBLE);
        shouldStopHandler = true;
    }

    @Override
    protected void onStart() {
        shouldStopHandler = false;
        handler.post(runnable);
        super.onStart();
    }

    private void handleManualSuggestion(SwitchCompat switchCompat) {
        AppCompatEditText moodText = findViewById(R.id.mood_input);
        searchButton = findViewById(R.id.search_on_mood);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                if (moodText.getText() == null || moodText.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Enter mood/genre", Toast.LENGTH_SHORT).show();
                else {
                    controllerContainer.setVisibility(View.GONE);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    progressTextContainer.setVisibility(View.VISIBLE);
                    progressBarTitle.setText(R.string.fetchingSongs);
                    progressBarMsg.setText("");
                    new EmotionControllerService(requestQueue, sharedPreferences).controller(switchCompat.isChecked(), EmotionControllerActivity.this, moodText.getText().toString(), language.getText().toString());
                }
            }
        });
    }

    private void initialiseCameraActions(SwitchCompat switchCompat) {
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
                            System.out.println("Language " + language.getText() );
                            task = cs.persistImageAndCallApi(uri, switchCompat.isChecked(), language.getText().toString());
                            new MyTask().execute();
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

    private void handleSpinnerDropdown(String[] arrayList) {
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EmotionControllerActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(750, 900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(EmotionControllerActivity.this
                        , R.layout.text_spinner, arrayList);

                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        language.setText(adapter.getItem(i));
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cameraLL.setEnabled(false);
            searchButton.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            controllerContainer.setVisibility(View.GONE);
            progressBarHolder.setVisibility(View.VISIBLE);
            progressTextContainer.setVisibility(View.VISIBLE);
            progressBarTitle.setText(R.string.detectingMood);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarTitle.setText(R.string.fetchingSongs);
            cameraLL.setEnabled(true);
            searchButton.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!task.isComplete()) ;
            return null;
        }
    }
}