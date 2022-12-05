package heimaufgaben;

import java.util.Scanner;

public class Server{
    public static String scanPort() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Port");

        String Port = sc.nextLine();
        return Port;
    }
    /*
    public ServerSocket(){

    }

     */
}
