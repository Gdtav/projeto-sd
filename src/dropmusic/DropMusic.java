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
    public String requestAlbumInfo() throws RemoteException;

    /**
     * Request artist info string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String requestArtistInfo() throws RemoteException;

    /**
     * Logon user string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String logonUser() throws RemoteException;

}
