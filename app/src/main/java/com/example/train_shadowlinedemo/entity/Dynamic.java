package com.example.train_shadowlinedemo.entity;

import java.util.List;

public class Dynamic {
    private int dynamicId;//动态id
    private int userId;//用户id
    private String userName;//用户名
    private String userImg;//用户头像
    private String dynamicTime;//发布动态时间
    private String dynamicContent;//发布动态内容
    private String dynamicPlace;//发布动态地点
    private List<String> dynamicImgs;//发布动态的图片
    private List<String> likeUsers;//动态点赞的人
    private List<Comment> comments;//动态图的评论

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getDynamicTime() {
        return dynamicTime;
    }

    public void setDynamicTime(String dynamicTime) {
        this.dynamicTime = dynamicTime;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public String getDynamicPlace() {
        return dynamicPlace;
    }

    public void setDynamicPlace(String dynamicPlace) {
        this.dynamicPlace = dynamicPlace;
    }

    public List<String> getDynamicImgs() {
        return dynamicImgs;
    }

    public void setDynamicImgs(List<String> dynamicImgs) {
        this.dynamicImgs = dynamicImgs;
    }

    public List<String> getLikeUser() {
        return likeUsers;
    }

    public void setLikeUser(List<String> likeUser) {
        this.likeUsers = likeUser;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Dynamic{" +
                "dynamicId=" + dynamicId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userImg='" + userImg + '\'' +
                ", dynamicTime='" + dynamicTime + '\'' +
                ", dynamicContent='" + dynamicContent + '\'' +
                ", dynamicPlace='" + dynamicPlace + '\'' +
                ", dynamicImgs=" + dynamicImgs +
                ", likeUser=" + likeUsers +
                ", comments=" + comments +
                '}';
    }
}
