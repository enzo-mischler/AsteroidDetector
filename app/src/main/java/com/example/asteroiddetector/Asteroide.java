package com.example.asteroiddetector;

public class Asteroide {

    private final String id;
    private final String nom;
    private final double magnitude;
    private final double distance;


    public Asteroide(String id, String nom, double magnitude, double distance) {
        this.id = id;
        this.nom = nom;
        this.magnitude = magnitude;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getDistance() {
        return distance;
    }
}
