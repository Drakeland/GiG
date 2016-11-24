package edu.ucsc.giggle.gig;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "ProfileActivity";
    public static final String ANONYMOUS = "anonymous";
    private CoordinatorLayout coordinatorLayout;
    private ActionBar actionBar;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private String mUsername;
    private String mPhotoUrl;
    private Uri PhotoURI;
    private ProgressDialog progressDialog;
    private ImageButton imageButton;
    private GoogleApiClient mGoogleApiClient;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private static final int RESULT_LOAD_IMG = 1;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private User mUser;
    private User pUser;
    private boolean ownProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        mStorage = FirebaseStorage.getInstance().getReference();

        imageButton = (ImageButton) findViewById(R.id.photo_name);
        progressDialog = new ProgressDialog(this);

        pUser = new User(getIntent().getExtras());

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
            if (!mUser.username.equals(pUser.username)) {
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
            } else {
                ownProfile = true;
                mUser.bandname = pUser.bandname;

            }
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("photos").child(mUser.username);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(pUser.bandname);

        ImageView profile_pic = (ImageView) findViewById(R.id.profile_pic);
        Glide.with(this).load(pUser.photoUrl).into(profile_pic);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        viewPager = (ViewPager)findViewById(R.id.tab_viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        AboutTabFragment   aboutTabFragment = new  AboutTabFragment();
        GenreTabFragment   genreTabFragment = new  GenreTabFragment();
        MusicTabFragment   musicTabFragment = new  MusicTabFragment();
        GigsTabFragment     gigsTabFragment = new   GigsTabFragment();
        PhotosTabFragment photosTabFragment = new PhotosTabFragment();

        aboutTabFragment .setArguments(pUser.bundle());
        genreTabFragment .setArguments(pUser.bundle());
        musicTabFragment .setArguments(pUser.bundle());
        gigsTabFragment  .setArguments(pUser.bundle());
        photosTabFragment.setArguments(pUser.bundle());

        adapter.addFrag( aboutTabFragment, getString(R.string.about_label));
        adapter.addFrag( genreTabFragment, getString(R.string.genres_label));
        adapter.addFrag( musicTabFragment, getString(R.string.music_label));
        adapter.addFrag(  gigsTabFragment, getString(R.string.gigs_label));
        adapter.addFrag(photosTabFragment, getString(R.string.photos_label));
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.about_label:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.genres_label:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.music_label:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.gigs_label:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.photos_label:
                        viewPager.setCurrentItem(4);
                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        // only allow profile editing if the user is viewing their own profile
        if (!ownProfile) {
            menu.removeItem(R.id.edit_profile_menu);
            menu.removeItem(R.id.upload_music_menu);
            menu.removeItem(R.id.upload_photo_menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUser = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;

            case R.id.edit_profile_menu:
                showEditProfileDialog();
                return true;

            case R.id.upload_music_menu:
                Intent intent = new Intent(ProfileActivity.this, MusicUploadActivity.class);
                intent.putExtras(mUser.bundle());
                startActivity(intent);
                return true;

            case R.id.upload_photo_menu:
                showUploadProfileDialog();
                return true;

            case R.id.action_settings:
                return true;

            case android.R.id.home:
                finish();
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

    public void showEditProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View inflater = layoutInflater.inflate(R.layout.dialog_edit_profile, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.edit_profile);
        alert.setIcon(R.drawable.ic_mode_edit_black);
        alert.setView(inflater);

        final EditText text_bandname = (EditText) inflater.findViewById(R.id.edit_bandname);

        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String new_bandname = text_bandname.getText().toString();
                actionBar.setTitle(new_bandname);
                mUser.bandname = new_bandname;
                mUser.update();

                dialog.dismiss();
            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }
    private void showUploadProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View inflater = layoutInflater.inflate(R.layout.dialog_upload_photo, null);
        imageButton = (ImageButton) inflater.findViewById(R.id.photo_name);
        final Dialog dialog = new Dialog(this);

        dialog.setTitle(R.string.upload_photo);
        dialog.setContentView(inflater);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }

        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View inflater = layoutInflater.inflate(R.layout.dialog_upload_photo, null);
            imageButton = (ImageButton) inflater.findViewById(R.id.photo_name);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            PhotoURI = data.getData();
            Log.v(TAG, "PhotoURI:" + PhotoURI.toString());
            imageButton.setImageURI(PhotoURI);

            alert.setTitle(R.string.upload_photo);
            alert.setIcon(R.drawable.ic_mode_edit_black);
            alert.setView(inflater);

            alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();
                    StorageReference filepath = mStorage.child("Photos").child(PhotoURI.getLastPathSegment());

                    filepath.putFile(PhotoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            DatabaseReference newPage = mDatabase.push();
                            newPage.child("Photos").setValue(downloadUrl.toString());

                            progressDialog.dismiss();
                        }
                    });

                    dialog.dismiss();
                }
            });

            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });

            alert.show();

        }
    }

}


