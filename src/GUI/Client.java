package GUI;

import logic.Bot;
import logic.Spiel;
import logic.netCode.Server;
import logic.save.SAFE_SOME;
import sun.security.provider.ConfigFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * Klasse fuer den Multiplayer-Spieler-Client
 */
public class Client {
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
     * true wenn geschossen wird
     */
    private boolean shooting = false;
    /**
     * ERROR, true wenn ein Verbindungsfehler oder aehnliches auftritt.
     * <br>
     * change Variable, welche true wird wenn das Spielfeld aktualiesiert werden soll.
     * <br>
     * loaded, true wenn des Spiel geladen werden soll.
     */
    public boolean ERROR = false, change = false, loaded = false;
    /**
     * Spiel aus dem Logic-package
     */
    public Spiel dasSpiel;
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
    // 1 = Verbindung und Spielfeldgröße gegeben
    // 2 = Schiffsgrößen erhalten

    /**
     * Konstruktor fuer die Client-Klasse, welche sich  mit dem Host verbindet
     * @param IP IP-Adresse vom Host
     * @param PORT PORT vom Host
     */
    public Client(String IP, Integer PORT) {
        Runnable runnable = () -> {
            try {
                s = new Socket(IP, PORT);
                System.out.println("Connected!");
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new OutputStreamWriter(s.getOutputStream());
                String z = receiveSocket();
                if (z.contains("load")) {
                    loaded = true;
                    SAFE_SOME safe_some = SAFE_SOME.load(z.split(" ")[1]);
                    dasSpiel = safe_some.spiele[0];
                    status = 2;
                    System.out.println("status " + status + " error " + ERROR);
                    sendSocket("done");
                    if (!receiveSocket().contains("ready")) {
                        CutConnection();
                        return;
                    }
                    sendSocket("ready");
                    z = receiveSocket();
                    if (z.contains("shot"))
                        Servershot(z);
                    else
                        sonderNachrichten(z);

                } else if (z.contains("size")) {
                    dasSpiel = new Spiel(Integer.parseInt(z.split(" ")[1]), Integer.parseInt(z.split(" ")[1]), true);
                    dasSpiel.init();
                    status = 1;
                    sendSocket("next");
                    z = receiveSocket();
                    if (!z.contains("ships")) {
                        System.err.println("Ships von Server erwartet!!");
                        CutConnection();
                        return;
                    }
                    //System.out.println(z);
                    String[] s = z.split(" ");
                    ships = new int[s.length - 1];
                    for (int i = 1; i < s.length; i++) {
                        ships[i - 1] = Integer.parseInt(s[i]);
                        //System.out.println(ships[i-1]);
                    }
                    status = 2;
                    System.out.println("Status 2");
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

    public void deleteShip(int len) {
        for (int i = 0; i < ships.length; i++)
            if (ships[i] == len) {
                ships[i] = -1;
                return;
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
    /**
     * Trennt die Verbindung zum Host
     */
    public void CutConnection() {
        ERROR = true;
        System.out.println("Closing Connection!");
        try {
            s.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Can not close Socket!!");
            e.printStackTrace();
        }
    }
    /**
     * Speichert das Spiel
     * @param hash Speicher-Id
     * @param dname Speicher-Name
     */
    public void save(String hash, String dname) {
        new SAFE_SOME(null, new Spiel[]{dasSpiel}, 4, hash, dname);
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
                String nachricht = receiveSocket();
                if (nachricht.contains("shot")) {
                    Servershot(nachricht);
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
    /**
     * Funktion um auf den Schuss des Hosts zu antworten
     * @param nachricht Schuss-Nachricht des Hosts
     */
    private void Servershot(String nachricht) {
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
    /**
     * Funktion um zu ueberpruefen ob alle Schiffgroessen vom Host gesetzt wurden und um das Spiel zu starten.
     * @return false wenn man noch nitcht alle Schiffe gesetzt hat
     */
    public boolean senships() {
        for (int i : ships) {
            if (i > -1)
                return false;
        }
        Runnable runnable = () -> {
            sendSocket("done");
            if (!receiveSocket().contains("ready")) {
                System.err.println("Server sollte eigentlich Spiel starten!");
                CutConnection();
                return;
            }
            dasSpiel.starteSpiel(0);
            sendSocket("ready");
            String z = receiveSocket();
            if (z.contains("shot")) {
                Servershot(z);
            } else {
                sonderNachrichten(z);
            }

        };
        runnable.run();
        return true;
    }
}
