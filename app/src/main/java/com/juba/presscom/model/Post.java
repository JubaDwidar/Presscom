package com.juba.presscom.model;

public class Post {
    private String id;
    private String desc;
    private String image;
    private String uImage;

    private String name;

    public Post() {
    }

    public Post(String id, String desc, String image, String name,String uImage) {
        this.id = id;
        this.desc = desc;
        this.image = image;
        this.name = name;
        this.uImage=uImage;
    }

    public String getuImage() {
        return uImage;
    }

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
