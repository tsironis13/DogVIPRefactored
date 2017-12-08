package com.dogvip.giannis.dogviprefactored.room_persistence_data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserDevice;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserRole;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by giannis on 4/12/2017.
 */
@Dao
public interface UserRoleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertUserRole(List<UserRole> userRole);

    @Query("DELETE FROM UserRole")
    void deleteUserRoleData();

    @Query("SELECT * FROM UserRole")
    Single<List<UserRole>> getUserRoles();

    @Query("SELECT * FROM UserRole WHERE user_id = :user_id AND role = 1")
    Single<UserRole> checkOwnerExists(int user_id);

}
