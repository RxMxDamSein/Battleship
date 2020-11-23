package logic.save;

import java.io.IOException;
import java.io.Serializable;

public class SaveData implements Serializable {
    private static final long serialVersionUID=1337L;

    public String name;
    public int hp;

    public static void main(String[] args) {
        SaveData saveData=new SaveData();
        saveData.name="KeinNAme!";
        saveData.hp=20;
        try {
            ResourceManager.save(saveData,"test");
        } catch (IOException e) {
            System.out.println("Save Error");
            e.printStackTrace();
            return;
        }
        SaveData saveData1=null;
        try {
            saveData1= (SaveData) ResourceManager.load("test");
        } catch (IOException e) {
            System.out.println("Load Error");
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(saveData1.name+" "+saveData1.hp);
    }
}
