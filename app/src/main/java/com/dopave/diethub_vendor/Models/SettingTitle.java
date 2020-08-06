package com.dopave.diethub_vendor.Models;

public class SettingTitle {
    private String Name;
    private int Image,Type;

    public SettingTitle(String name, int image, int type) {
        Name = name;
        Image = image;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
