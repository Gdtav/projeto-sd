package WebStrut.Model;

import dropmusic.DropMusic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Drop bean.
 */
public class DropBean {
    private DropMusic server;
    private String username; // username and password supplied by the user
    private String password;
    private String album,artist;
    private String host = "0.0.0.0";
    private int port = 5000;

    /**
     * Instantiates a new Drop bean.
     */
    public DropBean() {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (DropMusic) registry.lookup("dropmusic");
        }
        catch(NotBoundException | RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    /**
     * Register boolean.
     *
     * @param username the username
     * @param password the password
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    public boolean Register(String username, String password) throws RemoteException {
        return server.register(username,password);
    }

    /**
     * Verifies in the Remote Server if the login is correct
     *
     * @return the result of the login in the server.
     * @throws RemoteException the remote exception
     */
    public boolean getUserMatchesPassword() throws RemoteException {
        ArrayList<String> res = server.logonUser(username, password);
        if(res.get(0).equals("true")) {
            return true;
        }
        else
            return false;

    }

    /**
     * Search artists array list.
     *
     * @param art the art
     * @return the array list
     * @throws RemoteException the remote exception
     */
    public ArrayList<String> searchArtists(String art) throws RemoteException {
        return server.artistSearch(art);
    }

    /**
     * Artist info hash map.
     *
     * @return the hash map
     * @throws RemoteException the remote exception
     */
    public HashMap<String, String> artistInfo() throws RemoteException {
        return server.showArtistInfo(artist);
    }

    /**
     * Search albums array list.
     *
     * @param alb the alb
     * @return the array list
     * @throws RemoteException the remote exception
     */
    public ArrayList<String> searchAlbums(String alb) throws RemoteException {
        return server.albumSearch(alb);
    }

    /**
     * Album info hash map.
     *
     * @return the hash map
     * @throws RemoteException the remote exception
     */
    public HashMap<String, String> albumInfo() throws RemoteException {
        return server.showAlbumInfo(album);
    }

    /**
     * Review alb boolean.
     *
     * @param score the score
     * @param desc  the desc
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    public boolean review_alb(int score, String desc) throws RemoteException {
        return server.reviewAlbum(score,desc,album,username);
    }

    /**
     * Add art boolean.
     *
     * @param name  the name
     * @param date1 the date 1
     * @param date2 the date 2
     * @param desc  the desc
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    public boolean add_art(String name, String date1, String date2, String desc) throws RemoteException {
        return server.addArtist(name,date1,date2,desc);
    }

    /**
     * Add alb boolean.
     *
     * @param name the name
     * @param date the date
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    public boolean add_alb(String name, String date) throws RemoteException {
        return server.addAlbum(name,date,artist);
    }

    /**
     * Add song boolean.
     *
     * @param name   the name
     * @param lyrics the lyrics
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    public boolean add_song(String name, String lyrics) throws RemoteException {
        return server.addSong(name,lyrics,artist,album);
    }

    public boolean clean_artists() throws RemoteException {
        return server.cleanArtists();
    }
    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets album.
     *
     * @param album the album
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * Sets artist.
     *
     * @param artist the artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }
}
