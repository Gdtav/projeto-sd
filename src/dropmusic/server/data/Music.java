package dropmusic.server.data;

/**
 * The type Music.
 */
public class Music {
    private String name;
    private Album album;
    private int track;
    private Artist artist;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets album.
     *
     * @return the album
     */
    public Album getAlbum() {
        return album;
    }

    /**
     * Sets album.
     *
     * @param album the album
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * Gets track.
     *
     * @return the track
     */
    public int getTrack() {
        return track;
    }

    /**
     * Sets track.
     *
     * @param track the track
     */
    public void setTrack(int track) {
        this.track = track;
    }

    /**
     * Gets artist.
     *
     * @return the artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * Sets artist.
     *
     * @param artist the artist
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * Instantiates a new Music.
     *
     * @param name   the name
     * @param album  the album
     * @param track  the track
     * @param artist the artist
     */
    public Music(String name, Album album, int track, Artist artist) {
        this.name = name;
        this.album = album;
        this.track = track;
        this.artist = artist;
    }
}
