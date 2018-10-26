package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

interface DropMusic extends Remote {

    void register(String username, String password) throws RemoteException;

    boolean[] logonUser(String username, String password) throws RemoteException;

    ArrayList<String> artistSearch(String input) throws RemoteException;

    ArrayList<String> albumSearch(String input) throws RemoteException;

    ArrayList<String> albumFromArtistSearch(String input) throws RemoteException;

    boolean makeEditor(String input) throws RemoteException;

    void reviewAlbum(String review) throws RemoteException;

    void isAlive() throws RemoteException;

    ArrayList<String> editArtistInfo(String input);

    ArrayList<String> showArtistInfo(String input);

    ArrayList<String> editAlbumInfo(String input);

    ArrayList<String> showAlbumInfo(String input);
}
