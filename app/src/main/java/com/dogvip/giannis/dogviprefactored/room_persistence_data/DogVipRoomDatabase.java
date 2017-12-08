package com.dogvip.giannis.dogviprefactored.room_persistence_data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dogvip.giannis.dogviprefactored.room_persistence_data.dao.PetDao;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.dao.StateEntityDao;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.dao.UserDeviceDao;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.dao.UserRoleDao;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.StateEntity;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.Pet;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserDevice;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserRole;

/**
 * Created by giannis on 4/12/2017.
 */
@Database(entities = {UserDevice.class, UserRole.class, Pet.class, StateEntity.class}, version = 1)
public abstract class DogVipRoomDatabase extends RoomDatabase {
    public abstract UserDeviceDao userDeviceDao();
    public abstract UserRoleDao userRoleDao();
    public abstract PetDao petDao();
    public abstract StateEntityDao stateEntityDao();
}
