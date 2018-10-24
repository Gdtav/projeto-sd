package dropmusic.server.data;

public class Music {
    private String name;
    private Album album;
    private int track;
    private Artist artist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Music(String name, Album album, int track, Artist artist) {
        this.name = name;
        this.album = album;
        this.track = track;
        this.artist = artist;
    }
}
