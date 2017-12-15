package com.dogvip.giannis.dogviprefactored.roompersistencedata.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by giannis on 4/12/2017.
 */
@Dao
public interface UserRoleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertUserRoles(List<UserRole> userRole);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUserRole(UserRole userRole);

    @Query("DELETE FROM UserRole")
    void deleteAllUserRoles();

    @Query("DELETE FROM UserRole WHERE _id = :id AND role = :role")
    void deleteUserRole(int id, int role);

//    @Query("SELECT * FROM UserRole")
//    Single<List<UserRole>> getUserRoles();

//    @Query("SELECT * FROM UserRole WHERE user_id = :user_id AND role = :role")
//    Single<UserRole> checkOwnerExists(int user_id, int role);

    @Query("SELECT * FROM UserRole WHERE user_id = :user_id AND role = :role")
    Single<UserRole> fetchOwnerDetails(int user_id, int role);

    @Query("UPDATE UserRole SET _id = :id, name = :name, surname = :surname, city = :city, age = :age, mobile_number = :mobile_number, image_url = :image_url WHERE role = :role")
    int updateUserRole(int id, String name, String surname, String city, String age, String mobile_number, String image_url, int role);


}
