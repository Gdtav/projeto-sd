package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
    public boolean[] logonUser(String username, String password) {
        boolean[] res = new boolean[2];
        send("type:login_request;user:" + username + ";password:" + password);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> response = listener.getMessage();
        semaphore.release();
        int i = 0;
        if (response.get("type").equals("login_auth")) {
            if (response.get("status").equals("granted")) {
                System.out.println("Login succeded");
                if (response.get("notifications").equals("true"))
                    while (response.getOrDefault("notification_" + i, null) != null) {
                        System.out.println(response.get("notification_" + i));
                        i++;
                    }
                res[0] = true;
                res[1] = response.get("editor").equals("true");
                return res;
            } else {
                System.out.println("Login failed. Please try again.");
            }
        }
        return res;
    }

    @Override
    public ArrayList<String> artistSearch(String input) {
        ArrayList<String> query = new ArrayList<>();
        send("type:artist_search;name:" + input);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> response = listener.getMessage();
        semaphore.release();
        int i = 0;
        if (response.get("type").equals("artist_search_response") && response.get("status").equals("found")) {
            while (response.getOrDefault("name_" + i, null) != null) {
                query.add(response.get("name_" + i));
                i++;
            }
        }
        return query;
    }

    @Override
    public ArrayList<String> albumSearch(String input) {
        ArrayList<String> query = new ArrayList<>();
        send("type:album_search;name:" + input);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> response = listener.getMessage();
        semaphore.release();
        int i = 0;
        if (response.get("type").equals("album_search_response") && response.get("status").equals("found")) {
            while (response.getOrDefault("name_" + i, null) != null) {
                query.add(response.get("name_" + i));
                i++;
            }
        }
        return query;
    }

    @Override
    public ArrayList<String> albumFromArtistSearch(String input) {
        ArrayList<String> query = new ArrayList<>();
        send("type:album_search_artist;name:" + input);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> response = listener.getMessage();
        semaphore.release();
        int i = 0;
        if (response.get("type").equals("album_search_response") && response.get("status").equals("found")) {
            while (response.getOrDefault("name_" + i, null) != null) {
                query.add(response.get("name_" + i));
                i++;
            }
        }
        return query;
    }

    @Override
    public ArrayList<String> artistInfo(String input) {
        ArrayList<String> query = new ArrayList<>();
        send("type:artist_info;name:" + input);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> response = listener.getMessage();
        semaphore.release();
        if (response.get("type").equals("artist_info_response") && response.get("status").equals("found")) {
            query.addAll(response.values());
            query.remove("artist_info_response");
            query.remove("found");
        } else if (response.get("type").equals("album_info_response") && response.get("status").equals("not_found")) {
            query.add("failed to retrieve artist info");
        }
        return query;
    }

    @Override
    public ArrayList<String> albumInfo(String input) {
        ArrayList<String> query = new ArrayList<>();
        send("type:album_info;name:" + input);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> response = listener.getMessage();
        semaphore.release();
        if (response.get("type").equals("album_info_response") && response.get("status").equals("found")) {
            query.addAll(response.values());
            query.remove("album_info_response");
            query.remove("found");
        } else if (response.get("type").equals("album_info_response") && response.get("status").equals("not_found")) {
            query.add("failed to retrieve album info");
        }
        return query;
    }

    @Override
    public boolean makeEditor(String input) {
        send("type:make_editor;user:" + input);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> response = listener.getMessage();
        semaphore.release();
        if (response.get("type").equals("make_editor_response"))
            return response.get("status").equals("success");
        else
            return false;
    }

    @Override
    public void reviewAlbum(String review) {

    }

    @Override
    public void isAlive() {

    }
}