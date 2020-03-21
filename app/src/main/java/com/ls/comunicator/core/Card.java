package com.ls.comunicator.core;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;

import kotlin.UByteArray;

public class Card implements Serializable {

    private String name;
    private Image image;
    private transient String page;
    private transient boolean isCases;
    private EnumMap<CaseEnum, String> cases;

    public Card() {}

    public Card(String name,  EnumMap<CaseEnum, String> cases, Image image) {
        this.name = name;
        this.cases = cases;
        this.image = image;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  EnumMap<CaseEnum, String> getCases() {
        return cases;
    }

    public void setCases( EnumMap<CaseEnum, String> cases) {
        this.cases = cases;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isCases() {
        return isCases;
    }

    public void setCases(boolean cases) {
        isCases = cases;
    }

}
