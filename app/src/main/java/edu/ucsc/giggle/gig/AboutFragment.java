package edu.ucsc.giggle.gig;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by JanJan on 11/19/2016.
 */

public class AboutFragment extends Fragment{
    RecyclerView recyclerView;
    //    ArrayList<ImageData> photos = new ArrayList<ImageData>();
    DatabaseReference mDatabase;

    FirebaseRecyclerAdapter<AboutData, AboutViewHolder> mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("profile_page");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);

        mAdapter = new FirebaseRecyclerAdapter<AboutData, AboutViewHolder>(
                AboutData.class,
                R.layout.recyclerlist_item,
                AboutFragment.AboutViewHolder.class,
                mDatabase
        ) {

            @Override
            protected void populateViewHolder(AboutFragment.AboutViewHolder viewHolder, AboutData model, int position) {
                viewHolder.setAbout(model.getAbout());

            }
        };

        recyclerView.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AboutData, AboutViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AboutData, AboutViewHolder>(
                AboutData.class,
                R.layout.recyclerlist_item,
                AboutFragment.AboutViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(AboutFragment.AboutViewHolder viewHolder, AboutData model, int position) {
                viewHolder.setAbout(model.getAbout());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class AboutViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CardView cardView;
        TextView textView;

        public AboutViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.listitem_name);
            cardView = (CardView) itemView.findViewById(R.id.cardlist_item);
            mView = itemView;

        }

        public void setAbout(String about) {
            textView.setText(about);

        }


    }
}
