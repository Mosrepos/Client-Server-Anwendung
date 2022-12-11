package heimaufgaben;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client {
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    int port = 2022;
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

        while (!Objects.equals(clientNachricht, "EXIT")) {
            try {

                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                while ((clientNachricht = br.readLine()) != null) {

                    if (clientNachricht.startsWith("GET")) {
                        out.writeUTF(clientNachricht);
                        out.flush();
                        while (!(serverNachricht = in.readUTF()).equals("ende")) {
                            System.out.println(serverNachricht);
                        }
                        System.out.println("angekommen");
                    } else {
                        out.writeUTF(clientNachricht);
                        out.flush();
                        serverNachricht = in.readUTF();
                        System.out.println(serverNachricht);
                    }
                }
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

