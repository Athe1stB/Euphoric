package com.example.euphoric.services;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class CameraService {

    Context context;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    public CameraService(Context context, RequestQueue requestQueue, SharedPreferences sharedPreferences) {
        this.context = context;
        this.requestQueue = requestQueue;
        this.sharedPreferences = sharedPreferences;
    }

    public Task<Uri> persistImageAndCallApi(Uri uri, Boolean isVideoInput) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String uuid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        StorageReference imageRef = storageRef.child("images/" + uuid + ".jpg");
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            //rotate picture
            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            UploadTask uploadTask = imageRef.putBytes(data);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        try {
                            String mood = new EmotionApiService().getMood(downloadUri.toString());
                            if (mood == null || mood.equals("null"))
                                Toast.makeText(context, "Could not recognize face. Please try again!", Toast.LENGTH_SHORT).show();
                            else {
                                mood = mood.substring(1, mood.length()-1);
                                System.out.println(mood);
                                new EmotionControllerService(requestQueue, sharedPreferences).controller(isVideoInput, context, mood, new String[]{"evergreen", "bollywood"});
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, "Failed to get Download URI", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return urlTask;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
