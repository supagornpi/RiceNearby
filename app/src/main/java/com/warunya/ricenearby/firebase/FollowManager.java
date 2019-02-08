package com.warunya.ricenearby.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.warunya.ricenearby.model.Follow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowManager {

    private static final FollowManager ourInstance = new FollowManager();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener userProfileEventListener = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    static FollowManager getInstance() {
        return ourInstance;
    }

    private FollowManager() {

    }

    public static boolean isLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static DatabaseReference getUserFavoriteReference(String key) {
        return getInstance().mDatabase.child("user-follows").child(UserManager.getUid()).child(key);
    }

    private static void writeNewFavorite(final String uidSeller, final Handler handler) {
        // Create new submitPost at /user-posts/$userid/$postid
        // and at /posts/$postid simultaneously
        final String key = getInstance().mDatabase.child("user-follows").push().getKey();
        Follow follow = new Follow();
        follow.key = key;
        follow.uidSeller = uidSeller;
        follow.uid = UserManager.getUid();
        Map postValues = follow.toMap();

        Map childUpdates = new HashMap<String, Object>();
        childUpdates.put("/user-follows/" + UserManager.getUid() + "/" + key, postValues);

        getInstance().mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (handler == null) return;
                handler.onComplete();
            }
        });
    }

    public static void follow(final String uidSeller, final Handler handler) {
        getUserFollow(new QueryListener() {
            @Override
            public void onComplete(List<Follow> follows) {
                boolean hasFavorite = false;
                for (Follow follow : follows) {
                    if (follow.uidSeller.equals(uidSeller)) {
                        hasFavorite = true;
                    }
                }
                if (!hasFavorite) {
                    writeNewFavorite(uidSeller, handler);
                }
            }
        });
    }

    public static void hasFollow(final String uidSeller, final HandlerFollow handler) {
        getUserFollow(new QueryListener() {
            @Override
            public void onComplete(List<Follow> follows) {
                boolean hasFavorite = false;
                for (Follow follow : follows) {
                    if (follow.uidSeller.equals(uidSeller)) {
                        hasFavorite = true;
                    }
                }
                handler.onComplete(hasFavorite);
            }
        });
    }

    public static void getUserFollow(final QueryListener queryListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Follow>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Follow>>() {
                };
                Map<String, Follow> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) {
                    if (queryListener == null) return;
                    queryListener.onComplete(new ArrayList<Follow>());
                    return;
                }
                List<Follow> follows = new ArrayList<Follow>(objectHashMap.values());
                if (queryListener == null) return;
                queryListener.onComplete(follows);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("user-follows").child(UserManager.getUid())
                .addListenerForSingleValueEvent(getInstance().userProfileEventListener);
    }

    public static void unFollow(final String uidSeller, final Handler handler) {
        getUserFollow(new QueryListener() {
            @Override
            public void onComplete(List<Follow> follows) {
                for (Follow follow : follows) {
                    if (follow.uidSeller.equals(uidSeller)) {
                        getUserFavoriteReference(follow.key).removeValue();
                    }
                }
                handler.onComplete();
            }
        });
    }

    public interface OnValueEventListener {
        void onDataChange(DataSnapshot dataSnapshot);
    }

    public interface QueryListener {
        void onComplete(List<Follow> follows);
    }

    public interface Handler {
        void onComplete();
    }

    public interface HandlerFollow {
        void onComplete(boolean hasFavorite);
    }
}
