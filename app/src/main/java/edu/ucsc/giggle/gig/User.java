package edu.ucsc.giggle.gig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
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
    private static final String USERS_TABLE = "users";

    // Fields
    public String username;     // As in the email <username@gmail.com> from Google sign-in
                                // - Cannot be changed.
    public String bandname;     // Display name within GiG app. Can be changed freely.
    public String photoUrl;

    // Constructors
    public User () {}

    public User(final String username) {
        this.username = username;
        Log.d(TAG, "Attempting to lookup User(" + username + "). ---------------------------");
        usersTable().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator(); it.hasNext(); ) {
                    User user = (User) it.next().getValue(User.class);
                    Log.d(TAG, "- " + user.toString());
                    Log.d(TAG, "- - Comparing \"" + user.username + "\" vs \"" + username + "\".");
                    if (user.username.equals(username)) {
                        Log.d(TAG, "- - Correct user found, setting bandname. --------------------------------");
                        bandname = user.bandname;
                        break;
                    } else {
                        Log.d(TAG, "- - Comparison failed.");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public User(String username, String bandname) {
        this.username = username;
        this.bandname = bandname;
    }

    // Methods
    public void setUsernameFromEmail(@NonNull String email) {
        username = email.substring(0, email.lastIndexOf('@'));
//        Log.d(TAG, "username := \"" + username + "\"");
    }

    public DatabaseReference gigsRef() {
        return usersTable().child(username).child("gigs");
    }

    public static DatabaseReference usersTable() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(USERS_TABLE);
    }

    // Reflects changes made to User fields on Firebase
    public void update() {
        usersTable().child(username).setValue(this);
    }

    @Override
    public String toString() {
        return String.format("User(%s, %s)", username, bandname);
    }
}
