package heimaufgaben;


public class Main {
    public static void main(String[] args) {


        /*
        Socket soc = new Socket("localhost", 2022);
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(soc.getInputStream(),"utf-8"), 8192);
        while(!fromServer.ready()) Thread.sleep(5);
        String line = fromServer.readLine();
        System.out.println(line);
        fromServer.close();
        soc.close();


         */
        Client client = new Client();
        client.scan();
        System.out.println("Der Client wurde beendet.");

    }
}
