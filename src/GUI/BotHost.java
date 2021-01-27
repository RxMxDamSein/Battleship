package GUI;

import logic.Bot;
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
 * Klasse fuer den Multiplayer-Bot-Host
 */
public class BotHost {
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
     * change Variable, welche true wird wenn das Spielfeld aktualiesiert werden soll.
     */
    public volatile boolean change = false;
    /**
     * true wenn man den Server erstellen konnte
     */
    public boolean Hosted;
    /**
     * nachrichten die geschickt oder versendet werden
     * <br>
     * id zum Speichern des Spiels
     */
    public String nachricht = "", id;
    /**
     * Spiel aus dem Logic-package
     */
    private Spiel dasSpiel;
    /**
     * Bot aus dem Logic-package
     */
    private Bot derBot;
    /**
     * pause, true wenn der Bot pausieren soll.
     */
    public boolean pause = false;

    /**
     * Normaler Konstrukter von BotHost
     * @param p Port
     * @param g Feldgroesse
     * @param derBot Bot-Schwierigkeit
     */
    public BotHost(int p, int g, Bot derBot) {
        port = p;
        Feldg = g;
        this.dasSpiel = derBot.getDasSpiel();
        this.derBot = derBot;
    }

    /**
     * Konstruktor zum laden eines Spielstandes
     * @param p Port
     * @param g Feldgroesse
     * @param derBot Bot-Schwierigkeit
     * @param id Spielstand-ID
     */
    public BotHost(int p, int g, Bot derBot, String id) {
        load = true;
        port = p;
        Feldg = g;
        this.dasSpiel = derBot.getDasSpiel();
        this.derBot = derBot;
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


                // NICHT LADEN
                if (!load) {
                    sendSocket("size " + Feldg);
                    String z = receiveSocket();
                    if (!z.contains("next")) {
                        CutConnection();
                        return;
                    }
                    senships(Bot.getShipSizes(derBot.dasSpiel.schiffe,0));//need fix
                } else {
                    //LADEN
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
                    Spielstartet = true;
                    if (dasSpiel.getAbschussSpieler() == 0) {
                        sendSocket("next");
                        nachricht = receiveSocket();
                        if (nachricht.contains("shot"))
                            Clientshot(nachricht);
                        else
                            sonderNachrichten(nachricht);
                    } else {
                        schuss();
                    }
                }

            } catch (SocketException e) {
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
            Spielstartet = true;
            //schuss();
        };
        Thread t = new Thread(Runnable);
        t.start();
    }

    /**
     * Boolean, welche angibt ob der Server geschlossen wurde
     */
    private boolean closed=false;
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
            }
            if(closed==true)
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
        } catch (IOException e) {
            System.err.println("Can not close Socket!!");
            e.printStackTrace();
        }
    }
    /**
     * Funktion zum schiessen des Host.
     */
    public void schuss() {
        while (pause) ;
        if (dasSpiel.getAbschussSpieler() != 1 || dasSpiel.isOver() || !dasSpiel.isStarted()) {
            System.err.println("NIX SCHUSS");
            return;
        }
        Runnable runnable = () -> {
            int[] xy = derBot.getSchuss();
            sendSocket("shot " + (xy[0] + 1) + " " + (xy[1] + 1));
            String z = receiveSocket();
            System.out.println(z);
            if (!z.contains("answer")) {
                CutConnection();
            }
            if (z.contains("1")) {
                dasSpiel.shoot(xy[0], xy[1], 1, 1, false);
                derBot.setSchussFeld(xy[0], xy[1], 2, false);
                changeTrue();
                schuss();
            } else if (z.contains("2")) {
                dasSpiel.shoot(xy[0], xy[1], 1, 1, true);
                derBot.setSchussFeld(xy[0], xy[1], 2, true);
                changeTrue();
                schuss();
            } else if (z.contains("0")) {
                dasSpiel.shoot(xy[0], xy[1], 1, 0, false);
                derBot.setSchussFeld(xy[0], xy[1], 3, false);
                changeTrue();
                sendSocket("next");
                nachricht = receiveSocket();
                if (nachricht.contains("shot")) {
                    Clientshot(nachricht);
                } else
                    sonderNachrichten(nachricht);
            } else {
                CutConnection();
            }
            changeTrue();
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
        //derBot.setSchussFeld(x,y,);
        changeTrue();
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
     * aendert change auf true
     */
    private void changeTrue(){
        change=true;
        try {
            Thread.sleep(MultiHostBotController.sleeptime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Moegliche vom Client
     * @param nachricht Sondernachricht vom Host
     */
    private void sonderNachrichten(String nachricht) {
        if (nachricht.contains("save")) {
            new SAFE_SOME( new Spiel[]{dasSpiel}, 4, nachricht.split(" ")[1],derBot.slayship,derBot.slayX,derBot.slayY,derBot.enemyShips,derBot.smallestShip,derBot.longestShip);
            sendSocket("done");
            CutConnection();
        } else if (nachricht.contains("next")) {
            System.out.println("Sie sind an der Reihe!");
            schuss();
        } else {
            CutConnection();
        }

    }

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
        CutConnection();
    }
}
