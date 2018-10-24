package dropmusic.server;

import dropmusic.DropMusic;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The class for communication with client.
 */
public class Server extends UnicastRemoteObject implements DropMusic {

    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;

    private boolean isMain;
    /**
     * Instantiates a new Server.
     *
     * @throws RemoteException the remote exception
     */
    protected Server(boolean isMain) throws RemoteException {
        super();
        this.isMain = isMain;
    }

    public static void main(String[] args){
        try {
            Registry registry = LocateRegistry.createRegistry(1234);
            Server server = new Server(true);
            registry.rebind("dropmusic",server);
            System.out.println("RMI Server online.");
            MulticastListener listener = new MulticastListener(MULTICAST_ADDRESS, PORT);
            listener.start();
            System.out.println("MulticastListener started");
            MulticastSocket socket = new MulticastSocket();
            while (server.isMain) {
                server.send("Alive",socket);
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message, MulticastSocket socket) {
        byte[] buffer = message.getBytes();
        InetAddress group = null;
        try {
            group = InetAddress.getByName(MULTICAST_ADDRESS);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String requestAlbumInfo(String input, MulticastSocket socket) throws RemoteException {
        return null;
    }

    @Override
    public String requestArtistInfo(String input, MulticastSocket socket) throws RemoteException {
        return null;
    }

    @Override
    public String logonUser(MulticastSocket socket) throws RemoteException {
        return null;
    }

    @Override
    public void logoffUser(MulticastSocket socket) throws RemoteException {

    }


}
