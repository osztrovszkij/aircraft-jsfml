package aircraft;

import aircraft.command.Category;
import aircraft.datatable.PickupData;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

import java.util.ArrayList;

/**
 * Created by roski on 5/13/2016.
 */
public class Pickup extends Entity {
    public enum Type {
        HEALTH_REFILL,
        MISSILE_REFILL,
        FIRE_SPREAD,
        FIRE_RATE
    }

    public Pickup(Type type, final TextureHolder textures) {
        super(1);

        mType = type;
        //mSprite = new Sprite(textures.get(table.get(type.ordinal()).texture));

        Utility.centerOrigin(mSprite);
    }

    @Override
    public byte getCategory() {
        return Category.PICKUP;
    }

    @Override
    public FloatRect getBoundingRect() {
        return getWorldTransform().transformRect(mSprite.getGlobalBounds());
    }

    public void apply(Aircraft player) {
        //table.get(mType.ordinal()).action(player);
    }

    @Override
    protected void drawCurrent(RenderTarget target, RenderStates states) {
        target.draw(mSprite, states);
    }

    private Type mType;
    private Sprite mSprite;

    //final static ArrayList<PickupData> table = PickupData.initializePickupData();
}
