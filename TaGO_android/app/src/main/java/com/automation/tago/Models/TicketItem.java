package com.automation.tago.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TicketItem implements Parcelable, Serializable {

    private String food_name;
    private int food_quantity;

    public TicketItem(){}

    public TicketItem(String food_name, int food_quantity){
        this.food_name = food_name;
        this.food_quantity = food_quantity;
    }

    protected TicketItem(Parcel in) {
        food_name = in.readString();
        food_quantity = in.readInt();
    }

    public static final Creator<TicketItem> CREATOR = new Creator<TicketItem>() {
        @Override
        public TicketItem createFromParcel(Parcel in) {
            return new TicketItem(in);
        }

        @Override
        public TicketItem[] newArray(int size) {
            return new TicketItem[size];
        }
    };

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

    @Override
    public String toString() {
        return "TicketItem{" +
                "food_name='" + food_name + '\'' +
                ", food_quantity=" + food_quantity +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(food_name);
        dest.writeInt(food_quantity);
    }
}
