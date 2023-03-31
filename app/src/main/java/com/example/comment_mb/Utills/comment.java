package com.example.comment_mb.Utills;

public class comment {
    private String comment, userEmail;

    public comment(String comment, String userEmail) {
        this.comment = comment;
        this.userEmail = userEmail;
    }

    public comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
