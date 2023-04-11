package com.example.euphoric.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.euphoric.R;

import java.io.File;
import java.nio.file.Files;

public class EmotionControllerActivity extends AppCompatActivity {

    private static final int pic_id = 123;
    // Define the button and imageview type variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_controller);

        final LinearLayout cameraLL = findViewById(R.id.camera_ll);
        File file = new File(getFilesDir(), "external_files");
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        ActivityResultLauncher<Uri> mGetContent = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        // do what you need with the uri here ...
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