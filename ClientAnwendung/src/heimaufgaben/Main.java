package heimaufgaben;


import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();

        System.out.println("geben Sie die IP-Adresse ein:");

        Scanner sc = new Scanner(System.in);
        String IP = sc.nextLine();

        if (Objects.equals(IP, "127.0.0.1") || Objects.equals(IP, "localhost")){
            //System.out.println("Eingabe ist richtig");
            System.out.println("geben Sie die Port Nummer ein:");
            String Port = sc.nextLine();
            if (Integer.parseInt(Port)==2022) {

                //System.out.println("Eingabe ist richtig\nSie können jetzt ihre Anfragen an den Server schicken..");
                if (client.socket == null) {
                    System.out.println("Fehler beim Verbindungsaufbau\nDer Server wurde nicht gestartet");
                }
                else {
                    System.out.println("Eine TCP-Verbindung zum Server wurde hergestellt.");
                    System.out.println("Sie können Ihre Anfragen an den Server stellen");
                    client.startClient();
                }
            }
            else {
                System.out.println("Kein korrekter Port!\nAktuell ist nur Port 2022 möglich");
            }
        }
        else {
            System.out.println("Falsche IP Adresse!\nAktuell ist nur die Eingabe localhost möglich");
        }
        System.out.println("Der Client wurde beendet.");
    }
}

