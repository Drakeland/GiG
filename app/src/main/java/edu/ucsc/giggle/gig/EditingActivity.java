package edu.ucsc.giggle.gig;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

/**
 * Created by JanJan on 11/16/2016.
 */

public class EditingActivity extends AppCompatActivity{
//
//
//    private Button mSubmitBtn;
//    private EditText mAboutText;
//    private EditText mGenreText;
//    private ImageButton imageButton;
//    private Uri PhotoURI = null;
//
//    private StorageReference mStorage;
//
//    private DatabaseReference mDatabase;
//
//    private ProgressDialog mProgress;
//
//    private static final int GALLERY_REQUEST = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_editing);
//
//
//        mProgress = new ProgressDialog(this);
//
//        mStorage = FirebaseStorage.getInstance().getReference();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("profile_page");
//
//        mAboutText = (EditText) findViewById(R.id.aboutText);
//        mGenreText = (EditText) findViewById(R.id.genreText);
//
//        imageButton = (ImageButton) findViewById(R.id.photoBtn);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//                                           @Override
//                                           public void onClick(View v) {
//                                               // Create intent to Open Image applications like Gallery, Google Photos
//                                               Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                               // Start the Intent
//                                               startActivityForResult(galleryIntent, GALLERY_REQUEST);
//                                           }
//                                       });
//
//        mSubmitBtn = (Button) findViewById(R.id.submitbtn);
//        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startPosting();
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
//            PhotoURI = data.getData();
//            imageButton.setImageURI(PhotoURI);
//        }
//    }
//
//    private void startPosting(){
//        mProgress.setMessage("Posting....");
//        mProgress.show();
//        final String about_val = mAboutText.getText().toString().trim();
//        final String genre_val = mGenreText.getText().toString().trim();
//
//        if(!TextUtils.isEmpty(about_val) && !TextUtils.isEmpty(genre_val) && PhotoURI != null){
//            StorageReference filepath = mStorage.child("Photos").child(PhotoURI.getLastPathSegment());
//
//            filepath.putFile(PhotoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//
//                    DatabaseReference newPage = mDatabase.push();
//                    newPage.child("About").setValue(about_val);
//                    newPage.child("Genre").setValue(genre_val);
//                    newPage.child("Photos").setValue(downloadUrl.toString());
//
//                    mProgress.dismiss();
//
//                    startActivity(new Intent(EditingActivity.this, ProfileActivity.class));
//
//                }
//            });
//
//        }
//    }


}
