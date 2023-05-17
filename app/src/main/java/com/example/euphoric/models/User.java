package com.example.euphoric.models;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class User {
    private String name;
    private List<String> songIds;
    private String userId;
    private String email;
    private String dob;
    private String phone;

    public User(String dob, String phone, String name, List<String> songIds) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.dob = dob;
        this.name = name;
        this.songIds = songIds;
        this.phone = phone;
        this.userId = Objects.requireNonNull(firebaseUser).getUid();
        this.email = firebaseUser.getEmail();
        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());
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

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
