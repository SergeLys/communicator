package com.ls.comunicator.model;

import com.ls.comunicator.core.TextPositionEnum;
import java.io.Serializable;
import static android.graphics.Color.BLACK;
import static com.ls.comunicator.core.Consts.DEFAULT_BORDER_SIZE;
import static com.ls.comunicator.core.Consts.DEFAULT_TEXT_SIZE;

public class Card implements Serializable {

    private String name;
    private Image image;
    private boolean isHasCases;
    private Cases cases;
    private transient String page;

    public Card() {}

    public Card(String page, String name) {
        this.page = page;
        this.name = name;
        this.image = new Image(DEFAULT_TEXT_SIZE, BLACK,
                DEFAULT_BORDER_SIZE, BLACK, TextPositionEnum.BOTTOM);
        this.isHasCases = false;
    }

    public void addCases() {
        this.cases = new Cases();
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Cases getCases() {
        return cases;
    }

    public void setCases(Cases cases) {
        this.cases = cases;
    }

    public boolean isHasCases() {
        return isHasCases;
    }

    public void setHasCases(boolean hasCases) {
        isHasCases = hasCases;
    }
}
