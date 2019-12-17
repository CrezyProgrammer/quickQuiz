package com.dreamtechdesignandstudio.quickquiz1.activity;

public class Model {
    String name,image,id;
            int right;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public Model(String name, String image, String id, int right) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.right = right;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Model() {
    }
}
