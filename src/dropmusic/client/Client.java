package dropmusic.client;

import dropmusic.DropMusic;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class Client implements Remote {

    public Client() {
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            DropMusic server = (DropMusic) LocateRegistry.getRegistry(1234).lookup("dropmusic");
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
                case 1 : server.logonUser(); break;
                case 2 : client.searchMenu(scanner); break;
                case 3 : break;
                case 4 : break;
                case 5 : break;
                case 6 : server.logoffUser();
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void searchMenu(Scanner sc) {
        System.out.println("Please select an option: (insert the corresponding number and press [enter]");
        System.out.println("1 - Search song");
        System.out.println("2 - Search artist");
        System.out.println("3 - Search album");
        int option = sc.nextInt();
        switch (option) {
            case 1 : break;
            case 2 : break;
            case 3 : break;
        }
    }
}