package com.example.train_shadowlinedemo.Personal;

public class CityCollection {
    private int city_id;
    private String city_name;
    private String city_english;
    private String img;
    private boolean isChecked;

    public CityCollection() {
    }

    @Override
    public String toString() {
        return "CityCollection{" +
                "city_id=" + city_id +
                ", city_name='" + city_name + '\'' +
                ", city_english='" + city_english + '\'' +
                ", img='" + img + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public CityCollection(int city_id, String city_name, String city_english) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.city_english = city_english;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public CityCollection(String city_name, String city_english) {
        this.city_name = city_name;
        this.city_english = city_english;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_english() {
        return city_english;
    }

    public void setCity_english(String city_english) {
        this.city_english = city_english;
    }
}
