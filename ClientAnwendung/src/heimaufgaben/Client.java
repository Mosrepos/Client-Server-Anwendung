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

                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));//Zuständig für die Eingabe
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));//Zuständig für die Übermittlung an  den Server
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//liest die Eingabe vom Client

                while ((clientNachricht = br.readLine()) != null) { //wir lesen die Eingabe vom Client, solange Client verbunden ist

                    out.writeUTF(clientNachricht);
                    out.flush();
                    while (!(serverNachricht = in.readUTF()).equals("ende")) {
                        System.out.println(serverNachricht);
                    }
                }

                socket.close(); //Verbindung wird getrennt.
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
