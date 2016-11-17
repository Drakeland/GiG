package edu.ucsc.giggle.gig;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by drake on 11/16/16.
 */

public class User {
    public String username;
    public String bandname;
    public Drawable profilePic;

    public User(String username, String bandname) {
        this.username = username;
        this.bandname = bandname;
    }

    public void setProfilePic(Drawable profilePic) {
        this.profilePic = profilePic;
    }

    // Reflects changes made to User fields on Firebase
    public void updateData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        usersRef.child(username).setValue(this);
    }


}
