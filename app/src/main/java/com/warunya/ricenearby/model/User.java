package com.warunya.ricenearby.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.warunya.ricenearby.constance.Gender;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public String name;
    public String lastName;
    public String mobile;
    public Gender gender;
    public String birthday;
    public Upload image;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
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