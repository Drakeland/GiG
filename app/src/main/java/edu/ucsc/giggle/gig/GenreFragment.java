package edu.ucsc.giggle.gig;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JanJan on 11/19/2016.
 */

public class GenreFragment extends Fragment{
    RecyclerView recyclerView;
    //    ArrayList<ImageData> photos = new ArrayList<ImageData>();
    DatabaseReference mDatabase;

    FirebaseRecyclerAdapter<GenreData, GenreViewHolder> mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("profile_page");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);

        mAdapter = new FirebaseRecyclerAdapter<GenreData, GenreViewHolder>(
                GenreData.class,
                R.layout.recyclerlist_item,
                GenreFragment.GenreViewHolder.class,
                mDatabase
        ) {

            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, GenreData model, int position) {
                viewHolder.setGenre(model.getGenre());

            }
        };

        recyclerView.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<GenreData, GenreViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GenreData, GenreViewHolder>(
                GenreData.class,
                R.layout.recyclerlist_item,
                GenreFragment.GenreViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(GenreViewHolder viewHolder, GenreData model, int position) {
                viewHolder.setGenre(model.getGenre());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CardView cardView;
        TextView textView;

        public GenreViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.listitem_name);
            cardView = (CardView) itemView.findViewById(R.id.cardlist_item);
            mView = itemView;

        }

        public void setGenre(String genre) {
            textView.setText(genre);

        }


    }
}
