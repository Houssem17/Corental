package com.esprit.corental.Entities;

public class Article {


    private  String titre_article;
    private  String description_article;
    private  String location_article;

    private  String image_article;
    private  String vendre_article;
    private  String louer_article;
    private  int prix_article;
    private  int user_id;
    private  int id_article;


    public Article(String titre_article, String description_article, String location_article, String image_article, int prix_article, int user_id) {
        this.titre_article = titre_article;
        this.description_article = description_article;
        this.location_article = location_article;

        this.image_article = image_article;
        this.prix_article = prix_article;
        this.user_id = user_id;
    }

    public String getVendre_article() {
        return vendre_article;
    }

    public void setVendre_article(String vendre_article) {
        this.vendre_article = vendre_article;
    }

    public String getLouer_article() {
        return louer_article;
    }

    public void setLouer_article(String louer_article) {
        this.louer_article = louer_article;
    }

    public Article(String titre_article) {
        this.titre_article = titre_article;
    }

    public String getTitre_article() {
        return titre_article;
    }

    public void setTitre_article(String titre_article) {
        this.titre_article = titre_article;
    }

    public String getDescription_article() {
        return description_article;
    }

    public void setDescription_article(String description_article) {
        this.description_article = description_article;
    }

    public String getLocation_article() {
        return location_article;
    }

    public void setLocation_article(String location_article) {
        this.location_article = location_article;
    }



    public String getImage_article() {
        return image_article;
    }

    public void setImage_article(String image_article) {
        this.image_article = image_article;
    }

    public int getPrix_article() {
        return prix_article;
    }

    public void setPrix_article(int prix_article) {
        this.prix_article = prix_article;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId_article() {
        return id_article;
    }

    public void setId_article(int id_article) {
        this.id_article = id_article;
    }
}
