package heimaufgaben;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
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
