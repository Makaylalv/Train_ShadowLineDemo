package com.example.train_shadowlinedemo.entity;

import com.baidu.mapapi.search.route.PlanNode;

public class Distance {
    private double distance;
    private PlanNode i;
    private PlanNode j;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public PlanNode getI() {
        return i;
    }

    public void setI(PlanNode i) {
        this.i = i;
    }

    public PlanNode getJ() {
        return j;
    }

    public void setJ(PlanNode j) {
        this.j = j;
    }


}
