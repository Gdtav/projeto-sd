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
     * @return the string
     * @throws RemoteException the remote exception
     */
    String requestAlbumInfo() throws RemoteException;

    String requestSongInfo() throws RemoteException;

    /**
     * Request artist info string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    String requestArtistInfo() throws RemoteException;

    /**
     * Logon user string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    String logonUser() throws RemoteException;

    void logoffUser() throws RemoteException;
}
