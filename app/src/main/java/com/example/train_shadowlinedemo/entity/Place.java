package com.example.train_shadowlinedemo.entity;

public class Place {
    private int placeId;

    private String placeName;

    private String placeEnglishname;

    private double placeLongitude;

    private double placeLatitude;

    private String placePhone;

    private String placeReallyImg;

    private String placeFalseImg;

    private String placeDescribe;

    private String placeFilmDescribe;

    private String placePosition;

    private String placeTime;

    private String placeType;

    private String placeMapImg;

    public String getPlaceMapImg() {

        return placeMapImg;

    }

    public void setPlaceMapImg(String placeMapImg) {

        this.placeMapImg = placeMapImg;

    }

    public int getPlaceId() {

        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceEnglishname() {
        return placeEnglishname;
    }

    public void setPlaceEnglishname(String placeEnglishname) {
        this.placeEnglishname = placeEnglishname;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId=" + placeId +
                ", placeName='" + placeName + '\'' +
                ", placeEnglishname='" + placeEnglishname + '\'' +
                ", placeLongitude=" + placeLongitude +
                ", placeLatitude=" + placeLatitude +
                ", placePhone='" + placePhone + '\'' +
                ", placeReallyImg='" + placeReallyImg + '\'' +
                ", placeFalseImg='" + placeFalseImg + '\'' +
                ", placeDescribe='" + placeDescribe + '\'' +
                ", placeFilmDescribe='" + placeFilmDescribe + '\'' +
                ", placePosition='" + placePosition + '\'' +
                ", placeTime='" + placeTime + '\'' +
                ", placeType='" + placeType + '\'' +
                ", placeMapImg='" + placeMapImg + '\'' +
                '}';
    }

    public double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public String getPlacePhone() {
        return placePhone;
    }

    public void setPlacePhone(String placePhone) {
        this.placePhone = placePhone;
    }

    public String getPlaceReallyImg() {
        return placeReallyImg;
    }

    public void setPlaceReallyImg(String placeReallyImg) {
        this.placeReallyImg = placeReallyImg;
    }

    public String getPlaceFalseImg() {
        return placeFalseImg;
    }

    public void setPlaceFalseImg(String placeFalseImg) {
        this.placeFalseImg = placeFalseImg;
    }

    public String getPlaceDescribe() {
        return placeDescribe;
    }

    public void setPlaceDescribe(String placeDescribe) {
        this.placeDescribe = placeDescribe;
    }

    public String getPlaceFilmDescribe() {
        return placeFilmDescribe;
    }

    public void setPlaceFilmDescribe(String placeFilmDescribe) {
        this.placeFilmDescribe = placeFilmDescribe;
    }

    public String getPlacePosition() {
        return placePosition;
    }

    public void setPlacePosition(String placePosition) {
        this.placePosition = placePosition;
    }

    public String getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(String placeTime) {
        this.placeTime = placeTime;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

}
