package edu.ucsc.giggle.gig;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
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
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final int RESULT_LOAD_IMG = 1;
    private static final String TAG = "ProfileActivity";
    public static final String ANONYMOUS = "anonymous";
    String imgDecodableString;

    private CoordinatorLayout coordinatorLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    //ArrayList<ImageData> items = new ArrayList<ImageData>();

    private Uri PhotoURI;
    private ActionBar actionBar;
    ImageButton imageButton;

    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    private User mUser;
    Bitmap profilePic = null;


    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        Log.v("this", "This works");

        progressDialog = new ProgressDialog(this);

        imageButton = (ImageButton) findViewById(R.id.photo_name);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("profile_page");

        mUser = new User();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

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
                    }
                    actionBar.setTitle(mUser.bandname);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            // Initialize Google API
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .addApi(AppInvite.API)
                    .build();


///////////////////////////////////////////////////////////////////////////////////////////////////////////////
// bottom bar
//////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.three_buttons_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.event_item:
                        Snackbar.make(coordinatorLayout, "Event Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.music_item:
                        Snackbar.make(coordinatorLayout, "Music Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.social_item:
                        Snackbar.make(coordinatorLayout, "Social Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });*/

            // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
            //bottomBar.setActiveTabColor("#33dbff");

            // Use the dark theme. Ignored on mobile when there are more than three tabs.
            //bottomBar.useDarkTheme(true);

            // Use custom text appearance in tab titles.
            //bottomBar.setTextAppearance(R.style.MyTextAppearance);

            // Use custom typeface that's located at the "/src/main/assets" directory. If using with
            // custom text appearance, set the text appearance first.
            //bottomBar.setTypeFace("MyFont.ttf");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
// end
//////////////////////////////////////////////////////////////////////////////////////////////////////
            actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);

            ImageView profile_pic = (ImageView) findViewById(R.id.tab_layout);
            Glide.with(this).load(mPhotoUrl).into(profile_pic);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
            if (navView != null) {
                setupDrawerContent(navView);
            }

            viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
            if (viewPager != null) {
                setupViewPager(viewPager);
            }


            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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
    }


    public void loadImagefromGallery(View v){
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                progressDialog.setMessage("Uploading.....");
                progressDialog.show();

                Uri selectedImage = data.getData();
                Log.v("this", "Work?");
                Log.v("this", "Page: " + tabLayout.getTabCount());
                StorageReference filepath = mStorage.child("Photos").child(selectedImage.getLastPathSegment());
                Log.v("this", "Work Work?");

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.tab_layout);
                // Set the Image in ImageView after decoding the String
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(imgDecodableString);
                Drawable d =new BitmapDrawable(yourSelectedImage);


                imgView.setImageDrawable(d);

                Log.v("this", "Nah it works");
                filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfileActivity.this,"Upload Done",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

}*/




    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    //    adapter.addFrag(new FloatingLabelsFragment(), "Floating Labels");
    //    adapter.addFrag(new FABLayoutFragment(), "FAB");
    //    adapter.addFrag(new SnackBarFragment(), "Snackbar");
        adapter.addFrag(new AboutFragment(), "About");
        adapter.addFrag(new GenreFragment(), "Genres");
        adapter.addFrag(new GenreFragment(), "Music");
        adapter.addFrag(new GenreFragment(), "GiGs");
        adapter.addFrag(new PhotoFragment(), "Photos");
    //    adapter.addFrag(new CoordinatorFragment2(), "Media");
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
                    case R.id.genre_label:
                        viewPager.setCurrentItem(1);
                        break;
                    /*
                    case R.id.drawer_snackbar:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.drawer_coordinator:
                        viewPager.setCurrentItem(3);
                        break; */
                    case R.id.music_label:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.gig_label:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.photo_label:
                        viewPager.setCurrentItem(4);
                        break;
                    /*
                    case R.id.media_label:
                        viewPager.setCurrentItem(5);
                        break;*/
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.v("this", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds photos to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.upload_settings){
            return true;
        }

        if (id == R.id.editing_settings){
            startActivity(new Intent(ProfileActivity.this, EditingActivity.class));
        }

        switch (id){
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);*/

        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));

                return true;

            case R.id.upload_music:
                startActivity(new Intent(ProfileActivity.this,MusicUploadActivity.class));
                return true;

            case R.id.edit_profile_menu:
                if(viewPager.getCurrentItem() == 0)
                    showEditAboutDialog();
                if(viewPager.getCurrentItem() == 1)
                    showEditGenreDialog();
                else {
                    showEditProfileDialog();
                }
                return true;

            case R.id.upload_photo_menu:
                showUploadProfileDialog();
                return true;

            case R.id.action_settings:
                return true;

            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEditGenreDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View inflater = layoutInflater.inflate(R.layout.dialog_edit_profile, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Edit Genre");
        alert.setIcon(R.drawable.ic_mode_edit_black);
        alert.setView(inflater);

        final EditText genre_text = (EditText) inflater.findViewById(R.id.edit_band_name);

        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {

                String genre_val = genre_text.getText().toString();
                DatabaseReference newPage = mDatabase.push();
                newPage.child("Genre").setValue(genre_val);

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

    private void showEditAboutDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View inflater = layoutInflater.inflate(R.layout.dialog_edit_profile, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Edit About");
        alert.setIcon(R.drawable.ic_mode_edit_black);
        alert.setView(inflater);

        final EditText about_text = (EditText) inflater.findViewById(R.id.edit_band_name);

        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String about_val = about_text.getText().toString();
                DatabaseReference newPage = mDatabase.push();
                newPage.child("About").setValue(about_val);
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



    //////////// REMOVE THIS FOR A WORKING APP ///////////////////////

    private void showUploadProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View inflater = layoutInflater.inflate(R.layout.dialog_upload_profile, null);
        ImageButton imageButton = (ImageButton) inflater.findViewById(R.id.photo_name);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Upload Photo");
        alert.setIcon(R.drawable.ic_mode_edit_black);
        alert.setView(inflater);



        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View inflater = layoutInflater.inflate(R.layout.dialog_upload_profile, null);
            ImageButton photo_btn = (ImageButton) inflater.findViewById(R.id.photo_name);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            PhotoURI = data.getData();
            Log.v("this", "PhotoURI:" + PhotoURI.toString());
            photo_btn.setImageURI(PhotoURI);

        }
    }

    public void showEditProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View inflater = layoutInflater.inflate(R.layout.dialog_edit_profile, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.edit_profile);
        alert.setIcon(R.drawable.ic_mode_edit_black);
        alert.setView(inflater);

        final EditText text_band_name = (EditText) inflater.findViewById(R.id.edit_band_name);

        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String new_bandname = text_band_name.getText().toString();
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
}


