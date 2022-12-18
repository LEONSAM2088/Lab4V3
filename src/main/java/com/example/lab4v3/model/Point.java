package com.example.lab4v3.model;

import javax.persistence.*;

@Entity
@Table(name = "t_point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "x")
    private float X;
    @Column(name = "y")
    private float Y;
    @Column(name = "r")
    private float R;
    @Column(name = "in_area")
    private boolean IsInArea;


    public boolean isInArea() {
        return IsInArea;
    }

    public void setInArea(boolean inArea) {
        IsInArea = inArea;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getR() {
        return R;
    }

    public void setR(float r) {
        R = r;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
