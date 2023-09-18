package com.project.visitatarlac.Model;

public class ChurchMapModel {

    private String churchName, churchAddress, churchImage, dateAndTime;
    private float km;
    private double latitude, longitude;

    public ChurchMapModel(String churchName, String churchAddress, String churchImage, float km, double latitude, double longitude) {
        this.churchName = churchName;
        this.churchAddress = churchAddress;
        this.churchImage = churchImage;
        this.km = km;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ChurchMapModel(String churchName, String churchAddress, String churchImage, String dateAndTime, float km, double latitude, double longitude) {
        this.churchName = churchName;
        this.churchAddress = churchAddress;
        this.churchImage = churchImage;
        this.dateAndTime = dateAndTime;
        this.km = km;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }

    public String getChurchAddress() {
        return churchAddress;
    }

    public void setChurchAddress(String churchAddress) {
        this.churchAddress = churchAddress;
    }

    public String getChurchImage() {
        return churchImage;
    }

    public void setChurchImage(String churchImage) {
        this.churchImage = churchImage;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public String toString() {
        return "ChurchMapModel{" +
                "churchName='" + churchName + '\'' +
                ", churchAddress='" + churchAddress + '\'' +
                ", churchImage='" + churchImage + '\'' +
                ", km=" + km +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }


}
