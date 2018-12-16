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

    public HashMap<String, String> artistInfo(String art) throws RemoteException {
        return server.showArtistInfo(art);
    }

    public ArrayList<String> searchAlbums(String alb) throws RemoteException {
        return server.albumSearch(alb);
    }

    public HashMap<String, String> albumInfo(String alb) throws RemoteException {
        return server.showAlbumInfo(alb);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
