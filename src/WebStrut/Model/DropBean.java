package WebStrut.Model;

import dropmusic.DropMusic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class DropBean {
    private DropMusic server;
    private String username; // username and password supplied by the user
    private String password;
    private String album,artist;
    private String host = "0.0.0.0";
    private int port = 5000;

    public DropBean() {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (DropMusic) registry.lookup("dropmusic");
        }
        catch(NotBoundException | RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    public boolean getUserMatchesPassword() throws RemoteException {
        ArrayList<String> res = server.logonUser(username, password);
        if(res.get(0).equals("true")) {
            return true;
        }
        else
            return false;

    }

    public ArrayList<String> searchArtists(String art) throws RemoteException {
        return server.artistSearch(art);
    }

    public HashMap<String, String> artistInfo() throws RemoteException {
        return server.showArtistInfo(artist);
    }

    public ArrayList<String> searchAlbums(String alb) throws RemoteException {
        return server.albumSearch(alb);
    }

    public HashMap<String, String> albumInfo() throws RemoteException {
        return server.showAlbumInfo(album);
    }

    public boolean review_alb(int score, String desc) throws RemoteException {
        return server.reviewAlbum(score,desc,album,username);
    }

    public boolean add_art(String name, String date1, String date2, String desc) throws RemoteException {
        return server.addArtist(name,date1,date2,desc);
    }

    public boolean add_alb(String name, String date) throws RemoteException {
        return server.addAlbum(name,date,artist);
    }

    public boolean add_song(String name, String lyrics) throws RemoteException {
        return server.addSong(name,lyrics,artist,album);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
