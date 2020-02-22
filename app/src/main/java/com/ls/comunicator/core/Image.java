package com.ls.comunicator.core;

import java.io.Serializable;

public class Image implements Serializable {

    private ProxyBitMap image;
    private float textSize;
    private int textColour, borderSize, borderColour;
    private String textPlace;

    public Image() {}

    public Image(ProxyBitMap image, float textSize, int textColour,  String textPlace, int borderSize, int borderColour) {
        this.image = image;
        this.textSize = textSize;
        this.textColour = textColour;
        this.borderSize = borderSize;
        this.borderColour = borderColour;
        this.textPlace = textPlace;
    }

    public ProxyBitMap getImage() {
        return image;
    }

    public void setImage(ProxyBitMap image) {
        this.image = image;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColour() {
        return textColour;
    }

    public void setTextColour(int textColour) {
        this.textColour = textColour;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public int getBorderColour() {
        return borderColour;
    }

    public void setBorderColour(int borderColour) {
        this.borderColour = borderColour;
    }

    public String getTextPlace() {
        return textPlace;
    }

    public void setTextPlace(String textPlace) {
        this.textPlace = textPlace;
    }
}
