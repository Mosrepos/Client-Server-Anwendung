package heimaufgaben;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Server {
    Socket clientSocket;
    ServerSocket serverSocket;
    DataInputStream in = null;
    DataOutputStream out = null;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server wurde gestartet:\n geben Sie die Port Nummer ein:");

            Scanner sc = new Scanner(System.in);
            String Port = sc.nextLine();

            if (Integer.parseInt(Port)!=port){
                System.out.println("KEIN KORREKTER PORT \n Aktuell ist nur der Port 2022 möglich");
            }
            else {
                System.out.println("Eingabe ist richtig"); //wenn port richtig eingegeben ist
                clientSocket = serverSocket.accept();
                System.out.println("client ist verbunden...");

                in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String serverNachricht,clientNachricht = "";

                // reads message from client until "Done" is sent
                while (!clientNachricht.equals("Fertig")) {
                    try {
                        clientNachricht = in.readUTF();
                        //System.out.println("Anfrage vom Client:\n"+clientNachricht);
                        String time = LocalTime.now().toString();
                        String date = LocalDate.now().toString();
                        switch (clientNachricht) {
                            case "PING" -> {
                                System.out.println("PONG");
                                serverNachricht  = "PONG";
                            }
                            //case "ECHO": ;
                            case "CURRENT TIME" -> {
                                System.out.println("TIME " + time);
                                serverNachricht  = time;
                                //out.writeUTF(time);
                            }
                            case "CURRENT DATE" -> {
                                System.out.println("DATE " + date);
                                serverNachricht  = date;
                                //out.writeUTF(date);
                            }
                            //case "HISTORY" :;
                            default -> serverNachricht = "BAD REQUEST";
                        }
                        //serverNachricht = br.readLine();
                        out.writeUTF(serverNachricht);
                        out.flush();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Verbindung geschlossen");

                // close connection
                serverSocket.close();
                clientSocket.close();
                in.close();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
