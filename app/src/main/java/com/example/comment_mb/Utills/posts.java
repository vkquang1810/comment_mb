package com.example.comment_mb.Utills;

public class posts {
    private String date, postDesc, postImageUrl, userEmail,postID;

    public posts() {
    }

    public posts(String date, String postDesc, String postImageUrl, String userEmail, String postID) {
        this.date = date;
        this.postDesc = postDesc;
        this.postImageUrl = postImageUrl;
        this.userEmail = userEmail;
        this.postID = postID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}


