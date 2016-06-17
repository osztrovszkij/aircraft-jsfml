package aircraft;

import org.jsfml.graphics.Font;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by roski on 5/12/2016.
 */
public class FontHolder {
    public enum ID {
        MAIN
    }

    public FontHolder() {
        fonts = new HashMap<>();
    }

    public void load(ID id, String filename) throws IOException {
        Font font = new Font();
        font.loadFromFile(Paths.get(filename));
        fonts.put(id, font);
    }

    public Font get(ID id) {
        return fonts.get(id);
    }

    private HashMap<ID, Font> fonts;
}
