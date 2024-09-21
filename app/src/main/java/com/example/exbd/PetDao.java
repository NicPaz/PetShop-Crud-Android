package com.example.exbd;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PetDao {
    @Insert
    void inserir(Pet pet);

    @Update
    void atualizar(Pet pet);

    @Delete
    void deletar(Pet pet);

    @Query("SELECT * FROM Pet")
    LiveData<List<Pet>> listar();

    @Query("SELECT * FROM Pet WHERE id = :id LIMIT 1")
    Pet SelectId(int id);

}
