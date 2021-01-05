package GUI;

import logic.Bot;
import logic.Spiel;
import sun.security.provider.ConfigFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket s;
    private BufferedReader in;
    private OutputStreamWriter out;
    public boolean ERROR=false;
    public Spiel dasSpiel;
    public int[] ships;
    public int status=0;
    // 0 = keine verbindung
    // 1 = Verbindung und Spielfeld
    // 2 = Schiffsgrößen erhalten






    public Client(String IP, Integer PORT) {
        Runnable runnable = ()->{
            try {
                s = new Socket(IP, PORT);
                System.out.println("Connected!");
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new OutputStreamWriter(s.getOutputStream());
                String z =receiveSocket();
                if (z.contains("load")) {

                } else if (z.contains("size")) {
                    dasSpiel = new Spiel(Integer.parseInt(z.split(" ")[1]),Integer.parseInt(z.split(" ")[2]),true);
                    dasSpiel.init();
                    status = 1;
                    sendSocket("next");
                    z = receiveSocket();
                    if (!z.contains("ships")) {
                        System.err.println("Ships von Server erwartet!!");
                        CutConnection();
                        return;
                    }
                    String[] s = z.split(" ");
                    ships = new int[s.length-1];
                    for (int i=1;i<s.length;i++) {
                        ships[i-1] = Integer.parseInt(s[i]);
                    }
                    status = 2;
                } else {
                    CutConnection();
                    return;
                }
            } catch (IOException e) {
                System.err.println("Cannot create Socket!!");
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


    }

    public  void sendSocket(String antwort){
        System.out.print("Zu Client: " + antwort + "\n");
        try {
            out.write(String.format("%s%n", antwort));
            out.flush();
        }catch (IOException e){
            System.err.println("Can not send Antwort to Client!");
            e.printStackTrace();
        }catch (NullPointerException e){
            System.err.println("can not sent anything if it does not exist!");
            e.printStackTrace();
        }
    }

    public String receiveSocket(){
        String nachricht="ERROR";
        try {
            nachricht = in.readLine();
            System.out.println("Von Client: " + nachricht);
        }catch (IOException e){
            System.err.println("Can not receive Nachricht from Client!");
            e.printStackTrace();
        }
        return nachricht;
    }

    public void CutConnection() {
        ERROR = true;
        System.out.println("Closing Connection!");
        try {
            s.close();
        } catch (IOException e) {
            System.err.println("Can not close Socket!!");
            e.printStackTrace();
        }
    }
}
