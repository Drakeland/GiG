package edu.ucsc.giggle.gig;

/**
 * Created by JanJan on 11/22/2016.
 */

public class MusicData {

    private String Song;

    public MusicData(){

    }

    public MusicData(String Song){
        this.Song = Song;
    }

    public String getSong() {
        return Song;
    }

    public void setSong(String song) {
        Song = song;
    }
}
