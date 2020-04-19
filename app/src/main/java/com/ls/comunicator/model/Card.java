package com.ls.comunicator.model;

import android.content.Context;
import android.widget.ImageView;

import com.ls.comunicator.R;
import com.ls.comunicator.core.TextPositionEnum;

import java.io.Serializable;

import static android.graphics.Color.BLACK;

public class Card implements Serializable {

    private String name;
    private Image image;
    private boolean isHasCases;
    private Cases cases;
    private transient String page;

    public Card() {}

    public Card(String name, Context context) {
        this.name = name;
        this.image = new Image(20, BLACK, 10, BLACK, TextPositionEnum.BOTTOM);
        this.image.setImageView(new ImageView(context));
        this.image.getImageView().setImageResource(R.drawable.ic_image_black_24dp);
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

    public class Cases implements Serializable {

        String nominative, genitive, dative,
                accusative, instrumental, prepositional;

        Cases() {
            this.nominative = "";
            this.genitive = "";
            this.dative = "";
            this.accusative = "";
            this.instrumental = "";
            this.prepositional = "";
        }

        public String getNominative() {
            return nominative;
        }

        public void setNominative(String nominative) {
            this.nominative = nominative;
        }

        public String getGenitive() {
            return genitive;
        }

        public void setGenitive(String genitive) {
            this.genitive = genitive;
        }

        public String getDative() {
            return dative;
        }

        public void setDative(String dative) {
            this.dative = dative;
        }

        public String getAccusative() {
            return accusative;
        }

        public void setAccusative(String accusative) {
            this.accusative = accusative;
        }

        public String getInstrumental() {
            return instrumental;
        }

        public void setInstrumental(String instrumental) {
            this.instrumental = instrumental;
        }

        public String getPrepositional() {
            return prepositional;
        }

        public void setPrepositional(String prepositional) {
            this.prepositional = prepositional;
        }
    }
}
