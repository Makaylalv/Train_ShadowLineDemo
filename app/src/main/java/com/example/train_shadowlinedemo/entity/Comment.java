package com.example.train_shadowlinedemo.entity;

public class Comment {
    private int commentId;//评论id
    private int dynamicId;//动态id
    private int userId;//用户id
    private String username;//用户姓名
    private String commentContent;//评论内容
    private String commentTime;//评论时间

    public Comment( int dynamicId, int userId, String username, String commentContent, String commentTime) {

        this.dynamicId = dynamicId;
        this.userId = userId;
        this.username = username;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", dynamicId=" + dynamicId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", commentTime='" + commentTime + '\'' +
                '}';
    }
}
