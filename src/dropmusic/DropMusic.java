package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropMusic extends Remote {

    void requestAlbumInfo(String input) throws RemoteException;

    void requestArtistInfo(String input) throws RemoteException;

    void showAlbumInfo(String message) throws RemoteException;

    void showArtistInfo(String message) throws RemoteException;

    void logonUser() throws RemoteException;

    void logoffUser() throws RemoteException;
}
