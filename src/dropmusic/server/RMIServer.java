package dropmusic.server;

import dropmusic.server.data.*;
import java.rmi.RemoteException;

/**
 * The main class for the RMI Server. this class contains the main function.
 */
public class RMIServer  {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args){
        try {
            Server server = new Server();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        User[] users = null;

    }

}
