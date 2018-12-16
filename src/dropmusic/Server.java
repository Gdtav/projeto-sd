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

/**
 * The implementation of interface DropMusic on the server.
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

    /**
     * Implementation of the interface's method.
     * sends the passed string through a MulticastSocket
     */

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


    /**
     * Implementation of the interface's method.
     *
     * @param password receives user password
     * @param username receives username
     */

    @Override
    public void register(String username, String password) {
        send("type:register;user:" + username + ";password:" + password);
        HashMap<String, String> response;
        response = listener.getMessage("type","register_response");
        if (!response.isEmpty()) {
            if (response.get("status").equals("successful")) {
                System.out.println("Registration successful. You can login with your username and password.");
            } else {
                System.out.println("Registration failed. Reason: " + response.get("reason"));
            }
        }
    }

    /**
     * Implementation of the interface's method.
     * @param password inserted password
     * @param username inserted username
     */

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

    /**
     * Implementation of the interface's method.
     * @param input user search term
     */

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

    /**
     * Implementation of the interface's method.
     * @param input user search term
     */

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

    /**
     * Implementation of the interface's method.
     * @param input user search term
     */

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

    /**
     * Implementation of the interface's method.
     * @param input user to become editor
     */

    @Override
    public boolean makeEditor(String input) {
        send("type:make_editor;user:" + input);
        HashMap<String, String> response = listener.getMessage("type","make_editor_response");
        if (!response.isEmpty())
            return response.get("status").equals("success");
        else
            return false;
    }

    /**
     * Implementaion of the interface's method
     * @param grade    the grade
     * @param review   the review
     * @param album    the album
     * @param username the username
     * @return the success of the operation.
     */

    @Override
    public boolean reviewAlbum(int grade, String review, String album, String username) {
        HashMap<String, String> response;
        send("type:album_review;username:" + username + ";album_name:" + album + ";review:" + grade + ";review_desc:" + review);
        if ((response = listener.getMessage("type", "album_review_response")) != null) {
            return response.get("status").equals("successful");
        }
        return false;
    }


    /**
     * Dummy method. If the server fails it throws an exception and the secondary server binds its registry.
     */
    @Override
    public void isAlive() {

    }

    /**
     * Implementaion of the interface's method
     * @param input the input
     */
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

    /**
     * Implementaion of the interface's method
     * @param input the input
     * @return an array with the Artis's informations.
     */
    @Override
    public HashMap<String, String> showArtistInfo(String input) {
        send("type:artist_info;name:" + input);
        return listener.getMessage("type", "artist_info_response");
    }

    /**
     * Implementaion of the interface's method
     * @param input the input
     */
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

    /**
     * Implementaion of the interface's method
     * @param input the input
     * @return an array with the Album's informations.
     */

    @Override
    public HashMap<String, String> showAlbumInfo(String input) {
        send("type:album_info;album_name:" + input);
        return listener.getMessage("type","album_info_response");
    }

    @Override
    public String addArtist(HashMap<String, String> input) throws RemoteException {
        StringBuilder message = new StringBuilder();
        message.append("type:artist_add;");
        for (String string: input.keySet()) {
            message.append(string).append(":").append(input.get(string)).append(";");
        }
        send(message.toString());
        return listener.getMessage("type","artist_add_response").get("status");
    }

    @Override
    public String addAlbum(HashMap<String, String> input) throws RemoteException {
        StringBuilder message = new StringBuilder();
        message.append("type:album_add;");
        for (String string: input.keySet()) {
            message.append(string).append(":").append(input.get(string)).append(";");
        }
        send(message.toString());
        return listener.getMessage("type","album_add_response").get("status");
    }

}