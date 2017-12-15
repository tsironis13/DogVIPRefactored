package com.dogvip.giannis.dogviprefactored.roompersistencedata.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;

import javax.inject.Inject;

/**
 * Created by giannis on 4/12/2017.
 */
@Entity
public class UserRole extends BaseObservable {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int id;
    private int user_id;
    private int role;
    private String name;
    private String surname;
    private String city;
    private String age;
    private String mobile_number;
    private String image_url;
    @Ignore
    private int pet_size;
    @Ignore
    private int years_experience;
    @Ignore
    private int pet_place;
    @Ignore
    private int place_type;
    @Ignore
    private String place_address;
    @Ignore
    private String place_image_urls;
    @Ignore
    private String website;

    @Inject
    public UserRole() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUser_id() { return user_id; }

    public void setUser_id(int user_id) { this.user_id = user_id; }

    public int getRole() { return role; }

    public void setRole(int role) { this.role = role; }

//    @Bindable
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
//        notifyPropertyChanged(BR.name);
    }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getAge() { return age; }

    public void setAge(String age) { this.age = age; }

    public String getMobile_number() { return mobile_number; }

    public void setMobile_number(String mobile_number) { this.mobile_number = mobile_number; }

    public String getImage_url() { return image_url; }

    public void setImage_url(String image_url) { this.image_url = image_url; }

    public int getPet_size() { return pet_size; }

    public void setPet_size(int petSize) { this.pet_size = petSize; }

    public int getYears_experience() { return years_experience; }

    public void setYears_experience(int yearsExperience) { this.years_experience = yearsExperience; }

    public int getPet_place() { return pet_place; }

    public void setPet_place(int petPlace) { this.pet_place = petPlace; }

    public int getPlace_type() { return place_type; }

    public void setPlace_type(int placeType) { this.place_type = placeType; }

    public String getPlace_address() { return place_address; }

    public void setPlace_address(String placeAddress) { this.place_address = placeAddress; }

    public String getPlace_image_urls() { return place_image_urls; }

    public void setPlace_image_urls(String placeImageUrls) { this.place_image_urls = placeImageUrls; }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }
}
