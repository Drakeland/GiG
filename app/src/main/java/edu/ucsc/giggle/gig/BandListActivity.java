package edu.ucsc.giggle.gig;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by drake on 11/22/16.
 */

public class BandListActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "BandListActivity";

    private ListView bandList;
    private ArrayAdapter<String> adapter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private User mUser;
    private String mPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_list);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mUser = new User();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            String mEmail = mFirebaseUser.getEmail();
            mUser.setUsernameFromEmail(mEmail);

            User.usersTable().child(mUser.username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        // user exists, retrieve from DB snapshot
                        mUser = snapshot.getValue(User.class);
                    } else {
                        // user does not exist, use default bandname from Google Sign-in and add to DB
                        mUser.bandname = mFirebaseUser.getDisplayName();
                        mUser.update();
                        mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

        bandList = (ListView) findViewById(R.id.band_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        bandList.setAdapter(adapter);


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
