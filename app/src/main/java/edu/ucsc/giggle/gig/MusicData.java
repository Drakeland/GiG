package edu.ucsc.giggle.gig;


public class MusicData {

    private String Song;

    public MusicData() {}

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