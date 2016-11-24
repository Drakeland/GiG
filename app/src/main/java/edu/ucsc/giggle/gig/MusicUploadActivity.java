package edu.ucsc.giggle.gig;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JanJan on 11/21/2016.
 */


class Mp3Filter implements FilenameFilter{
    public boolean accept(File dir, String name){
        return (name.endsWith(".mp3"));
    }
}

public class MusicUploadActivity extends ListActivity {

    private static final String TAG = "MusicUploadActivity";
    private String SD_PATH = Environment.getExternalStorageDirectory() + "/Music/";
    private List<String> songs = new ArrayList<String>();
    private TextView musicFile;
    private Button submitBtn;
    private ListView listView;
    private Uri musicURI = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    ArrayList<File> songListFile;

    String[] items;
    User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_upload);
        musicFile = (TextView) findViewById(R.id.selected_music);
        updatePlaylist();

        songListFile = findSongs(new File(SD_PATH));
        Log.v(TAG, "PATH: " + SD_PATH);

        listView = (ListView) findViewById(R.id.list_music);
        mProgress = new ProgressDialog(this);

        mUser = new User(getIntent().getExtras());

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("Music").child(mUser.username);

        submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpload();
            }
        });

        items = new String[songListFile.size()];

        for (int i = 0; i < songListFile.size(); i++){
            items[i] = songListFile.get(i).getName().toString();
        }

    }

    private void startUpload() {
        mProgress.setMessage("Uploading...");
        mProgress.show();
        StorageReference filepath = mStorage.child("Song").child(musicURI.getLastPathSegment());
        filepath.putFile(musicURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadURI = taskSnapshot.getDownloadUrl();
                DatabaseReference newPage = mDatabase.push();
                newPage.child("Song").setValue(downloadURI.toString());
                mProgress.dismiss();
                Intent intent = new Intent(MusicUploadActivity.this,ProfileActivity.class);
                intent.putExtras(mUser.bundle());
                startActivity(intent);
            }
        });
    }

    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    public ArrayList<File> findSongs(File root) {
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findSongs(singleFile));
            } else {
                if(singleFile.getName().endsWith("mp3")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    public void onListItemClick(ListView list, View view, int position, long id){
        musicFile.setText(getListAdapter().getItem(position).toString());
        musicURI = Uri.fromFile(new File(songListFile.get(position).toURI()));
        Log.v(TAG, "SONG URI: " + musicURI.toString());
    }

    private void updatePlaylist() {
        File home = new File(SD_PATH);
        Log.v(TAG, "SD_PATH:" + home.toString());
        if (home.listFiles(new Mp3Filter()).length > 0) {
            for (File file : home.listFiles(new Mp3Filter())) {
                songs.add(file.getName());
            }
            ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.item_song, songs);
            setListAdapter(songList);
        }
    }
}