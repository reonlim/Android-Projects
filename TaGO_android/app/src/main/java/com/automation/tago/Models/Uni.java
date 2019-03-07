package com.automation.tago.Models;

public class Uni {

    private String university_name;
    private String university_picture;

    public Uni(){}

    public Uni(String university_name, String university_picture){
        this.university_name = university_name;
        this.university_picture = university_picture;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    public String getUniversity_picture() {
        return university_picture;
    }

    public void setUniversity_picture(String university_picture) {
        this.university_picture = university_picture;
    }

    @Override
    public String toString() {
        return "Uni{" +
                "university_name='" + university_name + '\'' +
                ", university_picture='" + university_picture + '\'' +
                '}';
    }
}
