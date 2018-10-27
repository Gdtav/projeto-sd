package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

interface DropMusic extends Remote {

    void register(String username, String password) throws RemoteException;

    boolean[] logonUser(String username, String password) throws RemoteException;

    ArrayList<String> artistSearch(String input) throws RemoteException;

    ArrayList<String> albumSearch(String input) throws RemoteException;

    ArrayList<String> albumFromArtistSearch(String input) throws RemoteException;

    boolean makeEditor(String input) throws RemoteException;

    void reviewAlbum(int grade, String review, String album) throws RemoteException;

    void isAlive() throws RemoteException;

    void editArtistInfo(HashMap<String, String> input);

    ArrayList<String> showArtistInfo(String input);

    void editAlbumInfo(HashMap<String, String> input);

    ArrayList<String> showAlbumInfo(String input);
}
