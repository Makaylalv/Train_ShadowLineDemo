package com.example.train_shadowlinedemo.entity;

public class RouteSpot {
    private int routeId;
    private int placeId;
    private int userId;

    public RouteSpot() {
    }

    @Override
    public String toString() {
        return "RouteSpot{" +
                "routeId=" + routeId +
                ", placeId=" + placeId +
                ", userId=" + userId +
                '}';
    }

    public RouteSpot(int routeId, int placeId, int userId) {
        this.routeId = routeId;
        this.placeId = placeId;
        this.userId = userId;
    }
    public RouteSpot( int placeId, int userId) {
        this.placeId = placeId;
        this.userId = userId;
    }
    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
