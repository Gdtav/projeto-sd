package dropmusic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Failover extends Thread {

    private DropMusic stub;
    private String host;
    private int port;
    private Client client;

    DropMusic getStub() {
        return stub;
    }

    private void setStub(DropMusic stub) {
        this.stub = stub;
    }

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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
