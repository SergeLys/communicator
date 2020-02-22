package com.ls.comunicator.core;

import android.graphics.Bitmap;
import java.io.Serializable;

public class ProxyBitMap implements Serializable {

    private final int [] pixels;
    private final int width , height;

    public ProxyBitMap(Bitmap bitmap){
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int [width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
    }

    public Bitmap getBitmap(){
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.RGB_565);
    }
}
