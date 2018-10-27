package dropmusic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class MulticastListener {
    private String MULTICAST_ADDRESS;
    private int PORT;

    MulticastListener(String MULTICAST_ADDRESS, int PORT) {
        super();
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.PORT = PORT;
    }

    HashMap<String, String> getMessage(String query, String expected) {
        HashMap<String, String> reply = new HashMap<>();
        try (MulticastSocket multicastSocket = new MulticastSocket(PORT)) {
            // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);
            long start = System.currentTimeMillis();
            while ((System.currentTimeMillis()-start)/1000F < 2.5) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message = new String(packet.getData(), 0, packet.getLength());
                reply = createMap(message);
                System.out.println(message);
               
                if(reply.get(query).equals(expected) || (reply.containsKey(query) && expected.equals(" "))) {
                    return reply;
                }
                else{
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private HashMap<String, String> createMap(String buffer) {
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
