package serveur;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
public class ServeurUDP {

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress(1234));
            System.out.println("Serveur UDP en écoute sur le port " + 1234 + "...");

            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // bloquant

                String message = new String(packet.getData(), 0, packet.getLength(),
                        StandardCharsets.UTF_8);
                System.out.println("Reçu de " + packet.getAddress().getHostAddress()
                        + ":" + packet.getPort() + " -> " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
