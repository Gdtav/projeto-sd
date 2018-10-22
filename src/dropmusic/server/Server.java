package dropmusic.server;

import dropmusic.DropMusic;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The class for communication with client.
 */
public class Server extends UnicastRemoteObject implements DropMusic {
    /**
     * Instantiates a new Server.
     *
     * @throws RemoteException the remote exception
     */
    protected Server() throws RemoteException {
        super();
    }

    @Override
    public String requestAlbumInfo() throws RemoteException {
        return null;
    }

    @Override
    public String requestSongInfo() throws RemoteException {
        return null;
    }

    @Override
    public String requestArtistInfo() throws RemoteException {
        return null;
    }

    @Override
    public String logonUser() throws RemoteException {
        return null;
    }

    @Override
    public void logoffUser() throws RemoteException {
    }
}