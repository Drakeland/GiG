package edu.ucsc.giggle.gig;

/**
 * Created by austinbrown on 11/21/16.
 */

public class Song {

    private String filename;

    public Song(String file){
        filename = file;
    }

    public String getID(){return filename;}
}
