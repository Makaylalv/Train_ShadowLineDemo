package com.example.train_shadowlinedemo.entity;

public class User {
    private String user_id;
    private String user_sex;
    private String user_phone;
    private String user_name;
    private String user_password;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public User(String user_id, String user_sex, String user_phone, String user_name, String user_password) {
        this.user_id = user_id;
        this.user_sex = user_sex;
        this.user_phone = user_phone;
        this.user_name = user_name;
        this.user_password = user_password;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", user_sex='" + user_sex + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                '}';
    }
}

