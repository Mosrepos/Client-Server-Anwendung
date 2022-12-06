package heimaufgaben;

import java.io.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    /*
    public static String scanPort() {
        String login_Port = "2022";
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Port");

        String Port = sc.nextLine();
        if (!Port.equals(login_Port)){
            return "KEIN KORREKTER PORT \n Aktuell ist nur der Port 2022 moeglich";
        }
        else {
            return "Eingabe ist richtig";
        }
    }

    public static String scanIP_Address() {
        String login_Address = "localhost";
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter IP Address");

        String IP = sc.nextLine();
        if (!IP.equals(login_Address)){
            return "Falsche IP Adresse \n Aktuell ist nur die IPv4-Adresse 127.0.0.1 und die Eingabe localhost moeglich";
        }
        else {
            return "Eingabe ist richtig";
        }
    }
*/

    public static String login_Port = "2022";
    public static String login_Address = "localhost";
    public static void scan(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Port");

        String Port = sc.nextLine();
        if (!Port.equals(login_Port)){
            System.out.println("KEIN KORREKTER PORT \n Aktuell ist nur der Port 2022 moeglich");
        }
        else {
            System.out.println("Eingabe ist richtig");
        }


        System.out.println("Enter IP Address");

        String IP = sc.nextLine();
        if (!IP.equals(login_Address)){
            System.out.println("Falsche IP Adresse \n Aktuell ist nur die IPv4-Adresse 127.0.0.1 und die Eingabe localhost moeglich");
        }
        else {
            System.out.println("Eingabe ist richtig");
        }
    }

     Socket socket = null;
     DataInputStream in = null;
     DataOutputStream out = null;
    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Client wurde gestartet: \n geben Sie die IP-Addresse ein:");

            Scanner sc = new Scanner(System.in);
            String IP = sc.nextLine();

            if (Objects.equals(IP, "127.0.0.1") || Objects.equals(IP, "localhost")){
                System.out.println("Eingabe ist richtig");
                System.out.println("geben Sie die Port Nummer ein:");
                String Port = sc.nextLine();
                if (Integer.parseInt(Port)==port){
                    System.out.println("Eingabe ist richtig");

                    in = new DataInputStream(System.in);
                    out = new DataOutputStream(socket.getOutputStream());

                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String clientNachricht = "";

                    while (!clientNachricht.equals("Fertig")) {
                        try {
                            clientNachricht = in.readLine();
                            out.writeUTF(clientNachricht);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    in.close();
                    out.close();
                    socket.close();
                }
                else {
                    System.out.println("Kein korrekter Port");
                }
            }
            else {
                System.out.println("Falsche IP Adresse");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
