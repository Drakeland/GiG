package edu.ucsc.giggle.gig;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MusicTabFragment extends Fragment {
    static final String TAG = "MusicTabFragment";
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<MusicData, MusicViewHolder> mAdapter;
    Uri songURI = null;
    MediaPlayer mediaPlayer;
    User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);

        mUser = new User(getArguments());
        mDatabase = FirebaseDatabase.getInstance().getReference("Music").child(mUser.username);
        mAdapter = new FirebaseRecyclerAdapter<MusicData, MusicViewHolder>(
                MusicData.class,
                R.layout.item_music,
                MusicViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MusicViewHolder viewHolder, MusicData model, int position) {
                viewHolder.setSong(getActivity().getApplicationContext(),model.getSong());
                Uri uri = Uri.parse(model.getSong());
                Log.v(TAG, "Music URI: " + uri.toString());
                Log.v(TAG, "SONG:" + model.getSong());
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
                R.layout.item_music,
                MusicViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MusicViewHolder viewHolder, MusicData model, int position) {
                viewHolder.setSong(getActivity().getApplicationContext(),model.getSong());
                Uri uri = Uri.parse(model.getSong());
                Log.v(TAG, "Music URI: " + uri.toString());
                Log.v(TAG, "SONG:" + model.getSong());
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
            Log.v(TAG, "SONG set:" + song);
            textView.setText(song);
            mediaPlayer = MediaPlayer.create(ctx, Uri.parse(song));
        }
    }
}
