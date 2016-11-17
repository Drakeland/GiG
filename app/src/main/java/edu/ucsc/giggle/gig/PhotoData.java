package edu.ucsc.giggle.gig;

import android.provider.ContactsContract;

/**
 * Created by JanJan on 11/16/2016.
 */

public class PhotoData {

    private String Photos;

    public PhotoData(){

    }

    public PhotoData(String Photos){
        this.Photos = Photos;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }
}
