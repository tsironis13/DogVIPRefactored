package com.dogvip.giannis.dogviprefactored.room_persistence_data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.StateEntity;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by giannis on 7/12/2017.
 */
@Dao
public interface StateEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(List<StateEntity> obj);

    @Query("UPDATE StateEntity SET updated_at = :updated_at WHERE id IN (:ids)")
    void updateUserStateEntity(long updated_at, int[] ids);

    @Query("DELETE FROM StateEntity WHERE id IN (:ids)")
    void deleteUserState(int[] ids);

    @Query("SELECT * FROM StateEntity WHERE id = 1")
    Single<StateEntity> getOwnerState();

    @Query("SELECT * FROM StateEntity")
    Single<List<StateEntity>> get();

}
