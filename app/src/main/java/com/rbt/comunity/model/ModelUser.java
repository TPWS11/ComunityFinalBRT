package com.rbt.comunity.model;

public class ModelUser {

    private String bio, email, username, imageurl, id;

    public ModelUser() {}

    public ModelUser(String bio, String email, String username, String imageurl, String id) {
        this.bio = bio;
        this.email = email;
        this.username = username;
        this.imageurl = imageurl;
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
