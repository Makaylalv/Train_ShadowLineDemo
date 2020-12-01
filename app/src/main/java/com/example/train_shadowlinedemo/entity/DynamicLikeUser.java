package com.example.train_shadowlinedemo.entity;

public class DynamicLikeUser {
    private int dynamicId;
    private int dynamicUserId;
    private String dynamicUserName;

    public DynamicLikeUser(int dynamicId, int dynamicUserId, String dynamicUserName) {
        this.dynamicId = dynamicId;
        this.dynamicUserId = dynamicUserId;
        this.dynamicUserName = dynamicUserName;
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public int getDynamicUserId() {
        return dynamicUserId;
    }

    public void setDynamicUserId(int dynamicUserId) {
        this.dynamicUserId = dynamicUserId;
    }

    public String getDynamicUserName() {
        return dynamicUserName;
    }

    public void setDynamicUserName(String dynamicUserName) {
        this.dynamicUserName = dynamicUserName;
    }

    @Override
    public String toString() {
        return "DynamicLikeUser{" +
                "dynamicId=" + dynamicId +
                ", dynamicUserId=" + dynamicUserId +
                ", dynamicUserName='" + dynamicUserName + '\'' +
                '}';
    }
}
