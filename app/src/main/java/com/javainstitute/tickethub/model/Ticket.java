package com.javainstitute.tickethub.model;

public class Ticket {
    private String image;
    private String id;
    private String title;
    private String time;
    private String date;
    private String place;
    private String location;
    private String price;
    private int qty;
    private String email;

    public Ticket(){

    }

    public Ticket(String image, String id, String title, String time, String date, String place, String location, String price, int qty,String email) {
        this.image = image;
        this.id = id;
        this.title = title;
        this.time = time;
        this.date = date;
        this.place = place;
        this.location = location;
        this.price = price;
        this.qty = qty;
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
