package GUI;

import javafx.animation.Timeline;
import logic.*;
import logic.save.SAFE_SOME;

import java.io.*;
import java.net.Socket;

/**
 * Klasse fuer den Multiplayer-Bot-Client
 */
public class BotClient {
    /**
     * Verbindungs-Socket
     */
    private Socket s;
    /**
     * ließt die ankommenden Nachrichten des Host
     */
    private BufferedReader in;
    /**
     * schreibt die Nachrichten fuer den Host
     */
    private OutputStreamWriter out;
    /**
     * ERROR, true wenn ein Verbindungsfehler oder aehnliches auftritt.
     * <br>
     * loaded, true wenn des Spiel geladen werden soll.
     * <br>
     * pause, true wenn der Bot pausieren soll.
     */
    public boolean ERROR = false, loaded = false, pause = false;

    /**
     * Spiel aus dem Logic-package
     */
    public Spiel dasSpiel;
    /**
     * Bot aus dem Logic-package
     */
    public Bot derBot;
    /**
     * Schiffgroessen vom Host
     */
    public int[] ships;//if ships is added make it -1
    /**
     * Verbindungsstatus:
     * <br>
     * 0 = keine Verbindung
     * <br>
     * 1 = Verbindung hergestellt und Spielfeldgroesse bekommen
     * <br>
     * 2 = Schiffsgroessen erhalten
     */
    public int status = 0;
    // 0 = keine verbindung
    // 1 = Verbindung und Spielfeld
    // 2 = Schiffsgrößen erhalten

    private Timeline updateT;

