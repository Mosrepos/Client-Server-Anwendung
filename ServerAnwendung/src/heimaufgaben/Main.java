package heimaufgaben;

import java.net.*;
public class Main {
    public static void main(String[] args) {

        Server server = new Server();
        server.startServer();
        System.out.println("Der Server wurde beendet.");
    }
}
