package com.dogvip.giannis.dogviprefactored.roompersistencedata.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserDevice;

import io.reactivex.Single;

/**
 * Created by giannis on 4/12/2017.
 */
@Dao
public interface UserDeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUserDevice(UserDevice userDevice);

    //no data in the database -> query returns no rows, Single will trigger onError(EmptyResultSetException.class)
    @Query("SELECT * FROM UserDevice WHERE device_model = :device_model")
    Single<UserDevice> getDeviceDetails(String device_model);

    @Query("UPDATE UserDevice SET token_synced = :token_synced, user_id = :user_id WHERE device_model = :device_model")
    int updateUserId(boolean token_synced, int user_id, String device_model);

    @Query("UPDATE UserDevice SET token_synced = :token_synced WHERE device_model = :device_model")
    int updateTokenSynced(boolean token_synced, String device_model);

    @Query("UPDATE UserDevice SET token_synced = :token_synced, fcm_registration_token = :fcm_registration_token WHERE device_model = :device_model")
    int updateFcmToken(boolean token_synced, String fcm_registration_token, String device_model);

}
