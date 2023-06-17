package com.example.firebasedb;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Events {
    public String event_name;
    public String event_description;
    public String event_age;
    public double event_ax;
    public double event_ay;
    public String event_image;

    public Events() {
    }

    public Events(String event_name, String event_age, String event_description, double event_ax, double event_ay, String event_image) {
        this.event_name = event_name;
        this.event_age = event_age;
        this.event_description = event_description;
        this.event_ax = event_ax;
        this.event_ay = event_ay;
        this.event_image = event_image;
    }

    public void setEvent_title(String title) {
        this.event_name = title;
    }

    public void setEvent_description(String description) {
        this.event_description = description;
    }

    public void setEvent_age(String rating) {
        this.event_age = rating;
    }

    public void setEvent_ax(double x) {
        this.event_ax = x;
    }

    public void setEvent_ay(double y) {
        this.event_ay = y;
    }

    public void setEvent_image(String photo) {
        this.event_image = photo;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_age() {
        return event_age;
    }

    public double getEvent_ax() {
        return event_ax;
    }

    public double getEvent_ay() {
        return event_ay;
    }

    public String getEvent_image() {
        return event_image;
    }
}