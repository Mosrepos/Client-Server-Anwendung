package heimaufgaben;

import java.io.*;
import java.io.PrintWriter;
import java.net.Socket;
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

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            input = new DataInputStream(System.in);
            output = new DataOutputStream(System.out);

            /*
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());
            System.out.println("connected");

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Something");
            String s = sc.nextLine();

            out.println(s);
            out.flush();
            out.close();
             */
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        //below line is to read message from input
        while (!(line.equals("Done"))) {
            try {
                line = input.readLine();
                output.writeUTF(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //below code to close the connection
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
