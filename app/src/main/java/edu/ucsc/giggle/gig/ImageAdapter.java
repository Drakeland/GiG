package edu.ucsc.giggle.gig;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;

/**
 * Created by JanJan on 10/19/2016.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{


    private List<ImageData> items;
    Context context;

    public ImageAdapter(List<ImageData> items){
        this.items = items;

    }

    public ImageAdapter(Context context){
        this.context = context;
    }


    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photorecycler_item,parent, false));
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        holder.setData(items.get(position));
       // Glide.with(holder.imageView.getContext()).load(photos.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        CardView cardView;
        //public TextView textView;
        public ImageData items;

        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.listpic_name);
            cardView = (CardView) view.findViewById(R.id.photo_cardlist);
        }

        public void setData(ImageData items){
            this.items = items;
            imageView.setImageResource(items.getDrawableResource());
        }
    }

    ////////////////////
    /*    List<String> versionModels;
    //private ImageData[] photoData;
    Context context;

    public ImageAdapter(List<String> versionModels){
        this.versionModels = versionModels;
    }

    public ImageAdapter (Context context) {this.context = context;}

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        holder.title.setText(versionModels.get(position));
    }

    @Override
    public int getItemCount() {
        return versionModels == null ? 0 : versionModels.size();
    }

    *//*
    @Override
    public int getItemCount() {
        return photoData.length;
    }*//*

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgViewIcon;
        TextView title;
        CardView cardItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgViewIcon = (ImageView) itemView.findViewById(R.id.listpic_name);
            title = (TextView) itemView.findViewById(R.id.listitem_name);
            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
        }
    }*/

}
