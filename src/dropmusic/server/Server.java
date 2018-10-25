package dropmusic.server;

import dropmusic.DropMusic;
import dropmusic.MulticastListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Server extends UnicastRemoteObject implements DropMusic {

    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;
    private static MulticastSocket socket;
    private static Semaphore semaphore = new Semaphore(1);
    private static MulticastListener listener = new MulticastListener(MULTICAST_ADDRESS, PORT, semaphore);



    static {
        try {
            socket = new MulticastSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(7000);
            registry.bind("dropmusic", server);
            listener.start();
        } catch (RemoteException | AlreadyBoundException e) {
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

    @Override
    public void register(String username, String password) {
        send("type:register;user:" + username + ";password:" + password);
        HashMap<String, String> response;
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response = listener.getMessage();
        semaphore.release();
        if (response.getOrDefault("type", "").equals("register_response")) {
            if (response.get("status").equals("successful")) {
                System.out.println("Registration successful. You can login with your username and password.");
            } else {
                System.out.println("Registration failed. Reason: " + response.get("reason"));
            }
        }
    }


    @Override
    public boolean logonUser(String username, String password) {
        send("type:login_request;user:" + username + ";password:" + password);
        HashMap<String, String> response = listener.getMessage();
        int i = 0;
        if (response.get("type").equals("login_auth")) {
            if (response.get("status").equals("granted")) {
                System.out.println("Login succeded");
                if (response.get("notifications").equals("true"))
                    while (response.getOrDefault("notification_" + i, null) != null) {
                        System.out.println(response.get("notification_" + i));
                        i++;
                    }
                return response.get("editor").equals("true");
            } else {
                System.out.println("Login failed. Please try again.");
            }
        }
        return false;
    }

    @Override
    public void artistSearch(String input) {
        send("type:artist_search;name:" + input);
        HashMap<String, String> response = listener.getMessage();
    }

    @Override
    public void albumSearch(String input) {

    }

    @Override
    public void albumFromArtistSearch() {

    }

    @Override
    public void artistInfo() {

    }

    @Override
    public void albumInfo() {

    }

    @Override
    public void reviewAlbum(String review) {

    }

    @Override
    public void isAlive() throws RemoteException {

    }
}