package heimaufgaben;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server{
    public static void scanPort() {
        String login_Port = "2022";
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Port");

        String Port = sc.nextLine();
        if (!Port.equals(login_Port)){
            System.out.println("KEIN KORREKTER PORT \n Aktuell ist nur der Port 2022 moeglich");
        }
        else {
            System.out.println("Eingabe ist richtig");
        }
    }

    public Server() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(2022);

            while (true) //TODO
            {
                Socket client = socket.accept();
                PrintWriter out = new PrintWriter(client.getOutputStream());
                Scanner in = new Scanner(client.getInputStream());

                String s = in.nextLine();
                s = s.toLowerCase();
                out.println(s);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
