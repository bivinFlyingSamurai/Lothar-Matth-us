package com.Matthaus.Lothar.keyboard.models;

import java.io.Serializable;

public class BackgroundImageModel implements Serializable {
   private Integer image;
   private String title;

    public BackgroundImageModel(Integer image, String title) {
        this.image = image;
        this.title = title;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
