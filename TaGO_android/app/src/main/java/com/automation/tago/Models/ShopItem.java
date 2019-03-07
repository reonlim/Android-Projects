package com.automation.tago.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopItem implements Parcelable {

    private String shop_image;
    private String shop_name;
    private String shop_food_genre;

    public ShopItem(){}

    public ShopItem(String shop_image, String shop_name, String shop_food_genre) {
        this.shop_image = shop_image;
        this.shop_name = shop_name;
        this.shop_food_genre = shop_food_genre;
    }

    protected ShopItem(Parcel in) {
        shop_image = in.readString();
        shop_name = in.readString();
        shop_food_genre = in.readString();
    }

    public static final Creator<ShopItem> CREATOR = new Creator<ShopItem>() {
        @Override
        public ShopItem createFromParcel(Parcel in) {
            return new ShopItem(in);
        }

        @Override
        public ShopItem[] newArray(int size) {
            return new ShopItem[size];
        }
    };

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_food_genre() {
        return shop_food_genre;
    }

    public void setShop_food_genre(String shop_food_genre) {
        this.shop_food_genre = shop_food_genre;
    }

    @Override
    public String toString() {
        return "ShopItem{" +
                "shop_image='" + shop_image + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", shop_food_genre='" + shop_food_genre + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shop_image);
        dest.writeString(shop_name);
        dest.writeString(shop_food_genre);
    }
}
