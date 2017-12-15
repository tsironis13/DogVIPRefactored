package com.dogvip.giannis.dogviprefactored.roompersistencedata.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import javax.inject.Inject;

/**
 * Created by giannis on 7/12/2017.
 */

//@Entity(foreignKeys = @ForeignKey(entity = UserRole.class,
//                                                parentColumns = "_id",
//                                                childColumns = "user_role_id",
//                                                onDelete = CASCADE, onUpdate = CASCADE))
@Entity
public class UserData {
    /*
     * id references set of data that are't updated for 45 minutes
     * 1-> owner role data, user pet data
     * 2-> pet sitter data
     * 3-> prof data
     * 4-> all pet data, excluding owner pets
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int entity_ID;
//    private int user_role_id;
    private int data_type;
//    private boolean stale;
    private long updated_at;

    @Inject
    public UserData() {}

    public int getEntity_ID() { return entity_ID; }

    public void setEntity_ID(int entity_id) { this.entity_ID = entity_id; }

//    public int getUser_role_id() { return user_role_id; }
//
//    public void setUser_role_id(int user_role_id) { this.user_role_id = user_role_id; }

    public int getData_type() { return data_type; }

    public void setData_type(int data_type) { this.data_type = data_type; }

    //    public boolean isStale() { return stale; }
//
//    public void setStale(boolean stale) { this.stale = stale; }

    public long getUpdated_at() { return updated_at; }

    public void setUpdated_at(long updated_at) { this.updated_at = updated_at; }
}
