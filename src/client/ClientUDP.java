package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientUDP {
    private static final String HOTE = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner sc = new Scanner(System.in)) {

            InetAddress serveur = InetAddress.getByName(HOTE);

            System.out.print("Nom d'utilisateur : ");
            String nom = sc.nextLine().trim();
            System.out.println("Tapez vos messages (\"quit\" pour quitter).");

            while (true) {
                String texte = sc.nextLine();
                if (texte.equalsIgnoreCase("quit")) break;

                String message = "[" + nom + "] : " + texte;
                byte[] data = message.getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(data, data.length, serveur, PORT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
