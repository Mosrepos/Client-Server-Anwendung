package heimaufgaben;



public class Main {
    public static void main(String[] args) {

       String login_Address = "localhost";
       String login_Port = "2022";

       String IP_Address = Client.scanIP_Address();
       String Port = Client.scanPort();

        Client client = new Client();
        System.out.println("Der Client wurde beendet.");
    }
}
