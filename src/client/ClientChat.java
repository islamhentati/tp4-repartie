package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientChat {

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serveur = InetAddress.getByName("localhost");
        Scanner sc = new Scanner(System.in);

        System.out.print("Nom d'utilisateur : ");
        String nom = sc.nextLine().trim();

        // Thread de réception (en arrière-plan)
        Thread reception = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try {
                while (!socket.isClosed()) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    System.out.println(new String(packet.getData(), 0, packet.getLength(),
                            StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                // socket fermé : fin normale du thread
            }
        });
        reception.setDaemon(true);
        reception.start();

        // Premier message : permet au serveur d'enregistrer ce client
        envoyer(socket, serveur, "*** " + nom + " a rejoint le chat ***");
        System.out.println("Tapez vos messages (\"quit\" pour quitter).");

        // Thread principal : envoi
        while (true) {
            String texte = sc.nextLine();
            if (texte.equalsIgnoreCase("quit")) {
                envoyer(socket, serveur, "*** " + nom + " a quitté le chat ***");
                break;
            }
            envoyer(socket, serveur, "[" + nom + "] : " + texte);
        }
        socket.close();
        sc.close();
    }

    private static void envoyer(DatagramSocket socket, InetAddress serveur, String msg)
            throws Exception {
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(data, data.length, serveur, 1234));
    }
}