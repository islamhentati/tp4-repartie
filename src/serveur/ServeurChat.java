package serveur;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServeurChat {

    public static void main(String[] args) {
        Set<SocketAddress> clients = new LinkedHashSet<>();

        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress(1234));
            System.out.println("Serveur de chat UDP en écoute sur le port " + 1234 + "...");

            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                SocketAddress expediteur = packet.getSocketAddress();
                String message = new String(packet.getData(), 0, packet.getLength(),
                        StandardCharsets.UTF_8);

                if (clients.add(expediteur)) {
                    System.out.println("Nouveau client : " + expediteur
                            + " (total : " + clients.size() + ")");
                }
                System.out.println("Reçu de " + expediteur + " -> " + message);

                byte[] data = message.getBytes(StandardCharsets.UTF_8);
                for (SocketAddress client : clients) {
                    if (!client.equals(expediteur)) {
                        socket.send(new DatagramPacket(data, data.length, client));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}