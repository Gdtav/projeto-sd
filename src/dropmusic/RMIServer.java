package dropmusic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * DROPMUSIC
 * RMI Server
 * This class runs a Registry located on the port 5000 and binds an implementation of
 * DropMusic interface (Server class) to "dropmusic" on the registry, if it fids itself as the main server.
 * Otherwise, it will lookup the already binded object on the RMI registry and call the dummy method isAlive()
 * until it receives an exception. When it happens, the server rebinds the name "dropmusic" to its own server object
 * and becomes the main server.
 */
public class RMIServer {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        String MULTICAST_ADDRESS = "224.0.224.0";
        int PORT = 4321;
        MulticastListener listener = new MulticastListener(MULTICAST_ADDRESS, PORT);
        try {
            Registry registry = LocateRegistry.getRegistry(5000);
            DropMusic server = (DropMusic) registry.lookup("dropmusic");
            System.out.println("Secondary Server alive");
            while (true) {
                server.isAlive();
                Thread.sleep(5000);
            }
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            try {
                DropMusic server = new Server(MULTICAST_ADDRESS, PORT, listener);
                Registry registry = LocateRegistry.createRegistry(5000);
                registry.rebind("dropmusic", server);
                System.out.println("Main server alive");
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }
}
