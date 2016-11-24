package edu.ucsc.giggle.gig;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JanJan on 10/19/2016.
 */

public class PhotoFragment extends Fragment{
    RecyclerView recyclerView;
    //    ArrayList<ImageData> photos = new ArrayList<ImageData>();
    DatabaseReference mDatabase;

    FirebaseRecyclerAdapter<PhotoData, PhotoViewHolder> mAdapter;
    User mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.photosfrag_layout, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        recyclerView.setHasFixedSize(true);

        Bundle bundle = getArguments();
        mUser = new User(bundle.getString("username"),
                bundle.getString("bandname"));



        mDatabase = FirebaseDatabase.getInstance().getReference("photos").child(mUser.username);
        //.child(mUser.username);
        mAdapter = new FirebaseRecyclerAdapter<PhotoData, PhotoViewHolder>(
                PhotoData.class,
                R.layout.photorecycler_item,
                PhotoViewHolder.class,
                mDatabase
        ){

            @Override
            protected void populateViewHolder(PhotoViewHolder viewHolder, PhotoData model, int position) {
                viewHolder.setPhoto(getActivity().getApplicationContext(), model.getPhotos());
                Log.v("this", "Photo: " + model.getPhotos());
            }
        };

        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<PhotoData,PhotoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PhotoData, PhotoViewHolder>(
                PhotoData.class,
                R.layout.photorecycler_item,
                PhotoViewHolder.class,
                mDatabase
        ){
            @Override
            protected void populateViewHolder(PhotoViewHolder viewHolder, PhotoData model, int position) {
                viewHolder.setPhoto(getActivity().getApplicationContext(), model.getPhotos());
                Log.v("this", "Photo 2: " + model.getPhotos());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CardView cardView;
        ImageView photo_image;
        public PhotoViewHolder(View itemView){
            super(itemView);
            photo_image = (ImageView) itemView.findViewById(R.id.listpic_name);
            cardView = (CardView) itemView.findViewById(R.id.photo_cardlist);

        }

        public void setPhoto(Context ctx, String photo){
            Log.v("this", "Photo URL:" + photo);
            Picasso.with(ctx).load(photo).into(photo_image);
        }


    }







}
