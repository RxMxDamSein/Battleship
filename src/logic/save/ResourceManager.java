package logic.save;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {
    public static void save(Serializable data, String fileName) throws IOException {
        new File("./save/").mkdirs();
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("./save/" + fileName)));
        oos.writeObject(data);
        oos.close();
    }

    public static Object load(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("./save/" + fileName)));
        Object ret=ois.readObject();
        ois.close();
        return ret;
    }
}
