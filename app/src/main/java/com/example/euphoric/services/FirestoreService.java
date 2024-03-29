package com.example.euphoric.services;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class FirestoreService {
    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void add(Object data, String collectionPath) {
        db.collection(collectionPath)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static void set(Object data, String collectionPath, String documentName) {
        db.collection(collectionPath).document(documentName)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static Map<String, Object> get(String collectionPath, String documentName) {
        Task<DocumentSnapshot> ds = db.collection(collectionPath).document(documentName)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                document.getData();
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        while (!ds.isComplete()) ;
        return ds.getResult().getData();
    }

    public static boolean update(Map<String, Object> data, String collectionPath, String documentName, Context context) {
        final boolean[] profileUpdated = {false};
        Task<Void> updateTask = db.collection(collectionPath).document(documentName)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        profileUpdated[0] = true;
                        System.out.println("updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to update profile!", Toast.LENGTH_SHORT).show();
                        profileUpdated[0] = false;
                    }
                });

        while(!updateTask.isComplete());
        return profileUpdated[0];
    }

    public static void addArrayElement(String collectionPath, String documentName, String arrayName, String element) {
        Map<String, Object> ob = get(collectionPath, documentName);
        if (!((List<String>) ob.get(arrayName)).contains(element)) {
            db.collection(collectionPath).document(documentName)
                    .update(arrayName, FieldValue.arrayUnion(element));
        }
    }

    public static void deleteArrayElement(String collectionPath, String documentName, String arrayName, String element) {
        db.collection(collectionPath).document(documentName)
                .update(arrayName, FieldValue.arrayRemove(element));
    }

    public static void delete(String collectionPath, String documentName) {
        db.collection(collectionPath).document(documentName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}

