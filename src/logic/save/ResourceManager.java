package logic.save;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Eine Klasse um serialisierbare Objekte zu speichern und zu laden
 */
public class ResourceManager {
    /**
     * Speichert ein serialisierbares Objekt
     * @param data serialisierbares Objekt
     * @param fileName Dateiname der zu speichernden Datei
     * @throws IOException im Fehlerfall
     */
    public static void save(Serializable data, String fileName) throws IOException {
        new File("./save/").mkdirs();
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("./save/" + fileName)));
        oos.writeObject(data);
        oos.close();
    }

    /**
     * lädt ein serialisierbares Objekt
     * @param fileName Dateiname der zu ladenden Datei
     * @return Object des geladenen serialisierbaren Objekts
     * @throws IOException falls die Datei nicht geladen werden kann
     * @throws ClassNotFoundException falls das Objekt nicht geladen werden kann
     */
    public static Object load(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("./save/" + fileName)));
        Object ret=ois.readObject();
        ois.close();
        return ret;
    }
}
