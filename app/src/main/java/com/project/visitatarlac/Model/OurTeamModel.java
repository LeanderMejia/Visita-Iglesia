package com.project.visitatarlac.Model;

public class OurTeamModel {

    private String name;
    private String position;
    private String img;

    public OurTeamModel(String name, String position, String img) {
        this.name = name;
        this.position = position;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "OurTeamModel{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
