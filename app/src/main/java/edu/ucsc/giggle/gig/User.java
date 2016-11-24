package edu.ucsc.giggle.gig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.BundleCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by drake on 11/16/16.
 */

public class User {
    private final String TAG = "User";
    private static final String  USERS_TABLE = "users";
    private static final String  ABOUT_TABLE = "about";
    private static final String GENRES_TABLE = "genres";
    private static final String  MUSIC_TABLE = "music";
    private static final String   GIGS_TABLE = "gigs";
    private static final String PHOTOS_TABLE = "photos";

    // Fields
    public String username;     // As in the email <username@gmail.com> from Google sign-in
                                // - Cannot be changed.
    public String bandname;     // Display name within GiG app. Can be changed freely.
    public String photoUrl;

    // Constructors
    public User() {}

    public User(Bundle bundle) {
        username = bundle.getString("username");
        bandname = bundle.getString("bandname");
        photoUrl = bundle.getString("photoUrl");
    }

    public User(String username, String bandname) {
        this.username = username;
        this.bandname = bandname;
    }

    public User(FirebaseUser firebaseUser) {
        setUsernameFromEmail(firebaseUser.getEmail());
        bandname = firebaseUser.getDisplayName();
        photoUrl = firebaseUser.getPhotoUrl().toString();
    }

    // Methods
    public void setUsernameFromEmail(@NonNull String email) {
        username = email.substring(0, email.lastIndexOf('@'));
    }

    public static DatabaseReference usersTable() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(USERS_TABLE);
    }

    public DatabaseReference  aboutRef() {
        return usersTable().child(username).child(ABOUT_TABLE);
    }

    public DatabaseReference genresRef() {
        return usersTable().child(username).child(GENRES_TABLE);
    }

    public DatabaseReference  musicRef() {
        return usersTable().child(username).child(MUSIC_TABLE);
    }

    public DatabaseReference   gigsRef() {
        return usersTable().child(username).child(GIGS_TABLE);
    }

    public DatabaseReference photosRef() {
        return usersTable().child(username).child(PHOTOS_TABLE);
    }

    // Reflects changes made to User fields on Firebase
    public void update() {
        usersTable().child(username).setValue(this);
    }

    public void updateIfNew() {
        usersTable().child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    update();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public String toString() {
        return String.format("User(%s, %s)", username, bandname);
    }

    public Bundle bundle() {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("bandname", bandname);
        bundle.putString("photoUrl", photoUrl);
        return bundle;
    }
}
