package com.example.train_shadowlinedemo.Personal;

import java.util.Date;

public class RouteCollection {
    private int id;
    private String name;
    private String date;
    private int tag;
    private boolean isChecked;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public RouteCollection() {
    }

    @Override
    public String toString() {
        return "RouteCollection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    public RouteCollection(int id, String name, int tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
