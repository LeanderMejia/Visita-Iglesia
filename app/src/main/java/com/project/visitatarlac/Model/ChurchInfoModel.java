package com.project.visitatarlac.Model;

import java.util.ArrayList;

public class ChurchInfoModel {

    private String churchName, churchAddress, churchDescription, churchImg;
    private ArrayList<String> churchInfo;

    public ChurchInfoModel(String churchName, String churchAddress, String churchDescription, String churchImg, ArrayList<String> churchInfo) {
        this.churchName = churchName;
        this.churchAddress = churchAddress;
        this.churchDescription = churchDescription;
        this.churchImg = churchImg;
        this.churchInfo = churchInfo;
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

    public String getChurchDescription() {
        return churchDescription;
    }

    public void setChurchDescription(String churchDescription) {
        this.churchDescription = churchDescription;
    }

    public String getChurchImg() {
        return churchImg;
    }

    public void setChurchImg(String churchImg) {
        this.churchImg = churchImg;
    }

    public ArrayList<String> getChurchInfo() {
        return churchInfo;
    }

    public void setChurchInfo(ArrayList<String> churchInfo) {
        this.churchInfo = churchInfo;
    }

    @Override
    public String toString() {
        return "ChurchInfoModel{" +
                "churchName='" + churchName + '\'' +
                ", churchDescription='" + churchDescription + '\'' +
                ", churchImg='" + churchImg + '\'' +
                ", churchInfo=" + churchInfo +
                '}';
    }
}
