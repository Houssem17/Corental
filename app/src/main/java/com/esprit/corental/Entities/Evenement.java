package com.esprit.corental.Entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "evenement_table")
public class Evenement {



    public Evenement(int id_evenement, String nom_evenement, String type_evenement, int distance_evenement, String lieux_evenement, int infoline, int difficulte_evenement, int prix_evenement, int id_user, String date_debut_evenement, String date_fin_evenement, String description_evenement, String photo_evenement, int nbplace_evenement) {
        this.id_evenement = id_evenement;
        this.nom_evenement = nom_evenement;
        this.type_evenement = type_evenement;
        this.distance_evenement = distance_evenement;
        this.lieux_evenement = lieux_evenement;
        this.infoline = infoline;
        this.difficulte_evenement = difficulte_evenement;
        this.prix_evenement = prix_evenement;
        this.id_user = id_user;
        this.date_debut_evenement = date_debut_evenement;
        this.date_fin_evenement = date_fin_evenement;
        this.description_evenement = description_evenement;
        this.photo_evenement = photo_evenement;
        this.nbplace_evenement = nbplace_evenement;
    }



    @PrimaryKey(autoGenerate = true)
    private int id_evenement;
    @ColumnInfo(name = "nom_evenement")
    private String nom_evenement;
    @ColumnInfo(name = "type_evenement")
    private String type_evenement;
    @ColumnInfo(name = "distance_evenement")
    private int distance_evenement;
    @ColumnInfo(name = "lieux_evenement")
    private String lieux_evenement;
    @ColumnInfo(name = "infoline")
    private int infoline;
    @ColumnInfo(name = "difficulte_evenement")
    private int difficulte_evenement;
    @ColumnInfo(name = "prix_evenement")
    private int prix_evenement;
    @ColumnInfo(name = "id_user")
    private int id_user;
    @ColumnInfo(name = "date_debut_evenement")
    private String date_debut_evenement;
    @ColumnInfo(name = "date_fin_evenement")
    private  String date_fin_evenement;
    @ColumnInfo(name = "description_evenement")
    private  String description_evenement;
    @ColumnInfo(name = "photo_evenement")
    private String photo_evenement;
    @ColumnInfo(name = "nbplace_evenement")
    private int nbplace_evenement;

    public int getNbplace_evenement() {
        return nbplace_evenement;
    }

    public void setNbplace_evenement(int nbplace_evenement) {
        this.nbplace_evenement = nbplace_evenement;
    }

    public String getDate_fin_evenement() {
        return date_fin_evenement;
    }

    public void setDate_fin_evenement(String date_fin_evenement) {
        this.date_fin_evenement = date_fin_evenement;
    }



    public String getDate_debut_evenement() {
        return date_debut_evenement;
    }

    public void setDate_debut_evenement(String date_debut_evenement) {
        this.date_debut_evenement = date_debut_evenement;
    }

    public String getPhoto_evenement() {
        return photo_evenement;
    }

    public void setPhoto_evenement(String photo_evenement) {
        this.photo_evenement = photo_evenement;
    }

    public String getDescription_evenement() {
        return description_evenement;
    }

    public void setDescription_evenement(String description_evenement) {
        this.description_evenement = description_evenement;
    }

    public Evenement() {
    }



    /*    public Evenement(int id_event, String nom_evenement, String type_evenement,String date_debut_evenement, int distance_evenement, String lieux_evenement, int infoline, int difficulte_evenement, int prix_evenement, int id_user) {
        this.id_event = id_event;
        this.nom_evenement = nom_evenement;
        this.type_evenement = type_evenement;
        this.date_debut_evenement = date_debut_evenement;
        this.distance_evenement = distance_evenement;
        this.lieux_evenement = lieux_evenement;
        this.infoline = infoline;
        this.difficulte_evenement = difficulte_evenement;
        this.prix_evenement = prix_evenement;
        this.id_user = id_user;
    }

    public Evenement(String nom_evenement, String type_evenement, int distance_evenement, String lieux_evenement, int infoline, int difficulte_evenement, int prix_evenement, int id_user, String description_evenement) {
        this.nom_evenement = nom_evenement;
        this.type_evenement = type_evenement;
        this.distance_evenement = distance_evenement;
        this.lieux_evenement = lieux_evenement;
        this.infoline = infoline;
        this.difficulte_evenement = difficulte_evenement;
        this.prix_evenement = prix_evenement;
        this.id_user = id_user;
        this.description_evenement = description_evenement;
    }*/

    public int getId_evenement() {
        return id_evenement;
    }

    public void setId_evenement(int id_evenement) {
        this.id_evenement = id_evenement;
    }

    public String getNom_evenement() {
        return nom_evenement;
    }

    public void setNom_evenement(String nom_evenement) {
        this.nom_evenement = nom_evenement;
    }

    public String getType_evenement() {
        return type_evenement;
    }

    public void setType_evenement(String type_evenement) {
        this.type_evenement = type_evenement;
    }

    public int getDistance_evenement() {
        return distance_evenement;
    }

    public void setDistance_evenement(int distance_evenement) {
        this.distance_evenement = distance_evenement;
    }

    public String getLieux_evenement() {
        return lieux_evenement;
    }

    public void setLieux_evenement(String lieux_evenement) {
        this.lieux_evenement = lieux_evenement;
    }

    public int getInfoline() {
        return infoline;
    }

    public void setInfoline(int infoline) {
        this.infoline = infoline;
    }

    public int getDifficulte_evenement() {
        return difficulte_evenement;
    }

    public void setDifficulte_evenement(int difficulte_evenement) {
        this.difficulte_evenement = difficulte_evenement;
    }

    public int getPrix_evenement() {
        return prix_evenement;
    }

    public void setPrix_evenement(int prix_evenement) {
        this.prix_evenement = prix_evenement;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
