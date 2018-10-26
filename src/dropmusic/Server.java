package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Server extends UnicastRemoteObject implements DropMusic {

    private String MULTICAST_ADDRESS;
    private int PORT;
    private Semaphore semaphore;
    private MulticastListener listener;

    Server(String multicast_address, int port, Semaphore semaphore, MulticastListener listener) throws RemoteException {
        super();
        MULTICAST_ADDRESS = multicast_address;
        PORT = port;
        this.semaphore = semaphore;
        this.listener = listener;
    }

    public String getMULTICAST_ADDRESS() {
        return MULTICAST_ADDRESS;
    }

    public void setMULTICAST_ADDRESS(String MULTICAST_ADDRESS) {
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    private void send(String message) {
        try {
            MulticastSocket socket = new MulticastSocket();
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
        int i = 0;
        if (response.get("type").equals("artist_search_response") && response.get("status").equals("found"))
            while (response.getOrDefault("notification_" + i, null) != null) {

            }
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