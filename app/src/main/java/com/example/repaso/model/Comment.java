package com.example.repaso.model;

import com.google.firebase.Timestamp;

public class Comment {
    public String id;
    public String authorUid;
    public String authorName;
    public String text;
    public Timestamp createdAt;

    public Comment() {}

    public Comment(String id, String authorUid, String authorName, String text) {
        this.id = id;
        this.authorUid = authorUid;
        this.authorName = authorName;
        this.text = text;
        this.createdAt = Timestamp.now();
    }
}
