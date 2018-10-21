package dropmusic.server;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The main class for the RMI Server. this class contains the main function.
 */
public class RMIServer  {
    private boolean isMain;

    public RMIServer(boolean isMain) {
        this.isMain = isMain;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args){
        try {
            Registry registry = LocateRegistry.createRegistry(1234);
            Server server = new Server();
            registry.rebind("dropmusic",server);
            System.out.println("RMI Server online.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
