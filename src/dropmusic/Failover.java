package dropmusic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * This class is a Thread running in the Client's background which provides failover support in case
 * the RMI Server goes down, rebinding to the secondary server's registry.
 */
public class Failover extends Thread {

    private DropMusic stub;
    private String host;
    private int port;
    private Client client;

    /**
     * Gets the Interface Implementation reference.
     *
     * @return the stub
     */
    DropMusic getStub() {
        return stub;
    }

    private void setStub(DropMusic stub) {
        this.stub = stub;
    }

    /**
     * Instantiates a new Failover thread.
     *
     * @param stub   the stub
     * @param host   the host
     * @param port   the port
     * @param client the client
     */
    Failover(DropMusic stub, String host, int port, Client client) {
        this.stub = stub;
        this.host = host;
        this.port = port;
        this.client = client;
    }

    public void run() {
        while (true) {
            try {
                stub.isAlive();
                Thread.sleep(1000);
            } catch (RemoteException | InterruptedException e) {
                try {
                    setStub((DropMusic) LocateRegistry.getRegistry(host, port).lookup("dropmusic"));
                    client.setServer(getStub());
                } catch (RemoteException | NotBoundException ignored) {
                }
            }
        }
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets client.
     *
     * @param client the client
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
