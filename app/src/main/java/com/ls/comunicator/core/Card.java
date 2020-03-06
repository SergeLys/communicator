package com.ls.comunicator.core;

import java.io.Serializable;
import java.util.HashMap;

public class Card implements Serializable {

    private String name;
    private transient String page;
    private HashMap<CaseEnum, Case> cases;
    private Image image;

    public Card() {}

    public Card(String name, HashMap<CaseEnum, Case> cases, Image image) {
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

    public HashMap<CaseEnum, Case> getCases() {
        return cases;
    }

    public void setCases(HashMap<CaseEnum, Case> cases) {
        this.cases = cases;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
