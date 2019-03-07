package com.automation.tago.Models;

public class CartItem {

    private String shop_name;
    private String food_image;
    private String food_name;
    private int food_quantity;
    private double food_price;

    public CartItem(){}

    public CartItem(String shop_name, String food_image, String food_name, int food_quantity, double food_price) {
        this.shop_name = shop_name;
        this.food_image = food_image;
        this.food_name = food_name;
        this.food_quantity = food_quantity;
        this.food_price = food_price;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(int food_quantity) {
        this.food_quantity = food_quantity;
    }

    public double getFood_price() {
        return food_price;
    }

    public void setFood_price(double food_price) {
        this.food_price = food_price;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "shop_name='" + shop_name + '\'' +
                ", food_image='" + food_image + '\'' +
                ", food_name='" + food_name + '\'' +
                ", food_quantity=" + food_quantity +
                ", food_price=" + food_price +
                '}';
    }
}
