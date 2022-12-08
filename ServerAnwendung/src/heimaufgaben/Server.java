package heimaufgaben;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Scanner;

public class Server {
    Socket clientSocket;
    ServerSocket serverSocket;
    DataInputStream in = null;
    DataOutputStream out = null;
    int port = 2022;
    public Server() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void startServer(){
        try {
            System.out.println("Server wurde gestartet:\ngeben Sie die Port Nummer ein:");

            Scanner sc = new Scanner(System.in);
            String Port = sc.nextLine();

            if (Integer.parseInt(Port)!=port){
                System.out.println("KEIN KORREKTER PORT \nAktuell ist nur der Port 2022 möglich");
            }
            else {
                System.out.println("Eingabe ist richtig"); //wenn port richtig eingegeben ist
                clientSocket = serverSocket.accept();
                System.out.println("client gibt IP-Adresse und Port Nummer ein..");


                String serverNachricht,clientNachricht = "";
                LinkedList<String> verlauf = new LinkedList<>();

                // reads message from client until "Done" is sent
                while (!clientNachricht.equals("Fertig")) {
                    in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                    out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));



                    try {
                        clientNachricht = in.readUTF();

                        verlauf.add(clientNachricht);
                        switch (clientNachricht) {
                            case "PING" -> {
                                System.out.println("PONG");
                                serverNachricht = "PONG";
                                out.writeUTF(serverNachricht);
                                out.flush();
                            }

                            case "CURRENT TIME" -> {
                                String time = LocalTime.now().toString();
                                System.out.println("TIME " + time);
                                serverNachricht = time;
                                out.writeUTF(serverNachricht);
                                out.flush();
                            }


                            case "CURRENT DATE" -> {
                                String date = LocalDate.now().toString();
                                System.out.println("DATE " + date);
                                serverNachricht = date;
                                out.writeUTF(serverNachricht);
                                out.flush();
                            }


                            case "EXIT" -> {
                                serverNachricht = "Verbindung wird geschlossen!";
                                out.writeUTF(serverNachricht);
                                out.flush();
                            }

                            case "HISTORY" -> {
                                serverNachricht = "";
                                System.out.println("HISTORY");
                                if(verlauf.size() == 1){
                                    System.out.println("ERROR 404 NOT FOUND");
                                    serverNachricht = "404 NOT FOUND";
                                } else {
                                    for (String i : verlauf) {
                                        serverNachricht = serverNachricht + i + "\n";

                                    }
                                }
                                out.writeUTF(serverNachricht);
                                out.flush();
                            }
                            case "LATEST NEWS" -> {

                            }
                            default -> {
                                if (clientNachricht.startsWith("ECHO")) {
                                    System.out.println(clientNachricht);
                                    serverNachricht = clientNachricht.substring(5);
                                    out.writeUTF(serverNachricht);
                                    out.flush();
                                } else if (clientNachricht.startsWith("HISTORY")) {
                                    serverNachricht = "";
                                    System.out.println("HISTORY");
                                    if(verlauf.size() == 1){
                                        System.out.println("ERROR 404 NOT FOUND");
                                        serverNachricht = "404 NOT FOUND";
                                    } else {
                                        int numberOfRequests = Integer.parseInt(clientNachricht.substring(8));
                                        for (int i = verlauf.size() - numberOfRequests - 1; i < verlauf.size() - 1; i++) {
                                            serverNachricht = serverNachricht + verlauf.get(i) + "\n";
                                        }
                                    }
                                    out.writeUTF(serverNachricht);
                                    out.flush();
                                } else if (clientNachricht.startsWith("HOLIDAYS")) {
                                    int year = Integer.parseInt(clientNachricht.substring(9));

                                } else {
                                    System.out.println("ERROR 400 BAD REQUEST");
                                    serverNachricht = "400 BAD REQUEST";
                                    out.writeUTF(serverNachricht);
                                    out.flush();
                                }
                            }

                        }
                    } catch (IOException e) {
                        //throw new RuntimeException(e);
                        System.out.println("Kein Client verbunden");
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
