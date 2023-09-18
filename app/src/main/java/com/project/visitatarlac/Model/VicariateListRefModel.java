package com.project.visitatarlac.Model;

public class VicariateListRefModel {

    private String vicariateName, categoryNameReference;

    public VicariateListRefModel(String vicariateName, String categoryNameReference) {
        this.vicariateName = vicariateName;
        this.categoryNameReference = categoryNameReference;
    }

    public String getVicariateName() {
        return vicariateName;
    }

    public void setVicariateName(String vicariateName) {
        this.vicariateName = vicariateName;
    }

    public String getCategoryNameReference() {
        return categoryNameReference;
    }

    public void setCategoryNameReference(String categoryNameReference) {
        this.categoryNameReference = categoryNameReference;
    }

    @Override
    public String toString() {
        return "VicariateListRefModel{" +
                "vicariateName='" + vicariateName + '\'' +
                ", categoryNameReference='" + categoryNameReference + '\'' +
                '}';
    }
}
