package com.example.train_shadowlinedemo.entity;

import android.graphics.Bitmap;

public class City {
    private String cityId;
    private Bitmap cityPic;
    private String cityTextChinese;
    private String cityTextEnglish;

    private String cityCountry;
    private String cityOcean;
    private String cityImg;
    private String cityInfo;

    public String getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(String cityInfo) {
        this.cityInfo = cityInfo;
    }

    public City(String cityId, Bitmap cityPic, String cityTextChinese, String cityTextEnglish) {
        this.cityId = cityId;
        this.cityPic = cityPic;
        this.cityTextChinese = cityTextChinese;
        this.cityTextEnglish = cityTextEnglish;
    }

    public City(String cityId, Bitmap cityPic, String cityTextChinese, String cityTextEnglish, String cityCountry, String cityOcean, String cityImg, String cityInfo) {
        this.cityId = cityId;
        this.cityPic = cityPic;
        this.cityTextChinese = cityTextChinese;
        this.cityTextEnglish = cityTextEnglish;
        this.cityCountry = cityCountry;
        this.cityOcean = cityOcean;
        this.cityImg = cityImg;
        this.cityInfo = cityInfo;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityId='" + cityId + '\'' +
                ", cityPic=" + cityPic +
                ", cityTextChinese='" + cityTextChinese + '\'' +
                ", cityTextEnglish='" + cityTextEnglish + '\'' +
                ", cityCountry='" + cityCountry + '\'' +
                ", cityOcean='" + cityOcean + '\'' +
                ", cityImg='" + cityImg + '\'' +
                '}';
    }

    public City(String cityId, Bitmap cityPic, String cityTextChinese, String cityTextEnglish, String cityCountry, String cityOcean, String cityImg) {
        this.cityId = cityId;
        this.cityPic = cityPic;
        this.cityTextChinese = cityTextChinese;
        this.cityTextEnglish = cityTextEnglish;
        this.cityCountry = cityCountry;
        this.cityOcean = cityOcean;
        this.cityImg = cityImg;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityTextChinese() {
        return cityTextChinese;
    }

    public void setCityTextChinese(String cityTextChinese) {
        this.cityTextChinese = cityTextChinese;
    }

    public String getCityTextEnglish() {
        return cityTextEnglish;
    }

    public void setCityTextEnglish(String cityTextEnglish) {
        this.cityTextEnglish = cityTextEnglish;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }

    public String getCityOcean() {
        return cityOcean;
    }

    public void setCityOcean(String cityOcean) {
        this.cityOcean = cityOcean;
    }

    public String getCityImg() {
        return cityImg;
    }

    public void setCityImg(String cityImg) {
        this.cityImg = cityImg;
    }

    public Bitmap getCityPic() {
        return cityPic;
    }

    public void setCityPic(Bitmap cityPic) {
        this.cityPic = cityPic;
    }
}
