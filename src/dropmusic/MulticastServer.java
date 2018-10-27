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
    private static MulticastListener listener = new MulticastListener(MULTICAST_ADDRESS, PORT);
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
    }

    private MulticastServer() {
        super();
    }

    public void run() {
        System.out.println(this.getName() + " running...");
        HashMap<String, String> response = new HashMap<>();
        while(true) {
            response = listener.getMessage("type"," ");
            if(response.isEmpty())
                continue;

            if (response.getOrDefault("type", "").equals("register")) {
                System.out.println("I here");
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
                reviewAlbum(response.getOrDefault("artist_name", " "), response.getOrDefault("album_name", " "), response.getOrDefault("username", " "), Integer.parseInt(response.getOrDefault("review", " ")), response.getOrDefault("review_desc", " "));
            }
            else if(response.getOrDefault("type", " ").equals("make_editor")) {
                makeEditor(response.getOrDefault("user", " "));
            }
        }
    }

    private void send(String message) {
        System.out.println("Sent out: " + message);
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
            /*Logger lgr = Logger.getLogger(MulticastServer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);*/
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
            String query = "SELECT name FROM artists WHERE (name like '%"+ art_name +"%') ORDER BY name ASC LIMIT 7";
            System.out.println("Query: " + query);
            
            ResultSet rs = st.executeQuery(query);
            String result = "type:artist_search_response;";
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
            
            ResultSet rs = st.executeQuery(query);
            String result = "type:artist_info_response;";
            int rows = countRows(rs);
            if(rows > 0) {
                rs.next();
                result += "status:found;name:" + art_name + ";activity_start:" + rs.getString(3) + ";activity_end:" + rs.getString(4) + ";description:" + rs.getString(5);

                query = "SELECT * FROM albums WHERE Artists_idArtists = '" + rs.getString(1) + "' ORDER BY release_date desc";
                rs = st.executeQuery(query);

                int album = 0;
                while(rs.next()) {
                    result += ";album_" + (album++) + ":" + rs.getString(2) + ";album_release_1:" + rs.getString(3);
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
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {
            String query = "SELECT idUserts FROM artists WHERE name = '" + art_name + "'";
            
            ResultSet rs = st.executeQuery(query);
            String result = "type:artist_album_response;";
            int rows = countRows(rs);
            if(rows > 0) {
                rs.next();
                query = "SELECT name FROM albums WHERE Artists_idArtists = '" + rs.getString(1) + "' ORDER BY release_date desc";
                rs = st.executeQuery(query);
                int rows2 = countRows(rs);
                int album = 0;
                if(rows2 > 0) {
                    result += "status:found";
                    while(rs.next()) {
                        result += ";name_" + (album++) + ":" + rs.getString(1);
                    }
                }
                else {
                    result += "status:not_found";
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

    public void albumSearch(String alb_name) {
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {            
            String result = "type:album_search_response;";
            String query = "SELECT name FROM albums WHERE name LIKE '%"+ alb_name +"%'  ORDER BY name asc";
            ResultSet rs = st.executeQuery(query);
            int rows = countRows(rs);
            int album = 0;
            if(rows > 0) {
                result += "status:found";
                while(rs.next()) {
                    result += ";name_" + (album++) + ":" + rs.getString(1);
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

    public void albumInfo(String art_name, String alb_name) {
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {            
            String result = "type:album_info_response;";
            String query = "SELECT name,release_date,rating,review FROM albums,ratings" + 
            " WHERE idAlbums = (SELECT idAlbums FROM albums WHERE name = '" + alb_name + "')" +
            " AND idAlbums = Albums_idAlbums ";

            System.out.println("Query1: " + query);
            ResultSet rs = st.executeQuery(query);
            int rows = countRows(rs);
            int review = 0;
            if(rows > 0) {
                rs.next();
                result += "status:found;artist_name:"+rs.getString(1)+";release_date:"+rs.getString(2);
                do{
                    result += ";review_score_" + (review) + ":" + rs.getString(3) + ";review_description_" + (review++) + ":" + rs.getString(4);
                } while(rs.next());

                query = "SELECT name FROM songs WHERE Albums_idAlbums = (SELECT idAlbums FROM albums WHERE name = '" + alb_name + "')";
                System.out.println("Query2: " + query);
                rs = st.executeQuery(query);
                int rows2 = countRows(rs);
                int songs = 0;
                if(rows2 > 0) {
                    while(rs.next()) {
                        result += ";song_" + (songs++) + ":" + rs.getString(1);
                    }
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

    public void reviewAlbum(String art_name, String alb_name, String user, int review, String desc) {
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {            
            String result = "type:album_review_response;";
            String query = "INSERT INTO ratings VALUES(NULL,'" + desc + "',"+ review +"," +
            "(SELECT idUsers FROM users WHERE username = '" + user + "'),"+
            "(SELECT idAlbums FROM albums WHERE name = '" + alb_name + "'),(SELECT Artists_idArtists FROM albums WHERE name = '" + alb_name + "'))";

            if(st.executeUpdate(query) == 1)
                result += "status:successful";
            else
                result += "status:unsuccessful";

            send(result); 
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MulticastServer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } 
    }

    public void makeEditor(String user) {
        String result = "type:make_editor_response;";
        try (Connection con = DriverManager.getConnection(url, sql_user, sql_password); Statement st = con.createStatement()) {
            String query = "UPDATE users SET editor = 1 WHERE username = '" + user + "'";

            if(st.executeUpdate(query) == 1)
                result += "status:success";
            else
                result += "status:insuccess";

            query = "INSERT INTO notifications VALUES(NULL,'You were granted Editor permissions while you were offline',(SELECT idUsers FROM users WHERE username = '" + user + "'))";
            System.out.println("WHYYYY: "+query);
            st.executeUpdate(query);
            send(result); 
            return;
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MulticastServer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }  
        send(result);
    }
    public int countRows(ResultSet rs) throws SQLException {
        int rows = 0;
        rs.last();
        rows = rs.getRow();
        rs.beforeFirst();
        return rows;
    }
}
