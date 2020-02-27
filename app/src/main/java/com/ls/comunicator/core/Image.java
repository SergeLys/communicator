package com.ls.comunicator.core;

import android.widget.ImageView;

import java.io.Serializable;

public class Image implements Serializable {

    private String imagePath;
    private transient ImageView imageView;
    private float textSize;
    private int textColour, borderSize, borderColour;
    private  TextPositionEnum textPlace;

    public Image() {}

    public Image(String imagePath, float textSize, int textColour, int borderSize, int borderColour, TextPositionEnum textPlace) {
        this.imagePath = imagePath;
        this.textSize = textSize;
        this.textColour = textColour;
        this.borderSize = borderSize;
        this.borderColour = borderColour;
        this.textPlace = textPlace;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
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

    public  TextPositionEnum getTextPlace() {
        return textPlace;
    }

    public void setTextPlace( TextPositionEnum textPlace) {
        this.textPlace = textPlace;
    }
}
