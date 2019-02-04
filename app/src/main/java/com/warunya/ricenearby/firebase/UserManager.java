package com.warunya.ricenearby.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.warunya.ricenearby.constant.UserType;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class UserManager {

    public static final String STORAGE_PATH_PROFILE = "profiles/";

    private static final UserManager ourInstance = new UserManager();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener userProfileEventListener = null;


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

    public static DatabaseReference getDatabaseReference() {
        return getDatabaseReference(getUid());
    }

    public static DatabaseReference getDatabaseReference(String uid) {
        return getInstance().mDatabase.child("users").child(uid);
    }

    public static void getUserProfile(final OnValueEventListener onValueEventListener) {
        getUserProfile(getUid(), onValueEventListener);
    }

    public static void getUserProfile(String uid, final OnValueEventListener onValueEventListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                onValueEventListener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("users").child(uid).addValueEventListener(getInstance().userProfileEventListener);
    }

    public static void getUserProfileSingleValueEvent(String uid, final OnValueEventListener onValueEventListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                onValueEventListener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("users").child(uid).addListenerForSingleValueEvent(getInstance().userProfileEventListener);
    }

    public static void editUserProfile(final User user, final Handler handler) {
        getDatabaseReference().runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User value = mutableData.getValue(User.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
//                        ?: return Transaction.success(mutableData);
                value.name = user.name;
                value.mobile = user.mobile;
                value.birthday = user.birthday;
                value.gender = user.gender;
                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (handler == null) return;
                handler.onComplete();
            }
        });
    }

    public static void editAddress(final List<Address> addresses, final Handler handler) {
        getDatabaseReference().runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User value = mutableData.getValue(User.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }

                value.addresses = addresses;
                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (handler == null) return;
                handler.onComplete();
            }


        });
    }

    public static void updateUserData(String uid, User user) {
        getInstance().mDatabase.child("users").child(uid).setValue(user);
    }

    public static void updateUserData(String uid, RegisterEntity entity) {
        String email = entity.getEmail();
        String username = entity.getUsername();

        User user = new User(username, email);
        user.userType = UserType.Normal;
        updateUserData(uid, user);
    }

    public static void updateUsername(String uid, final String name) {
        getDatabaseReference(uid).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User value = mutableData.getValue(User.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.name = name;
                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static void updateUserImage(final Upload upload, final Handler handler) {
        getDatabaseReference().runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User value = mutableData.getValue(User.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.image = upload;
                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (handler == null) return;
                handler.onComplete();
            }
        });
    }

    public static void updateUserType(final UserType userType, final Handler handler) {
        getDatabaseReference().runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User value = mutableData.getValue(User.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }

                value.userType = userType;
                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (handler == null) return;
                handler.onComplete();
            }
        });
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public interface OnValueEventListener {
        void onDataChange(DataSnapshot dataSnapshot);
    }

    public interface Handler {
        void onComplete();
    }
}
