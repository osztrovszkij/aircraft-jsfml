package aircraft;

import aircraft.command.Category;
import aircraft.command.CommandQueue;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

/**
 * Created by roski on 5/11/2016.
 */
public class Entity extends SceneNode {
    public Entity(int hitpoints) {
        super(Category.NONE);

        mVelocity = new Vector2f(0, 0);
        mHitpoints = hitpoints;
    }
    public void setVelocity(Vector2f velocity) {
        mVelocity = velocity;
    }

    public void setVelocity(float vx, float vy) {
        mVelocity = new Vector2f(vx, vy);
    }

    public Vector2f getVelocity() {
        return mVelocity;
    }

    void accelerate(Vector2f velocity) {
        mVelocity = Vector2f.add(mVelocity, velocity);
    }

    void accelerate(float vx, float vy) {
        mVelocity = Vector2f.add(mVelocity, new Vector2f(vx, vy));
    }

    public int getHitpoints() {
        return mHitpoints;
    }

    public void repair(int points) {
        assert(points > 0);

        mHitpoints += points;
    }

    public void damage(int points) {
        assert(points > 0);

        mHitpoints -= points;
    }

    public void destroy() {
        mHitpoints = 0;
    }

    @Override
    public boolean isDestroyed() {
        return mHitpoints <= 0;
    }

    @Override
    public boolean isMarkedForRemoval() {
        return isDestroyed();
    }

    @Override
    protected void updateCurrent(Time dt, CommandQueue commands) {
        move(Vector2f.mul(mVelocity, dt.asSeconds()));
    }

    private Vector2f mVelocity;
    private int	mHitpoints;
}
