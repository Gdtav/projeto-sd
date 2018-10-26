package dropmusic;

import dropmusic.MulticastListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

public class MulticastServer extends Thread {

    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;
    private static MulticastSocket socket;
    private static Semaphore semaphore = new Semaphore(1);
    private static MulticastListener listener = new MulticastListener(MULTICAST_ADDRESS, PORT,semaphore);
    String url = "jdbc:mysql://localhost:3306/dropmusic?autoReconnect=true&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&serverTimezone=GMT&useSSL=false";
    String sql_user = "pmsilva";
    String sql_password = "password";

    static {
        try {
            socket = new MulticastSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        System.out.println("Multicast Server Online");
        listener.start();
        System.out.println("MulticastListener started");
    }

    private MulticastServer() {
        super();
    }

    public void run() {
        System.out.println(this.getName() + " running...");
        HashMap<String, String> response;
        while(true) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = listener.getMessage();
            semaphore.release();

            if (response.getOrDefault("type", "").equals("register")) {
                register(response.getOrDefault("user", " "),response.getOrDefault("password", " "));
            }
            else if(response.getOrDefault("type", " ").equals("login_request")) {
                logonUser(response.getOrDefault("user", " "),response.getOrDefault("password", " "));
            }
            else if(response.getOrDefault("type", " ").equals("artist_search")) {
                artistSearch(response.getOrDefault("name", " "));
            }
            else if(response.getOrDefault("type", " ").equals("artist_info")) {
                artistInfo(response.getOrDefault("name", " "));
            }
            else if(response.getOrDefault("type", " ").equals("album_search_artist")) {
                albumFromArtistSearch(response.getOrDefault("name", " "));
            }
            else if(response.getOrDefault("type", " ").equals("album_search")) {
                albumSearch(response.getOrDefault("name", " "));
            }
            else if(response.getOrDefault("type", " ").equals("album_info")) {
                albumInfo(response.getOrDefault("artist_name", " "), response.getOrDefault("album_name", " "));
            }
            else if(response.getOrDefault("type", " ").equals("album_review")) {
                reviewAlbum(response.getOrDefault("artist_name", " "), response.getOrDefault("album_name", " "), Integer.parseInt(response.getOrDefault("review", " ")), response.getOrDefault("review_desc", " "));
            }
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

    public void register(String username, String password) {
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {
            String query = "select count(idUsers) from users";
            
            ResultSet rs = st.executeQuery(query);
            int first = 0;
            if(rs.next() && rs.getInt(1) == 0) 
                first = 1;
            
            query = "INSERT INTO `Users`(idUsers,username,password,editor) VALUES (NULL, '" + username + "', SHA2('" + password + "',256), " + first + ")";
            st.executeUpdate(query);

            send("type:register_response;status:successful"); 
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MulticastServer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void logonUser(String username, String password) {
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {
            String query = "SELECT idUsers, editor FROM users WHERE username = '" + username + "' AND password = SHA2('" + password + "',256)";
            String response = "type:login_auth;status:granted;";
            ResultSet rs = st.executeQuery(query);

            int rows = countRows(rs);
            
            if(rows != 0) {
                rs.next();
                int idUsers = rs.getInt(1);
                String editor = rs.getString(2);
                response += "editor:" + "1".equals(editor);
                query = "SELECT notification FROM notifications WHERE Users_idUsers = " + idUsers ;
                rs = st.executeQuery(query);
                int rows2 = countRows(rs);
                if(rows2 == 0) {
                    response += ";notifications:false";
                }
                else if(rows > 0) {
                    response += ";notifications:true";
                    int not = 0;
                    while(rs.next()) {
                        response += ";notification_" + (not++) + ":"+ rs.getString(1);
                    }
                    query = "DELETE FROM notifications WHERE Users_idUsers = " + idUsers;
                    st.executeUpdate(query);
                }
                send(response); 
            }
            else {
                send("type:login_auth;status:failed");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MulticastServer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void artistSearch(String art_name) {
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {
            String[]split_string = art_name.split(" ");

            String query = "SELECT name FROM artists WHERE (name like '%"+ split_string[0] +"%' LIMIT 7)";
            for(int i=1;i<split_string.length;i++) {
                query += " OR (name like '%" + split_string[i] + "%'";
            }
            query += " ORDER BY name ASC";
            System.out.println("Query: " + query);
            
            ResultSet rs = st.executeQuery(query);
            String result = "type:artist_search_reponse;";
            int rows = countRows(rs);
            if(rows > 0) {
                result += "status:found";
                int name = 0;
                while(rs.next()) {
                    result += ";name_" + (name++) + ":" + rs.getString(1);
                }
            }
            else {
                result += "status:not_found";
            }
                send(result); 
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MulticastServer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    public void artistInfo(String art_name) {
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {

            String query = "SELECT * FROM artists WHERE name = '" + art_name + "'";
            System.out.println("Query: " + query);
            
            ResultSet rs = st.executeQuery(query);
            String result = "type:artist_info_response;";
            int rows = countRows(rs);
            if(rows > 0) {
                rs.next();
                result += "status:found;name:" + art_name + "activity_start:" + rs.getString(3) + "activity_end:" + rs.getString(4) + "description:" + rs.getString(5);

                query = "SELECT * FROM albums WHERE Artists_idArtists = '" + rs.getString(1) + "' ORDER BY release_date desc";
                rs = st.executeQuery(query);

                int album = 0;
                while(rs.next()) {
                    result += ";album_" + (album++) + ":" + rs.getString(2) + "album_release_1:" + rs.getString(3);
                }
            }
            else {
                result += "status:not_found";
            }
                send(result); 
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MulticastServer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }        
    }
    
    public void albumFromArtistSearch(String art_name) {

    }

    public void albumSearch(String alb_name) {

    }    

    public void albumInfo(String art_name, String alb_name) {

    }

    public void reviewAlbum(String art_name, String alb_name, int review, String desc) {

    }

    public int countRows(ResultSet rs) throws SQLException {
        int rows = 0;
        rs.last();
        rows = rs.getRow();
        rs.beforeFirst();
        return rows;
    }
}
