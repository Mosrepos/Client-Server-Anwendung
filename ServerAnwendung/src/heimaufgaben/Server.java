package heimaufgaben;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        } catch (IOException e) {
            System.out.println("Es gibt ein Problem mit dem ServerSocket");
        }
    }

    public void startServer() {
        try {
            System.out.println("Server wurde gestartet:\ngeben Sie die Port Nummer ein:");

            Scanner sc = new Scanner(System.in);
            String Port = sc.nextLine();

            if (Integer.parseInt(Port) != port) {
                System.out.println("KEIN KORREKTER PORT \nAktuell ist nur der Port 2022 möglich");
            } else {
                System.out.println("Der Server wartet am Port 2022 auf anfragen vom Client."); //wenn port richtig eingegeben ist
                clientSocket = serverSocket.accept();
                System.out.println("client gibt IP-Adresse und Port Nummer ein..");


                String serverNachricht, clientNachricht = "";
                LinkedList<String> verlauf = new LinkedList<>();

                // reads message from client until "EXIT" is sent
                // TODO alle Kommentare auf Deutsch
                while (!(clientNachricht.equals("EXIT"))) {


                    try {
                        in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                        out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

                        clientNachricht = in.readUTF();

                        verlauf.add(clientNachricht);

                        switch (clientNachricht) {
                            case "PING" -> {
                                System.out.println("PONG");
                                sendMessageToClient("PONG", out);
                            }

                            case "CURRENT TIME" -> {
                                DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
                                LocalTime localTime = LocalTime.now();
                                System.out.println("TIME " + time.format(localTime));
                                sendMessageToClient(time.format(localTime), out);
                            }


                            case "CURRENT DATE" -> {
                                DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                LocalDate localDate = LocalDate.now();
                                System.out.println("DATE " + localDate.format(date));
                                sendMessageToClient(localDate.format(date), out);
                            }


                            case "EXIT" -> {
                                sendMessageToClient("Verbindung wird geschlossen", out);
                                sendMessageToClient("ende", out);
                            }

                            case "HISTORY" -> {
                                System.out.println("HISTORY");
                                if (verlauf.size() == 1) {
                                    sendMessageToClient(printException(404), out);
                                } else {
                                    serverNachricht = "";
                                    for (int i = 0; i < verlauf.size() - 1; i++) {
                                        serverNachricht = serverNachricht + "\n" + verlauf.get(i);
                                    }
                                    sendMessageToClient(serverNachricht, out);
                                }
                            }
                            case "LATEST NEWS" -> {
                                String[] url = {"GET", "tagesschau.de", "/api2/"};
                                connectToWebUsingAPI(url, out);
                            }
                            default -> {

                                if (clientNachricht.startsWith("ECHO")) {
                                    System.out.println(clientNachricht);
                                    serverNachricht = clientNachricht.substring(5);
                                    sendMessageToClient(serverNachricht, out);
                                } else if (clientNachricht.startsWith("HISTORY")) {
                                    serverNachricht = "";
                                    int numberOfRequests = Integer.parseInt(clientNachricht.substring(8));
                                    System.out.println("HISTORY");
                                    if (verlauf.size() == 1) {
                                        sendMessageToClient(printException(404), out);
                                    } else if (verlauf.size() <= numberOfRequests) {
                                        for (String anfrage : verlauf) {
                                            serverNachricht = serverNachricht + "\n" + anfrage;
                                        }

                                    } else {
                                        for (int zaehler = verlauf.size() - numberOfRequests - 1; zaehler < verlauf.size() - 1; zaehler++) {
                                            serverNachricht = serverNachricht + "\n" + verlauf.get(zaehler);
                                        }
                                    }
                                    sendMessageToClient(serverNachricht, out);
                                } else if (clientNachricht.startsWith("HOLIDAYS")) {
                                    String year = clientNachricht.substring(9);
                                    try {
                                        int yearInt = Integer.parseInt(year);
                                        if (yearInt < 0 || yearInt > 10000) {
                                            sendMessageToClient(printException(400), out);
                                        } else {
                                            String[] url = {"GET", "feiertage-api.de", "/api/?jahr=" + year + "&nur_land=NW"};
                                            connectToWebUsingAPI(url, out);
                                        }
                                    } catch (Exception e) {
                                        sendMessageToClient(printException(400), out);
                                    }

                                } else if (clientNachricht.startsWith("GET")) {
                                    String[] url = clientNachricht.split(" ");
                                    if (url.length != 3) {
                                        sendMessageToClient(printException(400), out);
                                        sendMessageToClient("ende", out);
                                    } else {
                                        connectToWeb(url, out);
                                    }
                                } else {
                                    sendMessageToClient(printException(400), out);
                                }
                            }

                        }

                    } catch (IOException e) {
                        printException(500);
                    }
                    sendMessageToClient("ende", out);
                }
                System.out.println("Verbindung geschlossen");
                // close connection

                serverSocket.close();
                clientSocket.close();
                in.close();
                out.close();
            }

        } catch (IOException e) {
            System.out.println("Server konnte nicht gestartet werden!");
        }
    }

    public void sendMessageToClient(String serverMessage, DataOutputStream out) {
        try {
            out.writeUTF(serverMessage);
            out.flush();
        } catch (IOException e) {
            System.out.println("Nachricht konnte nicht gesendet werden");
        }
    }

    public String printException(int number) {
        switch (number) {
            case 400 -> {
                System.out.println("ERROR 400 BAD REQUEST");
                return "400 BAD REQUEST";
            }
            case 404 -> {
                System.out.println("ERROR 404 NOT FOUND");
                return "404 NOT FOUND";
            }
            case 500 -> {
                System.out.println("ERROR 500 INTERNAL SERVER ERROR");
                return "500 INTERNAL SERVER ERROR";
            }
            default -> {
                System.out.println("unbekannter Fehler");
                return "unbekannter Fehler";
            }
        }
    }

    public void connectToWeb(String[] url, DataOutputStream out) {
        String host = url[1];
        String request = url[2];

        try {
            Socket webSocket = new Socket(host, 80);

            BufferedReader webIn = new BufferedReader(new InputStreamReader(webSocket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter webOut = new BufferedWriter(new OutputStreamWriter(webSocket.getOutputStream(), StandardCharsets.UTF_8));
            String serverNachricht;
            webOut.write("GET " + request + " HTTP/1.1\n");
            webOut.flush();
            webOut.write("Host: " + host + "\n");
            webOut.flush();
            webOut.write("Connection: close\n");
            webOut.flush();
            webOut.write("\n");
            webOut.flush();


            while ((serverNachricht = webIn.readLine()) != null) {

                System.out.println(serverNachricht);
                sendMessageToClient(serverNachricht, out);
            }
            webIn.close();
            webOut.close();
            webSocket.close();

        } catch (IOException e) {
            sendMessageToClient(printException(500), out);
        }

    }

    public void connectToWebUsingAPI(String[] url, DataOutputStream out) {
        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://" + url[1] + url[2])).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            sendMessageToClient(response.body(), out);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
