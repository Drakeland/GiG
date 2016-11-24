package edu.ucsc.giggle.gig;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by JanJan on 10/19/2016.
 */

public class PhotosTabFragment extends Fragment {
    RecyclerView recyclerView;
    //    ArrayList<ImageData> photos = new ArrayList<ImageData>();
    DatabaseReference mDatabase;

    FirebaseRecyclerAdapter<PhotoData, PhotoViewHolder> mAdapter;
    //static final String TAG = "GigsTabFragment";
    User mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        mUser = new User(getArguments());

        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the todoItems child items it the database
        mDatabase = FirebaseDatabase.getInstance().getReference("photos").child(mUser.username);
        //FirebaseDatabase.getInstance().getReference().child("profile_page");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        recyclerView.setHasFixedSize(true);

        mAdapter = new FirebaseRecyclerAdapter<PhotoData, PhotoViewHolder>(
                PhotoData.class,
                R.layout.item_photo_recycler,
                PhotoViewHolder.class,
                mDatabase){
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
                R.layout.item_photo_recycler,
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





    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        photos.add(new ImageData(R.drawable.background));
        photos.add(new ImageData(R.drawable.background2));
        photos.add(new ImageData(R.drawable.background));
        photos.add(new ImageData(R.drawable.background3));
        photos.add(new ImageData(R.drawable.background3));
        photos.add(new ImageData(R.drawable.background));
        photos.add(new ImageData(R.drawable.background3));
        ImageAdapter imageAdapter = new ImageAdapter(photos);

        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        recyclerView.setAdapter(imageAdapter);
        return rootView;
    }*/



    //////////////////////////
/*    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);
        setupRecyclerView(recyclerView);
        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new PhotoViewAdapter(getActivity(), ImageData.strings));
    }

    public static class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewAdapter.ViewHolder>{
        private String[] mValues;
        private Context mContext;



        public static class ViewHolder extends RecyclerView.ViewHolder{
            public final View mView;
            public final ImageView mImageView;
            public ViewHolder(View view){
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
            }
        }

        public String getValueAt(int position){ return mValues[position];}

        public PhotoViewAdapter(Context context, String[] photos){
            mContext = context;
            mValues = photos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1,parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position){
            Glide.with(holder.mImageView.getContext())
                .load(ImageData.data)
                .fitCenter()
                .into(holder.mImageView);
        }
        @Override
        public int getItemCount() {
            return mValues.length;
        }
    }*/


    /////////////////////////////
    /*
    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);

        return rootView;
    }*/

}
