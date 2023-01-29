package com.itsolution.car_owner;

public class model_for_owner_dash {
    String location;
    String car_model;
    String price;
    String img_1;


    public model_for_owner_dash() {
    }

    public model_for_owner_dash(String location, String car_model, String price, String img_1) {
        this.location = location;
        this.car_model = car_model;
        this.price = price;
        this.img_1 = img_1;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1;
    }
}
