package com.example.train_shadowlinedemo.entity;

public class Comment {
    private int commentId;//评论id
    private int dynamicId;//动态id
    private int userId;//用户id
    private String commentContent;//评论内容

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

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", dynamicId=" + dynamicId +
                ", userId=" + userId +
                ", commentContent='" + commentContent + '\'' +
                '}';
    }
}
