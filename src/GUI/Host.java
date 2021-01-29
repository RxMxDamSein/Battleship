package GUI;

import javafx.animation.Timeline;
import logic.Spiel;
import logic.save.SAFE_SOME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Klasse fuer den Multiplayer-Spieler-Host
 */
public class Host {
    /**
     * Verbindungs-Server-Socket
     */
    private ServerSocket ss;
    /**
     * Verbindungs-Socket
     */
    private Socket s;
    /**
     * Port und Feldgroesse
     */
    private int port, Feldg;
    /**
     * ließt die ankommenden Nachrichten des Client
     */
    private BufferedReader in;
    /**
     * schreibt die Nachrichten fuer den Client
     */
    private OutputStreamWriter out;
    /**
     * Variable die angibt ob das Spiel gestartet ist
     */
    public boolean Spielstartet = false;
    /**
     * true wenn es einen Verbindungsfehler gibt
     */
    public boolean ERROR = false;
    /**
     * Connected ist true wenn ein Client sich mit dem Server verbunden hat.
     * <br>
     * load gibt an ob ein geladenes Spiel genutzt wird
     */
    public boolean Connected = false, load = false;
    /**
     * true wenn gerade geschossen wird oder geschossen wurde
     */
    private boolean shooting = true;
    /**
     * true wenn man den Server erstellen konnte
     */
    public boolean Hosted = false;
    /**
     * nachrichten die geschickt oder versendet werden
     * <br>
     * id zum Speichern des Spiels
     */
    public String nachricht = "", id;
    /**
     * Spiel aus dem Logic-package
     */
    public Spiel dasSpiel;
    private Timeline updateT;
    private nuetzlicheMethoden nuetzlicheMethoden;

    /**
     * Nromaler Konstrukter von Host
     * @param p Port
     * @param g Feldgrosse
     * @param dasSpiel Bot-Schwierigkeit
     */
    public Host(int p, int g, Spiel dasSpiel) {
        port = p;
        Feldg = g;
        this.dasSpiel = dasSpiel;
    }

    /**
     * Konstruktor zum laden eines Spielstandes
     * @param p Port
     * @param g Feldgroesse
     * @param dasSpiel gespeichertes Spiel
     * @param id Spielstand-ID
     */
    public Host(int p, int g, Spiel dasSpiel, String id) {
        load = true;
        port = p;
        Feldg = g;
        this.dasSpiel = dasSpiel;
        this.id = id;
    }

    /**
     * initialisiert den Server,sendet je nach dem die Feldgroesse oder Spielstand-ID
     */
    public void init() {
        Runnable Runnable = () -> {
            ss = null;
            try {

                ss = new ServerSocket(port);
                ss.setSoTimeout(600000);
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

    public void setUpdateTimeline(Timeline t){
        updateT=t;
    }
    public void setNuetzlicheMethoden(nuetzlicheMethoden nuetzlicheMethoden){
        this.nuetzlicheMethoden=nuetzlicheMethoden;
    }
    /**
     * Funktion zum der Nachrichten an den Host
     * @param antwort Die Nachricht fuer den Host
     */
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
    /**
     * Funktion um die Nachricht vom Host zu bekommen
     * @return Nachricht vom Host
     */
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
    /**
     * sendet die Plazierten Schieffgroessen an den Client
     * @param shipSizes Array mit den Schiffgroesse
     */
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
    public boolean closed=false;
    /**
     * Schliesst die Verbindung
     */
    public void CutConnection() {
        ERROR = true;
        System.out.println("Closing Connection!");
        try {
            try{
                Thread.sleep(500);
                out.flush();
            }catch (IOException e){
                System.err.println("out already closed");
            }catch (InterruptedException e){
                e.printStackTrace();
            }catch (NullPointerException e){
                System.err.println("There was no connection established");
            }
            if(closed)
                return;
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
            closed=true;
            if(!dasSpiel.isOver()&& !closeOnPurpose && nuetzlicheMethoden!=null){
                nuetzlicheMethoden.connectionfeedback();
            }
        } catch (IOException e) {
            System.err.println("Can not close Socket!!");
            e.printStackTrace();
        }
    }


    /**
     * Funktion zum schiessen des Host.
     */
    public void schuss(int x, int y) {
        if (dasSpiel.getAbschussSpieler() != 1 || !dasSpiel.isStarted() || dasSpiel.isOver() || shooting|| dasSpiel.getFeld()[1][x][y]!=0) {
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
                updateT.play();
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
            updateT.play();
        };
        Thread t = new Thread(runnable);
        t.start();
    }
    /**
     * Funktion um auf den Schuss des Clients zu antworten
     * @param nachricht Schuss-Nachricht des Hosts
     */
    private void Clientshot(String nachricht) {
        int x = Integer.parseInt(nachricht.split(" ")[1]) - 1;
        int y = Integer.parseInt(nachricht.split(" ")[2]) - 1;
        dasSpiel.shoot(x, y, 0, 0, false);
        updateT.play();
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
    /**
     * Moegliche vom Client
     * @param nachricht Sondernachricht vom Host
     */
    private void sonderNachrichten(String nachricht) {
        if (nachricht.contains("save")) {
            dasSpiel.calcEnemyShips();
            new SAFE_SOME( new Spiel[]{dasSpiel}, 4, nachricht.split(" ")[1],dasSpiel.slayship, dasSpiel.slayX,dasSpiel.slayY, dasSpiel.enemyShips, dasSpiel.smallestShip, dasSpiel.longestShip);
            sendSocket("done");
            CutConnection();
        } else if (nachricht.contains("next")) {
            System.out.println("Sie sind an der Reihe!");
        } else {
            CutConnection();
        }

    }

    public boolean closeOnPurpose =false;
    /**
     * Speichert das Spiel
     * @param hash Speicher-ID
     */
    public void save(String hash) {
        sendSocket("save " + hash);
        if (!receiveSocket().contains("done")) {
            System.err.println("Client hat nicht wahrscheinlich gespeichert");
            CutConnection();
            return;
        }
        closeOnPurpose =true;
        CutConnection();

    }
}
