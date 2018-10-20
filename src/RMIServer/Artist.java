package RMIServer;

/**
 * The type Artist.
 */
class Artist {
    private String name;
    private Album[] albums;

    /**
     * Instantiates a new Artist.
     *
     * @param name   the name
     * @param albums the albums
     */
    public Artist(String name, Album[] albums) {
        this.name = name;
        this.albums = albums;
    }

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
     * Get albums album [ ].
     *
     * @return the album [ ]
     */
    public Album[] getAlbums() {
        return albums;
    }

    /**
     * Sets albums.
     *
     * @param albums the albums
     */
    public void setAlbums(Album[] albums) {
        this.albums = albums;
    }
}