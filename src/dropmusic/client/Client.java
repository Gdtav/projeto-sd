package dropmusic.client;

import dropmusic.DropMusic;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * The type Client.
 */
public class Client implements Remote {

    /**
     * Instantiates a new Client.
     */
    public Client() {
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Client client = new Client();
        try {
            DropMusic server = (DropMusic) LocateRegistry.getRegistry(1234).lookup("dropmusic");
            while (true) {
                System.out.println("  ____                  __  __           _      \n" +
                        " |  _ \\ _ __ ___  _ __ |  \\/  |_   _ ___(_) ___ \n" +
                        " | | | | '__/ _ \\| '_ \\| |\\/| | | | / __| |/ __|\n" +
                        " | |_| | | | (_) | |_) | |  | | |_| \\__ \\ | (__ \n" +
                        " |____/|_|  \\___/| .__/|_|  |_|\\__,_|___/_|\\___|\n" +
                        "                 |_|                            ");
                System.out.println("Please select an option: (insert the corresponding number and press [enter]");
                System.out.println("1 - Register");
                System.out.println("2 - Search");
                System.out.println("3 - Transfer music");
                System.out.println("4 - Edit information");
                System.out.println("5 - Manage editors");
                System.out.println("6 - Exit");
                Scanner scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        server.logonUser();
                        break;
                    case 2:
                        client.searchMenu(scanner, server);
                        break;
                    case 3:
                        client.connectTCP();
                        break;
                    case 4:

                        break;
                    case 5:
                        break;
                    case 6:
                        server.logoffUser();
                        return;
                }
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void connectTCP() {
    }

    private void searchMenu(Scanner sc, DropMusic server) {
        System.out.println("Please select an option: (insert the corresponding number and press [enter]");
        System.out.println("1 - Search artist");
        System.out.println("2 - Search album");
        System.out.println("3 - Return");
        int option = sc.nextInt();
        String input;
        try {
            switch (option) {
                case 1:
                    System.out.println("Please insert search term: ");
                    input = sc.next();
                    server.requestArtistInfo(input);
                    break;
                case 2:
                    System.out.println("Please insert search term: ");
                    input = sc.next();
                    server.requestAlbumInfo(input);
                    break;
                case 3:
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}