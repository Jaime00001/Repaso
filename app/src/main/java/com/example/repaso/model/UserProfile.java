package com.example.repaso.model;

import com.google.firebase.Timestamp;

public class UserProfile {
    public String uid;
    public String displayName;
    public String email;
    public Timestamp createdAt;
    public com.google.firebase.Timestamp dateOfBirth;
    public String mode;

    public UserProfile() {}

    public UserProfile(String uid, String displayName, String email, com.google.firebase.Timestamp dateOfBirth, String mode) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.createdAt = Timestamp.now();
        this.dateOfBirth = dateOfBirth;
        this.mode = mode;
    }
}
