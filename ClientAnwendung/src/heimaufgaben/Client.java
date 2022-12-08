package heimaufgaben;

import java.io.*;
import java.net.*;

public class Client {
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    int port = 2022;
    //TODO ip adresse 127.0.0.1
    String address = "localhost";
    public Client() {
        try {
            socket = new Socket(address, port);
        } catch (IOException e){
            System.out.println("Es gibt ein Problem mit dem Socket");
        }
    }
    public void startClient(){


        String serverNachricht,clientNachricht = "";

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            //out.writeUTF(verbunden);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

        while (!clientNachricht.equals("Fertig")) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                clientNachricht = br.readLine();
                out.writeUTF(clientNachricht);
                out.flush();
                serverNachricht = in.readUTF();
                System.out.println(serverNachricht);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

