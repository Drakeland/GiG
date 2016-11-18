package edu.ucsc.giggle.gig;

import android.support.annotation.DrawableRes;

/**
 * Created by JanJan on 10/10/2016.
 */
public class ImageData {
    private int mDrawableRes;

    public ImageData(@DrawableRes int drawable) {
        mDrawableRes = drawable;
    }

    public int getDrawableResource() {
        return mDrawableRes;
    }

    public void setmDrawableRes (@DrawableRes int drawable){ mDrawableRes = drawable;}


    ///////////////
/*    public static final int[] data = {R.drawable.background2};

    public int[] getPhoto(){
        return data;
    }

    public static final String[] strings = {"what"};*/
    /*
    private int image;

    public ImageData(int image){
        this.image = image;
    }

    public int getPhoto(){
        return image;
    }
    public void setPhoto(int image){
        this.image = image;
    }*/
}