    /**
     * Konstruktor fuer die BotClient-Klasse, welche sich  mit dem Host verbindet
     * @param IP IP-Adresse vom Host
     * @param PORT PORT vom Host
     * @param bot Bot-Schwierigkeit
     */
    public BotClient(String IP, Integer PORT, int bot) {
        Runnable runnable = () -> {
            try {

                s = new Socket(IP, PORT);
                s.setReuseAddress(true);
                System.out.println("Connected!");
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new OutputStreamWriter(s.getOutputStream());
                String z = receiveSocket();
                if (z!=null && z.contains("load")) {
                    loaded = true;
                    SAFE_SOME safe_some = SAFE_SOME.load(z.split(" ")[1]);
                    dasSpiel = safe_some.spiele[0];
                    int x = dasSpiel.getSizeX();
                    int y = dasSpiel.getSizeY();
                    switch (bot) {
                        case 1:
                            derBot = new RDM_Bot(x, y);
                            break;
                        case 2:
                            derBot = new Bot_lvl_2(x, y);
                            break;
                        case 3:
                            derBot = new Bot_schwer(x, y);
                            break;
                        default:
                            System.err.println("Bot Error!!");
                            ERROR = true;
                            CutConnection();
                            return;
                    }
                    derBot.dasSpiel = dasSpiel;
                    status = 2;
                    System.out.println("status " + status + " error " + ERROR);
                    sendSocket("done");
                    if (!receiveSocket().contains("ready")) {
                        CutConnection();
                        return;
                    }
                    sendSocket("ready");
                    z = receiveSocket();
                    if (z!=null && z.contains("shot"))
                        Servershot(z);
                    else
                        sonderNachrichten(z);

                } else if (z!=null && z.contains("size")) {
                    int x = Integer.parseInt(z.split(" ")[1]), y = Integer.parseInt(z.split(" ")[1]);
                    switch (bot) {
                        case 1:
                            derBot = new RDM_Bot(x, y);
                            break;
                        case 2:
                            derBot = new Bot_lvl_2(x, y);
                            break;
                        case 3:
                            derBot = new Bot_schwer(x, y);
                            break;
                        default:
                            System.err.println("Bot Error!!");
                            ERROR = true;
                            CutConnection();
                            return;
                    }
                    dasSpiel = derBot.dasSpiel;
                    status = 1;
                    sendSocket("next");
                    z = receiveSocket();
                    if (z==null || !z.contains("ships")) {
                        System.err.println("Ships von Server erwartet!!");
                        CutConnection();
                        return;
                    }
                    String[] s = z.split(" ");
                    ships = new int[s.length - 1];
                    for (int i = 1; i < s.length; i++) {
                        ships[i - 1] = Integer.parseInt(s[i]);
                    }
                    status = 2;
                    senships();
                } else {
                    CutConnection();
                    return;
                }
            } catch (IOException e) {
                ERROR = true;
                System.err.println("Cannot create Socket!!");
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


    }

    public void setUpdateTimeline(Timeline t){
        updateT=t;
    }

    /**
     * Funktion zum der Nachrichten an den Host
     * @param antwort Die Nachricht fuer den Host
     */
    public void sendSocket(String antwort) {
        System.out.print("Zu Server: " + antwort + "\n");
        try {
            out.write(String.format("%s%n", antwort));
            out.flush();
        } catch (IOException e) {
            System.err.println("Can not send Antwort to Server!");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("can not sent anything if it does not exist!");
            e.printStackTrace();
        }
    }

    /**
     * Funktion um fest zu stellen ob die gewählte Schiffsgroesse zulaessig ist.
     * @param len Schiffgroesse
     * @return false wenn
     */
    public boolean inShips(int len) {
        if (ships == null)
            return false;
        for (int i : ships) {
            if (len == i)
                return true;
        }
        return false;
    }

    /**
     * loescht verwendete Schiffgroessen.
     * @param len Schiffgroesse
     */
    public void deleteShip(int len) {
        for (int i = 0; i < ships.length; i++)
            if (ships[i] == len) {
                ships[i] = -1;
                return;
            }
    }

    /**
     * aendert change auf true
     */
    private void update(){

        try {
            updateT.play();
            Thread.sleep(MultiClientBotController.sleeptime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            System.err.println("updateTimeline is missing");
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
            System.out.println("Von Server: " + nachricht);
        } catch (IOException e) {
            System.err.println("Can not receive Nachricht from Server!");
            e.printStackTrace();
        }
        if (nachricht == null) {
            CutConnection();
        }
        return nachricht;
    }

    public boolean closed=false;
    /**
     * Trennt die Verbindung zum Host
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
                s.setSoTimeout(10);
                s.shutdownInput();
                s.shutdownOutput();
                s.close();
            }
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
            closed=true;
        } catch (IOException e) {
            System.err.println("Can not close Socket!!");
            //e.printStackTrace();
        }
    }

    /**
     * Speichert das Spiel
     * @param hash Speicher-Id
     * @param dname Speicher-Name
     */
    public void save(String hash, String dname) {
        new SAFE_SOME( new Spiel[]{dasSpiel}, 4, hash, dname,derBot.slayship, derBot.slayX,derBot.slayY, derBot.enemyShips, derBot.smallestShip, derBot.longestShip);
        sendSocket("save " + hash);
        if (!receiveSocket().contains("done")) {
            System.err.println("Client hat nicht wahrscheinlich gespeichert");
            CutConnection();
            return;
        }
        CutConnection();
    }

    /**
     * Funktion zum schiessen des Clients.
     */
    public void schuss() {
        if (dasSpiel.getAbschussSpieler() != 1 || !dasSpiel.isStarted() || dasSpiel.isOver()) {
            System.err.println("NIX SCHUSS");
            return;
        }
        Runnable runnable = () -> {
            while (pause) ;
            int[] xy = derBot.getSchuss();
            int x = xy[0];
            int y = xy[1];
            sendSocket("shot " + (x + 1) + " " + (y + 1));
            String z = receiveSocket();
            System.out.println(z);
            if (z==null ||!z.contains("answer")) {
                CutConnection();
                return;
            }
            if (z.contains("1")) {
                dasSpiel.shoot(x, y, 1, 1, false);
                derBot.setSchussFeld(x, y, 2, false);
                update();
                schuss();
            } else if (z.contains("2")) {
                dasSpiel.shoot(x, y, 1, 1, true);
                derBot.setSchussFeld(x, y, 2, true);
                update();

                //
                if (dasSpiel.isOver()) {
                    System.out.println("Spiel gewonnen!!");
                }
                ////////////
                schuss();
            } else if (z.contains("0")) {
                dasSpiel.shoot(x, y, 1, 0, false);
                derBot.setSchussFeld(x, y, 3, false);
                update();
                sendSocket("next");
                String nachricht = receiveSocket();
                if (nachricht.contains("shot")) {
                    Servershot(nachricht);
                } else
                    sonderNachrichten(nachricht);
            } else {
                CutConnection();
            }
            update();
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    /**
     * Funktion um auf den Schuss des Hosts zu antworten
     * @param nachricht Schuss-Nachricht des Hosts
     */
    private void Servershot(String nachricht) {
        int x = Integer.parseInt(nachricht.split(" ")[1]) - 1;
        int y = Integer.parseInt(nachricht.split(" ")[2]) - 1;
        dasSpiel.shoot(x, y, 0, 0, false);
        update();
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
        if (z!=null && z.contains("shot")) {
            Servershot(z);
            return;
        }
        sonderNachrichten(z);
    }

    /**
     * Moegliche vom Host
     * @param nachricht Sondernachricht vom Host
     */
    private void sonderNachrichten(String nachricht) {
        if (nachricht!=null && nachricht.contains("save")) {
            new SAFE_SOME(null, new Spiel[]{dasSpiel}, 4, nachricht.split(" ")[1]);
            sendSocket("done");
            CutConnection();
        } else if (nachricht!=null && nachricht.contains("next")) {
            schuss();
        } else {
            CutConnection();
        }

    }

    /**
     * Funktion um die Schiffgroessen vom Host zu setzen und um das Spiel zu starten.
     * @return returnt ob man die Schiff stzen konnte oder nicht.
     */
    public boolean senships() {
        Runnable runnable = () -> {
            if (!derBot.shipSizesToAdd(ships)) {
                CutConnection();
                System.err.println("Bot shipsizestoadd fehler");
                return;
            }
            dasSpiel.setAbschussSpieler(0);
            sendSocket("done");
            if (!receiveSocket().contains("ready")) {
                System.err.println("Server sollte eigentlich Spiel starten!");
                CutConnection();
                return;
            }
            dasSpiel.starteSpiel(0);
            sendSocket("ready");
            String z = receiveSocket();
            if (z!=null && z.contains("shot")) {
                Servershot(z);
            } else {
                sonderNachrichten(z);
            }

        };
        Thread t=new Thread(runnable);
        t.start();
        return true;
    }
}
