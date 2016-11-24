package edu.ucsc.giggle.gig;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JanJan on 10/10/2016.
 */
public class MusicFragment extends Fragment {
    RecyclerView recyclerView;
    //    ArrayList<ImageData> photos = new ArrayList<ImageData>();
    DatabaseReference mDatabase;

    FirebaseRecyclerAdapter<MusicData, MusicViewHolder> mAdapter;

    Uri songURI = null;
    MediaPlayer mp;
    User mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.musicfrag_layout, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);

        Bundle bundle = getArguments();
        mUser = new User(bundle.getString("username"),
                bundle.getString("bandname"));



        mDatabase = FirebaseDatabase.getInstance().getReference("Music").child(mUser.username);

        mAdapter = new FirebaseRecyclerAdapter<MusicData, MusicViewHolder>(
                MusicData.class,
                R.layout.music_item,
                MusicViewHolder.class,
                mDatabase
        ) {

            @Override
            protected void populateViewHolder(MusicViewHolder viewHolder, MusicData model, int position) {
                viewHolder.setSong(getActivity().getApplicationContext(),model.getSong());
                Uri uri = Uri.parse(model.getSong());
                Log.v("this", "Music URI: " + uri.toString());
                Log.v("this", "SONG:" + model.getSong());

            }
        };

        recyclerView.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<MusicData, MusicViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MusicData, MusicViewHolder>(
                MusicData.class,
                R.layout.music_item,
                MusicViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MusicViewHolder viewHolder, MusicData model, int position) {
                viewHolder.setSong(getActivity().getApplicationContext(),model.getSong());
                Uri uri = Uri.parse(model.getSong());
                Log.v("this", "Music URI: " + uri.toString());
                Log.v("this", "SONG:" + model.getSong());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CardView cardView;
        TextView textView;
        MediaPlayer mediaPlayer;

        public MusicViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.music_name);
            textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mediaPlayer!=null)
                                                    mediaPlayer.start();
                                            }
                                        });
            cardView = (CardView) itemView.findViewById(R.id.music_list_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer!=null)
                        mediaPlayer.start();
                }
            });
            mView = itemView;

        }

        public void setSong(Context ctx, String song) {
            Log.v("this", "SONG set:" + song);
//           Uri uri = Uri.parse(song);
            textView.setText(song);
            mediaPlayer = MediaPlayer.create(ctx, Uri.parse(song));


        }


    }
}

