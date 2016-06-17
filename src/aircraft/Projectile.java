package aircraft;

import aircraft.command.Category;
import aircraft.command.CommandQueue;
import aircraft.datatable.ProjectileData;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

/**
 * Created by roski on 5/13/2016.
 */
public class Projectile extends Entity {

    public enum Type {
        ALLIED_BULLET,
        ENEMY_BULLET,
        MISSILE
    }

    public Projectile(Type type, final TextureHolder textures) {
        super(1);

        mType = type;
        mSprite = new Sprite(textures.get(table.get(type.ordinal()).texture));
        mTargetDirection = new Vector2f(0, 0);

        Utility.centerOrigin(mSprite);
    }

    public void	guideTowards(Vector2f position) {
        assert(isGuided());
        mTargetDirection = Utility.unitVector(Vector2f.add(position, Vector2f.neg(getWorldPosition())));
    }

    public boolean isGuided() {
        return mType == Type.MISSILE;
    }

    @Override
    public byte getCategory() {
        if (mType == Type.ENEMY_BULLET)
            return Category.ENEMY_PROJECTILE;
        else
            return Category.ALLIED_PROJECTILE;
    }

    @Override
    public FloatRect getBoundingRect() {
        return getWorldTransform().transformRect(mSprite.getGlobalBounds());
    }

    public float getMaxSpeed() {
        return table.get(mType.ordinal()).speed;
    }

    public int getDamage() {
        return table.get(mType.ordinal()).damage;
    }

    @Override
    protected void updateCurrent(Time dt, CommandQueue commands) {
        if (isGuided()) {
            final float approachRate = 200.f;

            Vector2f newVelocity = Utility.unitVector(Vector2f.mul(Vector2f.add(mTargetDirection, getVelocity()), approachRate * dt.asSeconds()));
            newVelocity = Vector2f.mul(newVelocity, getMaxSpeed());
            float angle = (float) Math.atan2(newVelocity.y, newVelocity.x);

            setRotation(Utility.toDegree(angle) + 90.f);
            setVelocity(newVelocity);
        }

        super.updateCurrent(dt, commands);
    }

    @Override
    protected void drawCurrent(RenderTarget target, RenderStates states) {
        target.draw(mSprite, states);
    }

    private Type mType;
    private Sprite mSprite;
    private Vector2f mTargetDirection;

    private final static ArrayList<ProjectileData> table = ProjectileData.initializeProjectileData();
}
