package com.project.visitatarlac.Model;

public class VicariateListModel {

    private String vicariateName, vicariateImg, categoryName;

    public VicariateListModel(String vicariateName, String vicariateImg, String categoryName) {
        this.vicariateName = vicariateName;
        this.vicariateImg = vicariateImg;
        this.categoryName = categoryName;
    }

    public String getVicariateName() {
        return vicariateName;
    }

    public void setVicariateName(String vicariateName) {
        this.vicariateName = vicariateName;
    }

    public String getVicariateImg() {
        return vicariateImg;
    }

    public void setVicariateImg(String vicariateImg) {
        this.vicariateImg = vicariateImg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "VicariateListModel{" +
                "vicariateName='" + vicariateName + '\'' +
                ", vicariateImg='" + vicariateImg + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
