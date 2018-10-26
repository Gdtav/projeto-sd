package dropmusic;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Scanner;

public class Client implements Remote {

    private boolean[] status;
    private DropMusic server;
    public Client() {
        this.status = new boolean[2];
    }

    public static void main(String[] args) {
        System.out.println("Please insert server's address:");
        String host = new Scanner(System.in).next();
        Client client = new Client();
        String username, password;
        int port = 5000;
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            client.setServer((DropMusic) registry.lookup("dropmusic"));
            Failover failover = new Failover(client.getServer(), host, port, client);
            failover.start();
            while (true) {
                System.out.println("  ____                  __  __           _      \n" +
                        " |  _ \\ _ __ ___  _ __ |  \\/  |_   _ ___(_) ___ \n" +
                        " | | | | '__/ _ \\| '_ \\| |\\/| | | | / __| |/ __|\n" +
                        " | |_| | | | (_) | |_) | |  | | |_| \\__ \\ | (__ \n" +
                        " |____/|_|  \\___/| .__/|_|  |_|\\__,_|___/_|\\___|\n" +
                        "                 |_|                            ");
                System.out.println("Please select an option: (insert the corresponding number and press [enter]");
                System.out.println("1 - Register");
                System.out.println("2 - Logon");
                System.out.println("3 - Exit");
                Scanner scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        System.out.println("Please insert desired username:");
                        username = scanner.next();
                        System.out.println("Please insert password:");
                        password = scanner.next();
                        System.out.println("Repeat password:");
                        while (!(scanner.next().equals(password))) {
                            System.out.println("Passwords don't match. Please re-type password:");
                            password = scanner.next();
                            System.out.println("Repeat password:");
                        }
                        client.server.register(username, password);
                        break;
                    case 2:
                        System.out.println("Insert username");
                        username = scanner.next();
                        System.out.println("Please insert password:");
                        password = scanner.next();
                        client.status = client.server.logonUser(username, password);
                        if (client.status[0]) {
                            client.mainMenu(client.status[1]);
                        } else {
                            System.out.println("invalid login. Please retry with a different username/password combination.");
                        }
                        break;
                    case 3:
                        return;
                }
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void mainMenu(boolean editor) {
        Scanner scanner = new Scanner(System.in);
        int option;
        if (editor) {
            System.out.println("Please select what you want to do:");
            System.out.println("1 - Search");
            System.out.println("2 - Transfer music");
            System.out.println("3 - Edit information");
            System.out.println("4 - Manage editors");
            System.out.println("5 - Exit");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    searchMenu(scanner, false);
                    break;
                case 2:
                    connectTCP();
                    break;
                case 3:
                    searchMenu(scanner, true);
                    break;
                case 4:
                    System.out.println("Please insert username to become editor:");
                    try {
                        if (server.makeEditor(scanner.next()))
                            System.out.println("Upgrade to editor succeeded.");
                        else
                            System.out.println("Failed to upgrade to editor.");
                        break;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                case 5:
                    break;
            }
        } else {
            System.out.println("Please select what you want to do:");
            System.out.println("1 - Search");
            System.out.println("2 - Transfer music");
            System.out.println("3 - Exit");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    searchMenu(scanner, false);
                    break;
                case 2:
                    connectTCP();
                    break;
                case 3:
                    break;
            }
        }

    }

    private void connectTCP() {

    }

    private void searchMenu(Scanner sc, boolean edition) {
        HashMap<Integer, String> result = new HashMap<>();
        int i;
        System.out.println("Please select an option: (insert the corresponding number and press [enter]");
        System.out.println("1 - Search artist");
        System.out.println("2 - Search album");
        System.out.println("3 - Return");
        int option = sc.nextInt();
        String input;
        switch (option) {
            case 1:
                System.out.println("Please insert search term: ");
                input = sc.next();
                i = 0;
                try {
                    for (String artist : server.artistSearch(input)) {
                        System.out.println(i + " - " + artist);
                        i++;
                        result.put(i, artist);
                    }
                    System.out.println("Insert desired artist number:");
                    option = sc.nextInt();
                    if (edition) {
                        server.editArtistInfo(result.get(option));
                    } else {
                        System.out.println(server.showArtistInfo(result.get(option)).toString());
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("Please insert search term: ");
                input = sc.next();
                i = 0;
                try {
                    for (String album : server.albumSearch(input)) {
                        System.out.println(i + " - " + album);
                        i++;
                        result.put(i, album);
                    }
                    System.out.println("Insert desired album number:");
                    option = sc.nextInt();
                    if (edition) {
                        server.editAlbumInfo(result.get(option));
                    } else {
                        System.out.println(server.showAlbumInfo(result.get(option)).toString());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                break;
        }
    }

    private DropMusic getServer() {
        return server;
    }

    void setServer(DropMusic server) {
        this.server = server;
    }
}