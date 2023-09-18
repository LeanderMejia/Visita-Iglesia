package com.project.visitatarlac.Model;

public class PrayersModel {

    private String prayerName;
    private String prayerSubtitle;
    private String prayerDescription;
    private String prayerImg;

    public PrayersModel(String prayerName, String prayerSubtitle, String prayerDescription, String prayerImg) {
        this.prayerName = prayerName;
        this.prayerSubtitle = prayerSubtitle;
        this.prayerDescription = prayerDescription;
        this.prayerImg = prayerImg;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public void setPrayerName(String prayerName) {
        this.prayerName = prayerName;
    }

    public String getPrayerSubtitle() {
        return prayerSubtitle;
    }

    public void setPrayerSubtitle(String prayerSubtitle) {
        this.prayerSubtitle = prayerSubtitle;
    }

    public String getPrayerDescription() {
        return prayerDescription;
    }

    public void setPrayerDescription(String prayerDescription) {
        this.prayerDescription = prayerDescription;
    }

    public String getPrayerImg() {
        return prayerImg;
    }

    public void setPrayerImg(String prayerImg) {
        this.prayerImg = prayerImg;
    }

    @Override
    public String toString() {
        return "PrayersModel{" +
                "prayerName='" + prayerName + '\'' +
                ", prayerSubtitle='" + prayerSubtitle + '\'' +
                ", prayerDescription='" + prayerDescription + '\'' +
                ", prayerImg='" + prayerImg + '\'' +
                '}';
    }
}
