package RMIServer;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

/**
 * The main class for the RMI Server. this class contains the main function.
 */
public class RMIServer extends Thread {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        User[] users = null;
        RMIServer client = new RMIServer();
        client.start();
    }

    public void run() {
        long counter = 0;
        try (MulticastSocket socket = new MulticastSocket()) {
            // create socket without binding it (only for sending)
            while (true) {
                String message = this.getName() + " packet " + counter++;
                byte[] buffer = message.getBytes();

                String MULTICAST_ADDRESS = "224.3.2.1";
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                int PORT = 4321;
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);

                try {
                    long SLEEP_TIME = 5000;
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
