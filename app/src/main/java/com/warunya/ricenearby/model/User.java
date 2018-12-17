package com.warunya.ricenearby.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;
import com.warunya.ricenearby.constant.Gender;

import java.util.List;

@IgnoreExtraProperties
public class User {

    public Uri imageUri;

    public String username;
    public String email;
    public String name;
    public String mobile;
    public Gender gender;
    public String birthday;
    public Upload image;
    public List<Address> addresses;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String name, String mobile, Gender gender, String birthday, Uri imageUri) {
        this.username = username;
        this.name = name;
        this.mobile = mobile;
        this.gender = gender;
        this.birthday = birthday;
        this.imageUri = imageUri;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public User setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public User setImage(Upload image) {
        this.image = image;
        return this;
    }

    public User setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }
}