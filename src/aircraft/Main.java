package aircraft;

import javafx.util.Pair;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by roski on 5/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        Application app;
        try {
            app = new Application();
            app.run();
        } catch (IOException e) {
            System.out.println("Sorry for error");
        }
    }

}
