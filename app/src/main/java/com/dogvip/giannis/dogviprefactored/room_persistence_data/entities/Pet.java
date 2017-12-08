package com.dogvip.giannis.dogviprefactored.room_persistence_data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import javax.inject.Inject;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by giannis on 7/12/2017.
 */
@Entity(foreignKeys = @ForeignKey(entity = UserRole.class,
                                  parentColumns = "id",
                                  childColumns = "user_role_id",
                                  onDelete = CASCADE),
        indices = @Index(value="user_role_id"))
public class Pet {

    @Inject
    public Pet() {}

    @PrimaryKey
    private int p_id;
    private int user_role_id;
    private String p_name;
    private String race;
    private int microchip;
    private int genre;
    private long p_age;
    private String p_city;
    private String weight;
    private int neutered;
    private String chtr;
    private String image_urls;
    private String main_image;
    private long date_created;
    private int half_blood;
    private int total_likes;

    public int getP_id() { return p_id; }

    public void setP_id(int p_id) { this.p_id = p_id; }

    public int getUser_role_id() { return user_role_id; }

    public void setUser_role_id(int user_role_id) { this.user_role_id = user_role_id; }

    public String getP_name() { return p_name; }

    public void setP_name(String p_name) { this.p_name = p_name; }

    public String getRace() { return race; }

    public void setRace(String race) { this.race = race; }

    public int getMicrochip() { return microchip; }

    public void setMicrochip(int microchip) { this.microchip = microchip; }

    public int getGenre() { return genre; }

    public void setGenre(int genre) { this.genre = genre; }

    public long getP_age() { return p_age; }

    public void setP_age(long p_age) { this.p_age = p_age; }

    public String getP_city() { return p_city; }

    public void setP_city(String p_city) { this.p_city = p_city; }

    public String getWeight() { return weight; }

    public void setWeight(String weight) { this.weight = weight; }

    public int getNeutered() { return neutered; }

    public void setNeutered(int neutered) { this.neutered = neutered; }

    public String getChtr() { return chtr; }

    public void setChtr(String chtr) { this.chtr = chtr; }

    public String getImage_urls() { return image_urls; }

    public void setImage_urls(String image_urls) { this.image_urls = image_urls; }

    public String getMain_image() { return main_image; }

    public void setMain_image(String main_image) { this.main_image = main_image; }

    public long getDate_created() { return date_created; }

    public void setDate_created(long date_created) { this.date_created = date_created; }

    public int getHalf_blood() { return half_blood; }

    public void setHalf_blood(int half_blood) { this.half_blood = half_blood; }

    public int getTotal_likes() { return total_likes; }

    public void setTotal_likes(int total_likes) { this.total_likes = total_likes; }
}
