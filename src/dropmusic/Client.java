/**
 * DROPMUSIC
 * RMI Client
 * This Client interfaces with the user through the command line (A Web Interface is to be implemented),
 * with navigation based through option insertion and input reading. It communicates with the RMI server located on the
 * address requested at the beggining of the execution, which requires that both RMI servers are running on the same host.
 * It gets a reference to the remote object's implementation of the interface and uses it to make the queries to the
 * Multicast Server.
 * It has a Failover thread running in the background that detects when the main server fails and quickly rebinds to the
 * secondary server's object, thus providing zero evidence of the error to the user.
 */

package dropmusic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The Client class which contains information about its own status.
 */
public class Client implements Remote {


    /**
     * The Username.
     */
    static String username;

    private boolean[] status;
    private DropMusic server;

    /**
     * Instantiates a new Client.
     */
    public Client() {
        this.status = new boolean[2];
    }

    /**
     * Main function for Client program. Here the RMI host's address is requested for remote connection
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println("Please insert server's address:");
        String host = new Scanner(System.in).next();
        Client client = new Client();
        String password;
        int port = 5000;

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            client.setServer((DropMusic) registry.lookup("dropmusic"));
            Failover failover = new Failover(client.getServer(), host, port, client);
            failover.start();
            System.out.println("  ____                  __  __           _      \n" +
                    " |  _ \\ _ __ ___  _ __ |  \\/  |_   _ ___(_) ___ \n" +
                    " | | | | '__/ _ \\| '_ \\| |\\/| | | | / __| |/ __|\n" +
                    " | |_| | | | (_) | |_) | |  | | |_| \\__ \\ | (__ \n" +
                    " |____/|_|  \\___/| .__/|_|  |_|\\__,_|___/_|\\___|\n" +
                    "                 |_|                            ");
            while (true) {
                System.out.println("Please select an option: (insert the corresponding number and press [enter]");
                System.out.println("1 - Register");
                System.out.println("2 - Logon");
                System.out.println("3 - Exit");
                int option = protectedInputInt();
                switch (option) {
                    case 1:
                        System.out.println("Please insert desired username:");
                        Client.username = protectedInputStr();
                        System.out.println("Please insert password:");
                        password = protectedInputStr();
                        System.out.println("Repeat password:");
                        while (!(protectedInputStr().equals(password))) {
                            System.out.println("Passwords don't match. Please re-type password:");
                            password = protectedInputStr();
                            System.out.println("Repeat password:");
                        }
                        client.server.register(username, password);
                        break;
                    case 2:
                        System.out.println("Insert username");
                        username = protectedInputStr();
                        System.out.println("Please insert password:");
                        password = protectedInputStr();
                        ArrayList<String> res = client.server.logonUser(username, password);
                        client.status[0] = res.get(0).equals("true");
                        client.status[1] = res.get(1).equals("true");
                        if (client.status[0]) {
                            if(res.size()>2)
                                System.out.println("\nYou Received the following notifications while you were offline:");
                            for (int i = 2; i < res.size(); i++) {
                                System.out.println(res.get(i));
                            }
                            client.mainMenu(client.status[1]);
                        } else {
                            System.out.println("invalid login. Please retry with a different username/password combination.");
                        }
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Enter one of the presented options");
                }
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The Main Menu method, which is called upon successful login and is different for editors and common users.
     * @param editor states if user has editor privileges.
     */

