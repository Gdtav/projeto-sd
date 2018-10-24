package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * The type Multicast listener.
 */
public class MulticastListener extends Thread {
    private String MULTICAST_ADDRESS;
    private int PORT;
    private String message;

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    /**
     * Instantiates a new Multicast listener.
     *  @param MULTICAST_ADDRESS the multicast address
     * @param PORT              the port
     */
    public MulticastListener(String MULTICAST_ADDRESS, int PORT) {
        super();
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.PORT = PORT;
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
                setMessage(message);
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
