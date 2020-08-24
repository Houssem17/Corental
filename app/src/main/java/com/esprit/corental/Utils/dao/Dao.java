package com.esprit.corental.Utils.dao;


import com.esprit.corental.Entities.Evenement;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@androidx.room.Dao
public interface Dao {

    @Insert
    void insertOne(Evenement evenement);
    @Query("SELECT * FROM evenement_table WHERE id_evenement=:id")
    int check_item(int id);
    @Delete
    void delete(Evenement produit);
    @Query("SELECT * FROM evenement_table")
    List<Evenement> getAll();

}
