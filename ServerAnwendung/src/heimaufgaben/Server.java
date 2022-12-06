package heimaufgaben;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    Socket socket = null;

    public Server(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server started:\n Enter Port:");

            socket = server.accept();
            Scanner sc = new Scanner(socket.getInputStream());
            String Port = sc.nextLine();

            if (Integer.parseInt(Port)!=port){
                System.out.println("KEIN KORREKTER PORT \n Aktuell ist nur der Port 2022 moeglich");
            }
            else {
                System.out.println("Eingabe ist richtig");
                // takes input from the client socket
                DataInputStream in = null;
                try {
                    in = new DataInputStream(
                            new BufferedInputStream(socket.getInputStream()));
                    String line = "";

                    // reads message from client until "Done" is sent
                    while (!line.equals("Done")) {
                        try {
                            line = in.readUTF();
                            System.out.println(line);

                        } catch (IOException i) {
                            System.out.println(i);
                        }
                    }
                    System.out.println("Closing connection");

                    // close connection
                    socket.close();
                    in.close();




                } catch (IOException i) {
                System.out.println(i);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
