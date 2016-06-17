package aircraft;

import javafx.util.Pair;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import java.util.HashSet;

/**
 * Created by roski on 5/12/2016.
 */
public class Utility {
    public static void centerOrigin(Sprite sprite) {
        FloatRect bounds = sprite.getLocalBounds();
        sprite.setOrigin((float)Math.floor((float)bounds.left + bounds.width / 2.f), (float)Math.floor(bounds.top + bounds.height / 2.f));
    }

    public static void centerOrigin(Text text) {
        FloatRect bounds = text.getLocalBounds();
        text.setOrigin((float)Math.floor(bounds.left + bounds.width / 2.f), (float)Math.floor(bounds.top + bounds.height / 2.f));
    }

    public static Pair<SceneNode, SceneNode> minmax(SceneNode a, SceneNode b) {
        return (b == a) ? new Pair<>(b, a)
                       : new Pair<>(a, b);
    }

    public static float toDegree(float radian) {
        return 180.f / 3.141592653589793238462643383f * radian;
    }

    public static float toRadian(float degree) {
        return 3.141592653589793238462643383f / 180.f * degree;
    }

    public static float length(Vector2f vector) {
        return (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    }

    public static Vector2f unitVector(Vector2f vector) {
        return Vector2f.div(vector, length(vector));
    }
}
