package dropmusic.client;

import dropmusic.DropMusic;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Client implements Remote {
    public static void main(String[] args) {
        try {
            DropMusic server = (DropMusic) LocateRegistry.getRegistry(1234).lookup("dropmusic");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}