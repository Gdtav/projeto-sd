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

    boolean reviewAlbum(int grade, String review, String album) throws RemoteException;

    void isAlive() throws RemoteException;

    void editArtistInfo(HashMap<String, String> input) throws RemoteException;

    ArrayList<String> showArtistInfo(String input) throws RemoteException;

    void editAlbumInfo(HashMap<String, String> input) throws RemoteException;

    ArrayList<String> showAlbumInfo(String input) throws RemoteException;
}
