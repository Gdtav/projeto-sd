package dropmusic.server.data;

public class Artist {
    private String name;
    private Album[] albums;

    public Artist(String name, Album[] albums) {
        this.name = name;
        this.albums = albums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album[] getAlbums() {
        return albums;
    }

    public void setAlbums(Album[] albums) {
        this.albums = albums;
    }
}