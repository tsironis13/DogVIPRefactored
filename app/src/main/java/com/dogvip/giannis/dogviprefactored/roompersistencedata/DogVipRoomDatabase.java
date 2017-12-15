package com.dogvip.giannis.dogviprefactored.roompersistencedata;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dogvip.giannis.dogviprefactored.roompersistencedata.dao.PetDao;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.dao.UserDataDao;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.dao.UserDeviceDao;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.dao.UserRoleDao;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserData;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.Pet;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserDevice;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;

/**
 * Created by giannis on 4/12/2017.
 */
@Database(entities = {UserDevice.class, UserRole.class, Pet.class, UserData.class}, version = 1)
public abstract class DogVipRoomDatabase extends RoomDatabase {
    public abstract UserDeviceDao userDeviceDao();
    public abstract UserRoleDao userRoleDao();
    public abstract PetDao petDao();
    public abstract UserDataDao stateEntityDao();
}
