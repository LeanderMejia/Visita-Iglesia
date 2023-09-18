package com.project.visitatarlac.Model;

public class ChurchListRefModel {

    private String churchName, source;

    public ChurchListRefModel(String churchName, String source) {
        this.churchName = churchName;
        this.source = source;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ChurchListRefModel{" +
                "churchName='" + churchName + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
