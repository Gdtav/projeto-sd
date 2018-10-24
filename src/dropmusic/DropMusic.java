package dropmusic;

import java.net.MulticastSocket;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface Drop music.
 */
public interface DropMusic extends Remote {
    void send(String message, MulticastSocket socket);
    /**
     * Request album info string.
     *
     * @param input the input
     * @return the string
     * @throws RemoteException the remote exception
     */
    String requestAlbumInfo(String input, MulticastSocket socket) throws RemoteException;

    /**
     * Request artist info string.
     *
     * @param input the input
     * @return the string
     * @throws RemoteException the remote exception
     */
    String requestArtistInfo(String input, MulticastSocket socket) throws RemoteException;

    /**
     * Logon user string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    String logonUser(MulticastSocket socket) throws RemoteException;

    /**
     * Logoff user.
     *
     * @throws RemoteException the remote exception
     */
    void logoffUser(MulticastSocket socket) throws RemoteException;
}
