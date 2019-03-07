package com.automation.tago.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {

    private String food_name;
    private String food_description;
    private double food_price;
    private String food_image;

    public FoodItem(){}

    public FoodItem(String food_name, String food_description, double food_price, String food_image) {
        this.food_name = food_name;
        this.food_description = food_description;
        this.food_price = food_price;
        this.food_image = food_image;
    }

    protected FoodItem(Parcel in) {
        food_name = in.readString();
        food_description = in.readString();
        food_price = in.readDouble();
        food_image = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public double getFood_price() {
        return food_price;
    }

    public void setFood_price(double food_price) {
        this.food_price = food_price;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "food_name='" + food_name + '\'' +
                ", food_description='" + food_description + '\'' +
                ", food_price=" + food_price +
                ", food_image='" + food_image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(food_name);
        dest.writeString(food_description);
        dest.writeDouble(food_price);
        dest.writeString(food_image);
    }
}
