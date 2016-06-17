package aircraft;

import aircraft.command.Category;
import org.jsfml.graphics.*;

/**
 * Created by roski on 5/11/2016.
 */
public class SpriteNode extends SceneNode {
    public SpriteNode(Texture texture) {
        super(Category.NONE);
        mSprite = new Sprite(texture);
    }

    public SpriteNode(Texture texture, IntRect textureRect) {
        super(Category.NONE);
        mSprite = new Sprite(texture, textureRect);
    }

    @Override
    protected void drawCurrent(RenderTarget target, RenderStates states) {
        target.draw(mSprite, states);
    }

    private Sprite mSprite;
}
