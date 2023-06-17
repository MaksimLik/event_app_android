package com.example.firebasedb;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Reviews {
    public String beer_name;
    public String beer_description;
    public String beer_rating;
    public double beer_coordinates_x;
    public double beer_coordinates_y;
    public String beer_photo;

    public Reviews() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Reviews(String beer_name, String beer_rating, String beer_description, double beer_coordinates_x, double beer_coordinates_y, String beer_photo) {
        this.beer_name = beer_name;
        this.beer_rating = beer_rating;
        this.beer_description = beer_description;
        this.beer_coordinates_x = beer_coordinates_x;
        this.beer_coordinates_y = beer_coordinates_y;
        this.beer_photo = beer_photo;
    }

    public void setReview_title(String title) {
        this.beer_name = title;
    }

    public void setReview_description(String description) {
        this.beer_description = description;
    }

    public void setReview_rating(String rating) {
        this.beer_rating = rating;
    }

    public void setReview_lat(double x) {
        this.beer_coordinates_x = x;
    }

    public void setReview_lng(double y) {
        this.beer_coordinates_y = y;
    }

    public void setReview_image(String photo) {
        this.beer_photo = photo;
    }

    public String getBeer_name() {
        return beer_name;
    }

    public String getBeer_description() {
        return beer_description;
    }

    public String getBeer_rating() {
        return beer_rating;
    }

    public double getBeer_coordinates_x() {
        return beer_coordinates_x;
    }

    public double getBeer_coordinates_y() {
        return beer_coordinates_y;
    }

    public String getBeer_photo() {
        return beer_photo;
    }
}