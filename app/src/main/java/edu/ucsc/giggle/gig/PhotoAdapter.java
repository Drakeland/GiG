package edu.ucsc.giggle.gig;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by JanJan on 11/16/2016.
 */

public class PhotoAdapter extends RecyclerView.ViewHolder {


    View mView;
    public PhotoAdapter(View itemView) {
        super(itemView);
        itemView = mView;
    }

    public void setImage(Context ctx, String image){
        ImageView photo_image = (ImageView) mView.findViewById(R.id.listpic_name);
        Picasso.with(ctx).load(image).into(photo_image);
    }
}
