package com.dogvip.giannis.dogviprefactored.roompersistencedata.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserData;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by giannis on 7/12/2017.
 */
@Dao
public interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(List<UserData> obj);

    @Query("UPDATE UserData SET updated_at = :updated_at WHERE data_type IN (:data_types)")
    int updateUserStateEntity(long updated_at, int[] data_types);

    @Query("DELETE FROM UserData WHERE data_type IN (:data_types)")
    void deleteUserState(int[] data_types);

    @Query("SELECT * FROM UserData WHERE data_type = :dataType")
    Single<UserData> getOwnerData(int dataType);

    @Query("SELECT * FROM UserData")
    Single<List<UserData>> get();

}
