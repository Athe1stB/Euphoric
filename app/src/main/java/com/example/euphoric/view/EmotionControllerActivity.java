package com.example.euphoric.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.euphoric.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

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
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        String uuid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        StorageReference imageRef = storageRef.child("images/"+ uuid + ".jpg");
                        try {
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                            byte[] data = baos.toByteArray();
                            //uploading the image
                            UploadTask uploadTask = imageRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(EmotionControllerActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    StorageMetadata metadata = taskSnapshot.getMetadata();
                                    Toast.makeText(EmotionControllerActivity.this, "Upload Succeeded", Toast.LENGTH_SHORT).show();
                                    // do further
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
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
}