package com.example.moneycontrol;

public class Articles {
    private String Name;
    private String Description;
    private Double Price;
    private String Image;
    private String IdUser;
    private String IdArticle;

    public Articles() {
    }

    public Articles(String name, String description, Double price, String image, String idUser, String idArticle) {
        Name = name;
        Description = description;
        Price = price;
        Image = image;
        IdUser = idUser;
        IdArticle = idArticle;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getIdArticle() {
        return IdArticle;
    }

    public void setIdArticle(String idArticle) {
        IdArticle = idArticle;
    }
}
