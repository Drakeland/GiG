package edu.ucsc.giggle.gig;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by drake on 11/22/16.
 */

public class BandListActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "BandListActivity";

    private Toolbar toolbar;
    private ListView bandList;
    private BandListAdapter adapter;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private User mUser;
    private String mPhotoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_list);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

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
                        mUser.photoUrl = mFirebaseUser.getPhotoUrl().toString();
                        mUser.update();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

        // Initialize Google API
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */).
                addApi(Auth.GOOGLE_SIGN_IN_API).
                addApi(AppInvite.API).
                build();

        bandList = (ListView) findViewById(R.id.band_list);
        adapter = new BandListAdapter(User.usersTable().limitToFirst(50), User.class, R.layout.item_band_list, this);
        bandList.setAdapter(adapter);

        // Get a reference to the todoItems child items it the database
        // Assign a listener to detect changes to the child items
        // of the database reference.

        bandList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) bandList.getItemAtPosition(position);
                Query query = User.usersTable().orderByChild("username").equalTo(user.username);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                            User user = (User) firstChild.getValue(User.class);
                            Intent intent = new Intent(BandListActivity.this, ProfileActivity.class);
                            intent.putExtras(user.bundle());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bandlist, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case R.id.my_profile_menu:
                Intent intent = new Intent(BandListActivity.this, ProfileActivity.class);
                intent.putExtras(mUser.bundle());
                startActivity(intent);
                return true;

            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUser = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
