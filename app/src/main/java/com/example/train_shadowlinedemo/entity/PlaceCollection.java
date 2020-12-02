package com.example.train_shadowlinedemo.entity;

public class PlaceCollection {
    private int place_id;
    private String place_name;
    private String place_english;
    private String place_position;
    private String film;
    private boolean isChecked;
    public PlaceCollection() {
    }

    @Override
    public String toString() {
        return "PlaceCollection{" +
                "place_id=" + place_id +
                ", place_name='" + place_name + '\'' +
                ", place_english='" + place_english + '\'' +
                ", place_position='" + place_position + '\'' +
                ", film='" + film + '\'' +
                '}';
    }

    public PlaceCollection(int place_id, String place_name, String place_english, String place_position, String film) {
        this.place_id = place_id;
        this.place_name = place_name;
        this.place_english = place_english;
        this.place_position = place_position;
        this.film = film;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_english() {
        return place_english;
    }

    public void setPlace_english(String place_english) {
        this.place_english = place_english;
    }

    public String getPlace_position() {
        return place_position;
    }

    public void setPlace_position(String place_position) {
        this.place_position = place_position;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }
}
