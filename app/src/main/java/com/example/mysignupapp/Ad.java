package com.example.mysignupapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Ad implements Serializable
{
    String title;
    String category;
    String price;
    String description;
    ArrayList<String> categories_for_switching = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    Ad(){}

    public Ad(String title, String category, String price, ArrayList<String> categories_for_switching, ArrayList<String> images, String description) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.categories_for_switching = categories_for_switching;
        this.images = images;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<String> getCategories_for_switching() {
        return categories_for_switching;
    }

    public void setCategories_for_switching(ArrayList<String> categories_for_switching) {
        this.categories_for_switching = categories_for_switching;
    }

    public ArrayList<String> getImages() {
        return images;
    }
    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
