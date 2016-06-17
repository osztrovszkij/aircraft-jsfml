package aircraft;

import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by roski on 5/12/2016.
 */
public class TextureHolder {
    public enum ID {
        EAGLE,
        RAPTOR,
        AVENGER,
        BULLET,
        MISSILE,
        DESERT,
        HEALTH_REFILL,
        MISSILE_REFILL,
        FIRE_SPREAD,
        FIRE_RATE,
        TITLE_SCREEN,
        BUTTON_NORMAL,
        BUTTON_SELECTED,
        BUTTON_PRESSED
    }

    public TextureHolder() {
        textures = new HashMap<>();
    }

    public void load(ID id, String filename) throws IOException {
        Texture texture = new Texture();
        texture.loadFromFile(Paths.get(filename));
        textures.put(id, texture);
    }

    public Texture get(ID id) {
        return textures.get(id);
    }

    private HashMap<ID, Texture> textures;
}
