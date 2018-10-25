package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class MulticastListener extends Thread {
    private String MULTICAST_ADDRESS;
    private int PORT;
    private HashMap<String, String> message;


    public MulticastListener(String MULTICAST_ADDRESS, int PORT) {
        super();
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.PORT = PORT;
        this.message = new HashMap<>();
    }

    @Override
    public void run() {
        try (MulticastSocket multicastSocket = new MulticastSocket(PORT)) {
            // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message = new String(packet.getData(), 0, packet.getLength());
                createMap(message);
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getMessage() {
        return message;
    }

    private void createMap(String buffer) {
        try {
            String[] pairs = buffer.split(";");
            for (String pair : pairs) {
                String[] splitted = pair.split(":");
                System.out.println(splitted[0] + " // " + splitted[1]);
                message.put(splitted[0], splitted[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
