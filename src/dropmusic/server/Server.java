package dropmusic.server;

import dropmusic.DropMusic;
import dropmusic.MulticastListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements DropMusic {

    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;
    private static MulticastSocket socket;
    private static MulticastListener listener = new MulticastListener(MULTICAST_ADDRESS, PORT);
    static {
        try {
            socket = new MulticastSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isMain;
    private Server(boolean isMain) throws RemoteException {
        super();
        this.isMain = isMain;
    }

    public static void main(String[] args){
        try {
            Registry registry = LocateRegistry.createRegistry(1234);
            Server server = new Server(true);
            registry.rebind("dropmusic",server);
            System.out.println("RMI Server online.");

            listener.start();
            System.out.println("MulticastListener started");
            while (server.isMain) {
                server.send("Alive");
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void send(String message) {
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

    private void read() {
        String message = listener.getMessage();
        if (message.contains("response")) {

        }
    }

    @Override
    public void requestAlbumInfo(String input) throws RemoteException {
        String message = null;
        send(message);
        read();
    }

    @Override
    public void requestArtistInfo(String input) throws RemoteException {
        String message = null;
        send(message);
        read();
    }

    @Override
    public void showAlbumInfo(String message) throws RemoteException {
    }

    @Override
    public void showArtistInfo(String message) throws RemoteException {

    }

    @Override
    public void logonUser() throws RemoteException {
        String message = null;
        send(message);
        read();
    }

    @Override
    public void logoffUser() throws RemoteException {
        String message = null;
        send(message);
        read();
    }


}