    private void mainMenu(boolean editor) {
        int option;
        while (true) {
            if (editor) {
                System.out.println("\nPlease select what you want to do:");
                System.out.println("1 - Search");
                System.out.println("2 - Transfer music");
                System.out.println("3 - Edit information");
                System.out.println("4 - Manage editors");
                System.out.println("5 - Exit");
                option = protectedInputInt();
                switch (option) {
                    case 1:
                        searchMenu( false);
                        break;
                    case 2:
                        connectTCP();
                        break;
                    case 3:
                        searchMenu(true);
                        break;
                    case 4:
                        System.out.println("Please insert username to become editor:");
                        try {
                            if (server.makeEditor(protectedInputStr()))
                                System.out.println("Upgrade to editor succeeded.");
                            else
                                System.out.println("Failed to upgrade to editor.");
                            break;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    case 5:
                        return;
                    default:
                        System.out.println("Enter one of the presented options");
                }
            } else {
                System.out.println("Please select what you want to do:");
                System.out.println("1 - Search");
                System.out.println("2 - Transfer music");
                System.out.println("3 - Exit");
                option = protectedInputInt();
                switch (option) {
                    case 1:
                        searchMenu( false);
                        break;
                    case 2:
                        connectTCP();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Enter one of the presented options");
                }
            }
        }

    }


    /**
     * Menu for TCP connection with the dataserver.
     */

    private void connectTCP() {
        System.out.println("Select an option:");
        System.out.println("1 - Upload file");
        System.out.println("2 - Download file");
        System.out.println("3 - Share file");
        System.out.println("4 - Return");
        int option = protectedInputInt();
        String song;
        switch (option) {
            case 1:
                System.out.println("Insert song name:");
                song = protectedInputStr();
                System.out.println("Insert filepath to upload:");
                String filepath = protectedInputStr();
                uploadFile(filepath, song);
                break;
            case 2:
                System.out.println("Insert song name to download:");
                song = protectedInputStr();
                downloadFile(song);
                break;
            case 3:
                // TODO
                break;
            case 4:
                break;
            default:
                System.out.println("Enter one of the presented options");
        }
    }


    /**
     * The Search Menu method, which is used for both search and editing of information.
     *
     * @param edition states if the menu has been called for editing info or not.
     */

    private void searchMenu( boolean edition) {
        HashMap<Integer, String> result = new HashMap<>();
        HashMap<String, String> edit = new HashMap<>();
        int i;
        System.out.println("Please select an option: (insert the corresponding number and press [enter]");
        System.out.println("1 - Search artist");
        System.out.println("2 - Search album");
        System.out.println("3 - Return");
        int option = protectedInputInt();
        String input;
        switch (option) {
            case 1:
                System.out.println("Please insert search term: ");
                input = protectedInputStr();
                i = 0;
                try {
                    for (String artist : server.artistSearch(input)) {
                        System.out.println(i + " - " + artist);
                        result.put(i, artist);
                        i++;
                    }
                    System.out.println("Insert desired artist number:");
                    option = protectedInputInt();
                    if (edition) {
                        System.out.println("new name:");
                        edit.put("name", protectedInputStr());
                        System.out.println("new start of activity:");
                        edit.put("activity_start", protectedInputStr());
                        System.out.println("new end of activity:");
                        edit.put("activity_end", protectedInputStr());
                        System.out.println("new description:");
                        edit.put("description", protectedInputStr());
                        System.out.println("How many albums:");
                        i = protectedInputInt();
                        for (int j = 0; j < i; j++) {
                            System.out.println("Album name:");
                            edit.put("album_" + j, protectedInputStr());
                            System.out.println("Album date:");
                            edit.put("album_release_" + j, protectedInputStr());
                        }
                        server.editArtistInfo(edit);
                    } else {
                        System.out.println(server.showArtistInfo(result.get(option)).toString());
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("Please insert search term: ");
                input = protectedInputStr();
                i = 0;
                try {
                    for (String album : server.albumSearch(input)) {
                        System.out.println(i + " - " + album);
                        result.put(i, album);
                        i++;
                    }
                    System.out.println("Insert desired album number:");
                    option = protectedInputInt();
                    if (edition) {
                        System.out.println("new artist name:");
                        edit.put("artist_name", protectedInputStr());
                        System.out.println("new album name:");
                        edit.put("album_name", protectedInputStr());
                        System.out.println("new album date");
                        edit.put("album_date", protectedInputStr());
                        for (int j = 0; j < i; j++) {
                            System.out.println("song name:");
                            edit.put("song_" + j, protectedInputStr());
                        }
                        server.editAlbumInfo(edit);
                    } else {
                        System.out.println(server.showAlbumInfo(result.get(option)).toString());
                        String album = result.get(option);
                        System.out.println("Do you wish to write a review? [y,N]");
                        input = protectedInputStr();
                        if (input.equals("y") || input.equals("Y")) {
                            System.out.println("Please give rating (0-5)");
                            int rate = protectedInputInt();
                            System.out.println("Please write review:");
                            input = protectedInputStr();
                            if (server.reviewAlbum(rate, input, album, username)) {
                                System.out.println("Review successfully published.");
                            } else {
                                System.out.println("Review failed.");
                            }
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                break;
            default:
                System.out.println("Enter one of the presented options");
        }
    }

    /**
     * Gets server.
     */
    private DropMusic getServer() {
        return server;
    }

    /**
     * Sets server.
     *
     * @param server the server
     */
    void setServer(DropMusic server) {
        this.server = server;
    }

    /**
     * Method for uploading files to dataserver
     * @implNote not implemented
     * @param filepath is local file path
     * @param song is song name
     */
    private void uploadFile(String filepath, String song) {
        /*try {
            Socket socket = new Socket(server.getIP(), 7001);
            FileOutputStream fos = (FileOutputStream) socket.getOutputStream();
            File file = new File(filepath);
            // TODO

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Method for uploading files to dataserver
     * @implNote not implemented
     * @param song is song name
     */

    private void downloadFile(String song) {
        // TODO
    }

    private static int protectedInputInt() {
        while (true) {
            try (Scanner scanner = new Scanner(System.in)) {
                int option;
                option = scanner.nextInt();
                return option;
            } catch (Exception e) {
                System.out.println("PLease insert a number!");
            }
        }
    }

    private static String protectedInputStr() {
        while (true) {
            try (Scanner scanner = new Scanner(System.in)) {
                String input;
                input = scanner.next();
                return input;
            } catch (Exception e) {
                System.out.println("PLease insert a valid input!");
            }
        }
    }
}