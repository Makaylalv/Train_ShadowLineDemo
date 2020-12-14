package com.example.train_shadowlinedemo.entity;

public class MyRoute {
    private int routeId;
    private String word;
    private int filmId;
    private String placeIds;
    private int star;
    private String img;
    public int getRouteId() {
        return routeId;
    }
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public int getFilmId() {
        return filmId;
    }
    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }
    public String getPlaceIds() {
        return placeIds;
    }
    public void setPlaceIds(String placeIds) {
        this.placeIds = placeIds;
    }
    public int getStar() {
        return star;
    }
    public void setStar(int star) {
        this.star = star;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "SysRoute [routeId=" + routeId + ", word=" + word + ", filmId=" + filmId + ", placeIds=" + placeIds
                + ", star=" + star + ", img=" + img + "]";
    }
    public MyRoute(int routeId, String word, int filmId, String placeIds, int star, String img) {
        super();
        this.routeId = routeId;
        this.word = word;
        this.filmId = filmId;
        this.placeIds = placeIds;
        this.star = star;
        this.img = img;
    }
    public MyRoute() {
        super();
    }

}
