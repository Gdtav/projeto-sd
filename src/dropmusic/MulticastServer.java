package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer extends Thread {

    public static void main(String[] args) {
        MulticastServer server1 = new MulticastServer(1);
        MulticastServer server2 = new MulticastServer(2);
        MulticastServer server3 = new MulticastServer(3);
        server1.start();
        server2.start();
        server3.start();
    }

    private MulticastServer(int i) {
        super("Server " + i + ": " + (long) (Math.random() * 1000));
    }

    public void run() {

        System.out.println(this.getName() + " running...");
        int PORT = 4321;
        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            // create socket and bind it
            String MULTICAST_ADDRESS = "224.3.2.1";
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                System.out.println(this.getName() + " Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
