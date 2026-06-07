package com.example.repaso.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

public class FavoriteItem {
    @PropertyName("id") private String id;
    @PropertyName("title") private String title;
    @PropertyName("mediaType") private String mediaType;
    @PropertyName("posterPath") private String posterPath;
    @PropertyName("addedAt") private Timestamp addedAt;

    public FavoriteItem() {}

    public FavoriteItem(String id, String title, String mediaType, String posterPath) {
        this.id = id;
        this.title = title;
        this.mediaType = mediaType;
        this.posterPath = posterPath;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public Timestamp getAddedAt() { return addedAt; }
    public void setAddedAt(Timestamp addedAt) { this.addedAt = addedAt; }

    @Exclude @Override public String toString() {
        return "FavoriteItem{id='" + id + "', title='" + title + "'}";
    }
}
