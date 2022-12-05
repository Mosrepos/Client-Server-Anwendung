package heimaufgaben;

public class Main {
    public static void main(String[] args) {

        String recieved_Port = Server.scanPort();
        Server server = new Server();
        System.out.println("Der Server wurde beendet.");
    }
}
