package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

/**
 * The type Multicast listener. This class allows to get a message from the multicast address
 */
class MulticastListener {
    private String MULTICAST_ADDRESS;
    private int PORT;

    /**
     * Instantiates a new Multicast listener.
     *
     * @param MULTICAST_ADDRESS the multicast address
     * @param PORT              the port
     */
    MulticastListener(String MULTICAST_ADDRESS, int PORT) {
        super();
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.PORT = PORT;
    }

    /**
     * Gets message.
     *
     * @param query    the query
     * @param expected the expected answer type
     * @return the message
     */
    HashMap<String, String> getMessage(String query, String expected) {
        HashMap<String, String> reply = new HashMap<>();
        try (MulticastSocket multicastSocket = new MulticastSocket(PORT)) {
            // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);
            long start = System.currentTimeMillis();
            while (true) {
                System.out.println("-------------In the Listener Cycle -------------");
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message = new String(packet.getData(), 0, packet.getLength());
                reply = createMap(message);
                System.out.println(message);

                if(reply.get(query).equals(expected) || (reply.containsKey(query) && expected.equals(" "))) {
                    return reply;
                } else{
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    /**
     * Create hashmap from received message.
     *
     * @param buffer the buffer
     * @return the hash map
     */
    HashMap<String, String> createMap(String buffer) {
        try {
            HashMap<String, String> message = new HashMap<>();
            String[] pairs = buffer.split(";");
            for (String pair : pairs) {
                String[] splitted = pair.split(":");
                message.put(splitted[0], splitted[1]);
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
