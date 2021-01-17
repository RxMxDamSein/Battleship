package logic.netCode;

import java.io.IOException;
import java.util.Random;

public class trash {
    public static void main(String[] args) throws IOException {
        Random random = new Random();
        while (true) {
            System.out.println(random.nextInt((10 - 8) + 1) + 8);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
