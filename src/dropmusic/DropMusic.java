package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropMusic extends Remote {

    void register(String username, String password) throws RemoteException;

    boolean logonUser(String username, String password) throws RemoteException;

    void artistSearch(String input) throws RemoteException;

    void albumSearch(String input) throws RemoteException;

    void albumFromArtistSearch() throws RemoteException;

    void artistInfo() throws RemoteException;

    void albumInfo() throws RemoteException;

    void reviewAlbum(String review) throws RemoteException;

    void isAlive() throws RemoteException;
}
