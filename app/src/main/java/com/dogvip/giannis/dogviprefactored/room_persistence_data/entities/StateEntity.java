package com.dogvip.giannis.dogviprefactored.room_persistence_data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import javax.inject.Inject;

/**
 * Created by giannis on 7/12/2017.
 */
@Entity
public class StateEntity {
    /*
     * id references set of data that are't updated for 45 minutes
     * 1-> user roles data, user pet data
     * 2-> all pet data, excluding user pets
     */
    @PrimaryKey
    private int id;
//    private boolean stale;
    private long updated_at;

    @Inject
    public StateEntity() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

//    public boolean isStale() { return stale; }
//
//    public void setStale(boolean stale) { this.stale = stale; }

    public long getUpdated_at() { return updated_at; }

    public void setUpdated_at(long updated_at) { this.updated_at = updated_at; }
}
