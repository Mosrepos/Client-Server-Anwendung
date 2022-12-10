package heimaufgaben;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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

                // reads message from client until "EXIT" is sent
                while (!clientNachricht.equals("EXIT")) {
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
                                System.out.println("antwort ist zu ende");
                            }

                            case "CURRENT TIME" -> {
                                String time = LocalTime.now().toString();
                                System.out.println("TIME " + time);
                                serverNachricht = time;
                                out.writeUTF(serverNachricht);
                                out.flush();
                                System.out.println("antwort ist zu ende");
                            }


                            case "CURRENT DATE" -> {
                                String date = LocalDate.now().toString();
                                System.out.println("DATE " + date);
                                serverNachricht = date;
                                out.writeUTF(serverNachricht);
                                out.flush();
                                System.out.println("antwort ist zu ende");
                            }


                            case "EXIT" -> {
                                serverNachricht = "Verbindung wird geschlossen!";
                                out.writeUTF(serverNachricht);
                                out.flush();
                                System.out.println("antwort ist zu ende");
                            }

                            case "HISTORY" -> {
                                serverNachricht = "";
                                System.out.println("HISTORY");
                                if(verlauf.size() == 1){
                                    System.out.println("ERROR 404 NOT FOUND");
                                    serverNachricht = "404 NOT FOUND";
                                } else {

                                    for (int i = 0; i < verlauf.size() - 1; i++) {

                                        serverNachricht = serverNachricht + "\n" + verlauf.get(i);

                                    }
                                }
                                out.writeUTF(serverNachricht);
                                out.flush();
                                System.out.println("antwort ist zu ende");
                            }
                            case "LATEST NEWS" -> {

                                // TODO api letzte Nachrichten
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
                                            serverNachricht = serverNachricht + "\n" + verlauf.get(i);
                                        }
                                    }
                                    out.writeUTF(serverNachricht);
                                    out.flush();
                                    System.out.println("antwort ist zu ende");
                                } else if (clientNachricht.startsWith("HOLIDAYS")) {
                                    int year = Integer.parseInt(clientNachricht.substring(9));

                                    //TODO api Feiertage
                                } else if (clientNachricht.startsWith("GET")) {
                                    String[] url = clientNachricht.split(" ");
                                    if (url.length != 3) {
                                        System.out.println("ERROR 400 BAD REQUEST");
                                        serverNachricht = "400 BAD REQUEST";
                                        out.writeUTF(serverNachricht);
                                        out.flush();
                                    } else {
                                        String host = url[1];

                                        System.out.println(host);
                                        String request = url[2];

                                        System.out.println(request);
                                        Socket webSocket = new Socket(host, 80);
                                        System.out.println("socket connected");

                                        BufferedReader webIn = new BufferedReader(new InputStreamReader(webSocket.getInputStream(), StandardCharsets.UTF_8), 8192);
                                        BufferedWriter webOut = new BufferedWriter(new OutputStreamWriter(webSocket.getOutputStream(), StandardCharsets.UTF_8), 8192);


                                        webOut.write("GET " + request + " HTTP/1.1\n");
                                        //webOut.flush();
                                        webOut.write("Host: " + host + "\n");
                                        //webOut.flush();
                                        webOut.write("Connection: close\n");
                                        //webOut.flush();
                                        webOut.write("\n");
                                        webOut.flush();


                                        while ((serverNachricht = webIn.readLine()) != null) {

                                            System.out.println(serverNachricht);
                                            out.writeUTF(serverNachricht);
                                            out.flush();
                                        }
                                        System.out.println("antwort ist zu ende");
                                    }
                                } else {
                                    System.out.println("ERROR 400 BAD REQUEST");
                                    serverNachricht = "400 BAD REQUEST";
                                    out.writeUTF(serverNachricht);
                                    out.flush();
                                }
                            }

                        }
                    } catch (IOException e) {
                        System.out.println("Kein Client verbunden");
                    } catch (Exception e) {
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
