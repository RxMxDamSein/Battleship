package GUI;

import logic.Spiel;
import logic.netCode.Server;
import logic.save.SAFE_SOME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Host {

    private ServerSocket ss;
    private Socket s;
    private int port, Feldg;
    private BufferedReader in;
    private OutputStreamWriter out;
    private boolean Spielstartet = false;
    public boolean ERROR = false;
    public boolean Connected = false, change = false, load = false;
    public boolean Hosted = false;
    public String nachricht = "", id;
    private Spiel dasSpiel;

    public Host(int p, int g, Spiel dasSpiel) {
        port = p;
        Feldg = g;
        this.dasSpiel = dasSpiel;
    }

    public Host(int p, int g, Spiel dasSpiel, String id) {
        load = true;
        port = p;
        Feldg = g;
        this.dasSpiel = dasSpiel;
        this.id = id;
    }

    public void init() {
        Runnable Runnable = () -> {
            ss = null;
            try {

                ss = new ServerSocket(port);
                ss.setSoTimeout(60000);
                ss.setReuseAddress(true);
                Hosted = true;
                System.out.println("Waiting for client ...");
                s = ss.accept();
                s.setReuseAddress(true);
                System.out.println("Connection established");
                Connected = true;
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new OutputStreamWriter(s.getOutputStream());
                // LADEN
                if (!load) {
                    //////////
                    //Thread.sleep(10000);
                    //////////
                    sendSocket("size " + Feldg);
                    String z = receiveSocket();
                    if (!z.contains("next")) {
                        CutConnection();
                        return;
                    }
                } else {
                    sendSocket("load " + id);
                    String z = receiveSocket();
                    if (!z.contains("done")) {
                        CutConnection();
                        return;
                    }
                    sendSocket("ready");
                    z = receiveSocket();
                    if (!z.contains("ready")) {
                        CutConnection();
                        return;
                    }
                    shooting = false;
                    Spielstartet = true;
                    if (dasSpiel.getAbschussSpieler() == 0) {
                        sendSocket("next");
                        nachricht = receiveSocket();
                        if (nachricht.contains("shot"))
                            Clientshot(nachricht);
                        else
                            sonderNachrichten(nachricht);
                    }
                }

            }catch (SocketException e) {
                System.out.println("Socket closed");
            } catch (IOException e){
                System.err.println("Can not create Socket!");
                ERROR = true;
                e.printStackTrace();
            }

        };
        Thread t = new Thread(Runnable);
        t.start();


    }

    public void sendSocket(String antwort) {
        System.out.print("Zu Client: " + antwort + "\n");
        try {
            out.write(String.format("%s%n", antwort));
            out.flush();
        } catch (IOException e) {
            System.err.println("Can not send Antwort to Client!");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("can not sent anything if it does not exist!");
            e.printStackTrace();
        }
    }

    public String receiveSocket() {
        String nachricht = "ERROR";
        try {
            nachricht = in.readLine();
            System.out.println("Von Client: " + nachricht);
        } catch (IOException e) {
            System.err.println("Can not receive Nachricht from Client!");
            e.printStackTrace();
        }
        if (nachricht == null) {
            CutConnection();
        }
        return nachricht;
    }

    public void senships(int[] shipSizes) {
        if (Spielstartet) {
            System.err.println("Spiel bereit gestartet!!");
            return;
        }
        Runnable Runnable = () -> {
            String antwort = "ships";
            for (int i = shipSizes.length - 1; i >= 0; i--) {

                antwort += " " + shipSizes[i];
            }
            sendSocket(antwort);
            String z = receiveSocket();
            if (!z.contains("done")) {
                CutConnection();
            }
            sendSocket("ready");
            z = receiveSocket();
            if (!z.contains("ready")) {
                CutConnection();
            }
            shooting = false;
            Spielstartet = true;
        };
        Thread t = new Thread(Runnable);
        t.start();
    }

    public void CutConnection() {
        ERROR = true;
        System.out.println("Closing Connection!");
        try {
            if(s!=null){
                s.setSoTimeout(100);
                s.shutdownInput();
                s.shutdownOutput();
                s.close();
            }
            ss.setSoTimeout(100);
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
            ss.close();

        } catch (IOException e) {
            System.err.println("Can not close Socket!!");
            e.printStackTrace();
        }
    }

    private boolean shooting = true;

    public void schuss(int x, int y) {
        if (dasSpiel.getAbschussSpieler() != 1 || !dasSpiel.isStarted() || dasSpiel.isOver() || shooting) {
            System.err.println("NIX SCHUSS");
            return;
        }
        shooting = true;
        Runnable runnable = () -> {
            sendSocket("shot " + (x + 1) + " " + (y + 1));
            String z = receiveSocket();
            System.out.println(z);
            if (!z.contains("answer")) {
                CutConnection();
            }
            if (z.contains("1")) {
                dasSpiel.shoot(x, y, 1, 1, false);
            } else if (z.contains("2")) {
                dasSpiel.shoot(x, y, 1, 1, true);
            } else if (z.contains("0")) {
                dasSpiel.shoot(x, y, 1, 0, false);
                change = true;
                sendSocket("next");
                nachricht = receiveSocket();
                if (nachricht.contains("shot")) {
                    Clientshot(nachricht);
                } else
                    sonderNachrichten(nachricht);
            } else {
                CutConnection();
            }
            shooting = false;
            change = true;
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    private void Clientshot(String nachricht) {
        int x = Integer.parseInt(nachricht.split(" ")[1]) - 1;
        int y = Integer.parseInt(nachricht.split(" ")[2]) - 1;
        dasSpiel.shoot(x, y, 0, 0, false);
        change = true;
        String antwort = "answer ";
        switch (dasSpiel.getFeld()[0][x][y]) {
            case 2:
            case 4:
                if (dasSpiel.istVersenkt()) {
                    antwort += "2";
                } else {
                    antwort += "1";
                }
                break;
            case 3:
                antwort += "0";
                break;
            default:
                System.err.println("Spielbrett sollte beschossen sein");
                CutConnection();
                return;
        }
        sendSocket(antwort);
        String z = receiveSocket();
        if (z.contains("shot")) {
            Clientshot(z);
            return;
        }
        sonderNachrichten(z);
    }

    private void sonderNachrichten(String nachricht) {
        if (nachricht.contains("save")) {
            new SAFE_SOME(null, new Spiel[]{dasSpiel}, 4, nachricht.split(" ")[1]);
            sendSocket("done");
            CutConnection();
        } else if (nachricht.contains("next")) {
            System.out.println("Sie sind an der Reihe!");
        } else {
            CutConnection();
        }

    }

    public void save(String hash) {
        sendSocket("save " + hash);
        if (!receiveSocket().contains("done")) {
            System.err.println("Client hat nicht wahrscheinlich gespeichert");
            CutConnection();
            return;
        }
        CutConnection();
    }
}
