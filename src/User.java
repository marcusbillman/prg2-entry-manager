import java.io.Serializable;
import java.util.Random;

public class User implements Serializable {
    private String name;
    private int id;

    public User(String name) {
        this.name = name;
        this.id = generateID(6);
    }

    private int generateID(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
