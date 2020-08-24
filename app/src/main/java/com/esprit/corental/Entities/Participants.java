package com.esprit.corental.Entities;

public class Participants {


    private int id_participant;
    private int id_user;
    private int id_evenement;

    public int getId_participant() {
        return id_participant;
    }

    public void setId_participant(int id_participant) {
        this.id_participant = id_participant;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_evenement() {
        return id_evenement;
    }

    public void setId_evenement(int id_evenement) {
        this.id_evenement = id_evenement;
    }

    public Participants() {
    }

    public Participants(int id_user, int id_evenement) {
        this.id_user = id_user;
        this.id_evenement = id_evenement;
    }

    public Participants(int id_participant, int id_user, int id_evenement) {
        this.id_participant = id_participant;
        this.id_user = id_user;
        this.id_evenement = id_evenement;
    }
}
