package com.warunya.ricenearby.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.model.User;

public class UserManager {

    private static final String STORAGE_PATH_PROFILE = "profiles/";

    private static final UserManager ourInstance = new UserManager();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {

    }

    public static String getUid() {
        return (FirebaseAuth.getInstance().getCurrentUser() != null) ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
    }

    public static boolean isLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void updateUserData(String uid, User user) {
        getInstance().mDatabase.child("users").child(uid).setValue(user);
    }

    public static void updateUserData(String uid, RegisterEntity entity) {
        String email = entity.getEmail();
        String username = entity.getUsername();

        User user = new User(username, email);
        updateUserData(uid, user);
    }

    public static void updateUserImage(Upload upload) {
        getInstance().mDatabase.child("user-images").child(getUid()).setValue(upload);
    }
}
