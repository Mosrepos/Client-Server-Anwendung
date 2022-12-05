package heimaufgaben;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static String scanPort() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Port");

        String Port = sc.nextLine();
        return Port;
    }

    public static String scanIP_Address() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter IP Address");

        String IP = sc.nextLine();
        return IP;
    }


}
