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

    MediaPlayer mp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("profile_page");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);

        mAdapter = new FirebaseRecyclerAdapter<MusicData, MusicViewHolder>(
                MusicData.class,
                R.layout.music_item,
                MusicViewHolder.class,
                mDatabase
        ) {

            @Override
            protected void populateViewHolder(MusicViewHolder viewHolder, MusicData model, int position) {
                Log.v("this", "SONG:" + model.getSong());
                //Uri myuri = Uri.parse(model.getSong());
                viewHolder.setSong(model.getSong());
                //mp = MediaPlayer.create(getActivity().getApplicationContext(), myuri);

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
                Log.v("this", "SONG:" + model.getSong());
                //Uri myuri = Uri.parse(model.getSong());
                viewHolder.setSong(model.getSong());
                //mp = MediaPlayer.create(getActivity().getApplicationContext(), myuri);
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
                                                mediaPlayer.start();
                                            }
                                        });
                    cardView = (CardView) itemView.findViewById(R.id.music_list_view);
            mView = itemView;

        }

        public void setSong(String song) {
            Log.v("this", "SONG set:" + song);
            textView.setText(song);


        }


    }
}

