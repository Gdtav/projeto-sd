package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class MulticastListener extends Thread {
    private String MULTICAST_ADDRESS;
    private int PORT;
    private HashMap<String, String> message;
    private Semaphore semaphore;

    public MulticastListener(String MULTICAST_ADDRESS, int PORT, Semaphore semaphore) {
        super();
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.PORT = PORT;
        this.message = new HashMap<>();
        this.semaphore = semaphore;
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
                semaphore.acquire();
                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message = new String(packet.getData(), 0, packet.getLength());
                createMap(message);
                System.out.println(message);
                semaphore.release();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getMessage() {
        if(!message.isEmpty()) {
            HashMap<String, String> messageClone = new HashMap<String, String>(message);
            message.clear();
            return messageClone;
        }
        return message;
    }

    private void createMap(String buffer) {
        try {
            String[] pairs = buffer.split(";");
            for (String pair : pairs) {
                String[] splitted = pair.split(":");
                message.put(splitted[0], splitted[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
