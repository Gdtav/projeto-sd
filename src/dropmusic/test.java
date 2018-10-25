import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class test {

    public static void main(String[] args) {
        //Host ip is obtained using docker-machine ip
        //Socket is the one opened for interface with docker
        String url = "jdbc:mysql://localhost:3306/dropmusic?autoReconnect=true&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&serverTimezone=GMT&useSSL=false";
        String user = "pmsilva";
        String password = "password";
        
        String query = "select * from users;";

        try (Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            int rows = 0;
            rs.last();
            rows = rs.getRow();
            rs.beforeFirst();

            while (rs.next()) {
                for(int i=1;i<=rows;i++)
                    System.out.print(rs.getString(i)+"\t");
                System.out.println();
            }

        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(test.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } 
    }
}