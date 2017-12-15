package com.dogvip.giannis.dogviprefactored.roompersistencedata.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.Pet;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by giannis on 7/12/2017.
 */
@Dao
public interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertPet(List<Pet> pet);

//    @Query("SELECT * FROM Pet")
//    Single<List<Pet>> getAllPets();

    @Query("SELECT * FROM Pet WHERE user_role_id = :user_role_id")
    Single<List<Pet>> fetchOwnerPets(int user_role_id);

    @Query("DELETE FROM Pet WHERE user_role_id = :user_role_id")
    void deleteOwnerPet(int user_role_id);
}
