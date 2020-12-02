package com.example.train_shadowlinedemo.entity;

public class FilmCollection {
    private int film_id;
    private String film_name;
    private String film_english;
    private String film_type;
    private String film_city;
    private String film_place;//片场
    private boolean isChecked;
    public FilmCollection() {
    }

    public FilmCollection(int film_id, String film_name, String film_english, String film_type, String film_city, String film_place) {
        this.film_id = film_id;
        this.film_name = film_name;
        this.film_english = film_english;
        this.film_type = film_type;
        this.film_city = film_city;
        this.film_place = film_place;
    }

    public FilmCollection(String film_name, String film_english, String type, String city, String place) {
        this.film_name = film_name;
        this.film_english = film_english;
        this.film_type = type;
        this.film_city = city;
        this.film_place = place;
    }

    @Override
    public String toString() {
        return "FilmCollection{" +
                "film_id=" + film_id +
                ", film_name='" + film_name + '\'' +
                ", film_english='" + film_english + '\'' +
                ", film_type='" + film_type + '\'' +
                ", film_city='" + film_city + '\'' +
                ", film_place='" + film_place + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }

    public String getFilm_name() {
        return film_name;
    }

    public void setFilm_name(String film_name) {
        this.film_name = film_name;
    }

    public String getFilm_english() {
        return film_english;
    }

    public void setFilm_english(String film_english) {
        this.film_english = film_english;
    }

    public String getFilm_type() {
        return film_type;
    }

    public void setFilm_type(String film_type) {
        this.film_type = film_type;
    }

    public String getFilm_city() {
        return film_city;
    }

    public void setFilm_city(String film_city) {
        this.film_city = film_city;
    }

    public String getFilm_place() {
        return film_place;
    }

    public void setFilm_place(String film_place) {
        this.film_place = film_place;
    }
}
