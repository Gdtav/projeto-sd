package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

interface DropMusic extends Remote {

    void register(String username, String password) throws RemoteException;

    boolean logonUser(String username, String password) throws RemoteException;

    ArrayList<String> artistSearch(String input) throws RemoteException;

    ArrayList<String> albumSearch(String input) throws RemoteException;

    ArrayList<String> albumFromArtistSearch(String input) throws RemoteException;

    ArrayList<String> artistInfo(String input) throws RemoteException;

    ArrayList<String> albumInfo(String input) throws RemoteException;

    void reviewAlbum(String review) throws RemoteException;

    void isAlive() throws RemoteException;
}
