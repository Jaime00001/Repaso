package com.example.repaso.model;

import com.google.firebase.Timestamp;

public class UserProfile {
    public String uid;
    public String displayName;
    public String email;
    public Timestamp createdAt;

    public UserProfile() {}

    public UserProfile(String uid, String displayName, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.createdAt = Timestamp.now();
    }
}
