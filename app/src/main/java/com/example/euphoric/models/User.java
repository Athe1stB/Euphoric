package com.example.euphoric.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import java.util.List;
import java.util.Objects;

public class User {
    private int age;
    private String name;
    private List<Integer> songIds;
    private String userId;
    private String email;

    public User(int age, String name, List<Integer> songIds) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.age = age;
        this.name = name;
        this.songIds = songIds;
        this.userId = Objects.requireNonNull(firebaseUser).getUid();
        this.email = firebaseUser.getEmail();
        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Integer> songIds) {
        this.songIds = songIds;
    }
}
