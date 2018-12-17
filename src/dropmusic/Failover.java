package dropmusic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * The class to address failure of RMI server
 */
public class Failover extends Thread {

    private DropMusic stub;
    private String host;
    private int port;
    private Client client;

    /**
     * Gets stub.
     *
     * @return the stub
     */
    DropMusic getStub() {
        return stub;
    }

    /**
     * Sets stub.
     *
     * @param stub the stub
     */
    void setStub(DropMusic stub) {
        this.stub = stub;
    }

    /**
     * Instantiates a new thread to verify if the server is alive. When it gets an exception, creates a new server.
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
