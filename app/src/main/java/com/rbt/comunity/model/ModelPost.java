package com.rbt.comunity.model;

public class ModelPost {
    String postid, description, imageurl, publisher;

    public ModelPost() {}

    public ModelPost(String postid, String description, String imageurl, String publisher) {
        this.postid = postid;
        this.description = description;
        this.imageurl = imageurl;
        this.publisher = publisher;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
