package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface Drop music.
 */
public interface DropMusic extends Remote {

    /**
     * Request album info string.
     *
     * @param input the input
     * @throws RemoteException the remote exception
     */
    void requestAlbumInfo(String input) throws RemoteException;

    /**
     * Request artist info string.
     *
     * @param input the input
     * @throws RemoteException the remote exception
     */
    void requestArtistInfo(String input) throws RemoteException;

    void showAlbumInfo(String message) throws RemoteException;

    void showArtistInfo(String message) throws RemoteException;

    /**
     * Logon user string.
     * @throws RemoteException the remote exception
     */
    void logonUser() throws RemoteException;

    /**
     * Logoff user.
     *
     * @throws RemoteException the remote exception
     */
    void logoffUser() throws RemoteException;
}
