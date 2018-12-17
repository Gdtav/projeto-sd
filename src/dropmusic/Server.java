package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The implementation class of the application's remote interface
 */
public class Server extends UnicastRemoteObject implements DropMusic {

    private String MULTICAST_ADDRESS;
    private int PORT;
    private MulticastListener listener;

    /**
     * Instantiates a new Server.
     *
     * @param multicast_address the multicast address
     * @param port              the port
     * @param listener          the listener
     * @throws RemoteException the remote exception
     */
    Server(String multicast_address, int port, MulticastListener listener) throws RemoteException {
        super();
        MULTICAST_ADDRESS = multicast_address;
        PORT = port;
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
    public boolean register(String username, String password) {
        send("type:register;user:" + username + ";password:" + password);
        HashMap<String, String> response;
        response = listener.getMessage("type","register_response");
        if (!response.isEmpty()) {
            if (response.get("status").equals("successful")) {
                System.out.println("Registration successful. You can login with your username and password.");
                return true;
            } else {
                System.out.println("Registration failed. Reason: " + response.get("reason"));
                return false;
            }
        }
        return false;
    }

    @Override
    public ArrayList<String> logonUser(String username, String password) {
        ArrayList<String> res = new ArrayList<>();
        send("type:login_request;user:" + username + ";password:" + password);
        HashMap<String, String> response;
        response = listener.getMessage("type","login_auth");
        int i = 0;
        if (!response.isEmpty()) {
            if (response.get("status").equals("granted")) {
                System.out.println("Login succeded");
                res.add("true");
                if(response.get("editor").equals("true"))
                    res.add("true");
                else
                    res.add("false");
                if (response.get("notifications").equals("true"))
                    while (response.getOrDefault("notification_" + i, null) != null) {
                        res.add(response.get("notification_" + i));
                        System.out.println(response.get("notification_" + i));
                        i++;
                    }

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
        HashMap<String, String> response = new HashMap<>();
        response = listener.getMessage("type","artist_search_response");
        int i = 0;
        if (!response.isEmpty()) {
            if(response.get("status").equals("found")) {
                while (response.getOrDefault("name_" + i, null) != null) {
                    query.add(response.get("name_" + i));
                    i++;
                }
            }
        }
        return query;
    }

    @Override
    public ArrayList<String> albumSearch(String input) {
        ArrayList<String> query = new ArrayList<>();
        send("type:album_search;name:" + input);
        HashMap<String, String> response = listener.getMessage("type", "album_search_response");
        int i = 0;
        if (!response.isEmpty()) {
            if (response.get("status").equals("found")) {
                while (response.getOrDefault("name_" + i, null) != null) {
                    query.add(response.get("name_" + i));
                    i++;
                }
            }
        }
        return query;
    }

    @Override
    public ArrayList<String> albumFromArtistSearch(String input) {
        ArrayList<String> query = new ArrayList<>();
        send("type:album_search_artist;name:" + input);
        HashMap<String, String> response = listener.getMessage("type","album_search_response");
        int i = 0;
        if (!response.isEmpty()) {
            if (response.get("status").equals("found")) {
                while (response.getOrDefault("name_" + i, null) != null) {
                    query.add(response.get("name_" + i));
                    i++;
                }
            }
        }
        return query;
    }

    @Override
    public boolean makeEditor(String input) {
        send("type:make_editor;user:" + input);
        HashMap<String, String> response = listener.getMessage("type","make_editor_response");
        if (!response.isEmpty())
            return response.get("status").equals("success");
        else
            return false;
    }

    @Override
    public boolean reviewAlbum(int grade, String review, String album, String username) {
        HashMap<String, String> response;
        send("type:album_review;username:" + username + ";album_name:" + album + ";review:" + grade + ";review_desc:" + review);
        if ((response = listener.getMessage("type", "album_review_response")) != null) {
            return response.get("status").equals("successful");
        }
        return false;
    }


    @Override
    public void isAlive() {

    }

    @Override
    public void editArtistInfo(HashMap<String, String> input) {
        StringBuilder query = new StringBuilder("type:artist_edit;name:" + input.get("name") + ";activity_start:" + input.get("activity_start") +
                ";activity_end:" + input.get("activity_end") + ";description:" + input.get("description") + ";");
        for (int i = 0; input.getOrDefault("album_" + i, null) != null; i++) {
            query.append("album_").append(i).append(":").append(input.get("album_" + i));
            query.append("album_release_").append(i).append(":").append(input.get("album_release_" + i));
        }
        System.out.println(query);
        send(query.toString());
    }

    @Override
    public HashMap<String, String> showArtistInfo(String input) {
        send("type:artist_info;name:" + input);
        return listener.getMessage("type", "artist_info_response");
    }

    @Override
    public void editAlbumInfo(HashMap<String, String> input) {
        StringBuilder query = new StringBuilder("type:album_edit;name:" + input.get("name") + ";artist_name:" + input.get("artist_name") +
                ";album_name:" + input.get("album_name") + ";album_date:" + input.get("album_date"));
        for (int i = 0; input.getOrDefault("album_" + i, null) != null; i++) {
            query.append(";song_").append(i).append(":").append(input.get("song_" + i));
        }
        System.out.println(query);
        send(query.toString());
    }

    @Override
    public HashMap<String, String> showAlbumInfo(String input) {
        send("type:album_info;album_name:" + input);
        return listener.getMessage("type","album_info_response");
    }

    @Override
    public boolean addArtist(String name, String date1, String date2, String desc) throws RemoteException {
        HashMap<String, String> response;
        send("type:artist_add;art_name:" + name + ";act_start:" + date1 + ";act_end:" + date2 + ";art_desc:" + desc);
        if ((response = listener.getMessage("type", "artist_add_response")) != null) {
            return response.get("status").equals("successful");
        }
        return false;
    }

    @Override
    public boolean addAlbum(String name, String date, String artist) throws RemoteException {
        HashMap<String, String> response;
        send("type:album_add;alb_name:" + name + ";release_date:" + date + ";artist:" + artist);
        if ((response = listener.getMessage("type", "album_add_response")) != null) {
            return response.get("status").equals("successful");
        }
        return false;
    }

    @Override
    public boolean addSong(String name, String lyrics, String artist, String album) throws RemoteException {
        HashMap<String, String> response;
        send("type:song_add;song_name:" + name + ";lyrics:" + lyrics + ";artist:" + artist + ";album:" + album);
        if ((response = listener.getMessage("type", "song_add_response")) != null) {
            return response.get("status").equals("successful");
        }
        return false;
    }

    @Override
    public boolean cleanArtists() throws RemoteException {
        HashMap<String, String> response;
        send("type:clean_artists");
        if ((response = listener.getMessage("type", "clean_artists_response")) != null) {
            return response.get("status").equals("successful");
        }
        return false;
    }

}