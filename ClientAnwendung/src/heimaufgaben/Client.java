package heimaufgaben;

import java.io.*;
import java.net.Socket;

public class Client {
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    int port = 2022;
    String address = "localhost";

    public Client() {
        try {
            socket = new Socket(address, port); // Es wird ein neues Socket initialisiert, um die Verbindung herzustellen.
        } catch (IOException e) { 
            System.out.println("Es gibt ein Problem mit dem Socket");
        }
    }

    public void startClient() {
        String serverNachricht, clientNachricht = "";

        while (!(clientNachricht.equals("EXIT"))) {
            /* Die While-Schleife wird durchgegangen,
            bis der Input-String "EXIT" entspricht, erst dann wird die Verbindung zum Server damit abgeschlossen*/
            try {

                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                while ((clientNachricht = br.readLine()) != null) {

                    out.writeUTF(clientNachricht);
                    out.flush();
                    while (!(serverNachricht = in.readUTF()).equals("ende")) {
                        System.out.println(serverNachricht);
                    }
                }

                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
