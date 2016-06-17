package aircraft;

import aircraft.command.Category;
import aircraft.command.Command;
import aircraft.command.CommandQueue;
import aircraft.datatable.AircraftData;
import aircraft.datatable.Direction;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

/**
 * Created by roski on 5/11/2016.
 */
public class Aircraft extends Entity {
    public enum Type {
        EAGLE,
        RAPTOR,
        AVENGER
    }

    public Aircraft(Type type, final TextureHolder textures, final FontHolder fonts) {
        super(table.get(type.ordinal()).hitpoints);

        mType = type;
        mSprite = new Sprite(textures.get(table.get(type.ordinal()).texture));
        mFireCommand = new Command();
        mMissileCommand =  new Command();
        mFireCountdown = Time.ZERO;
        mIsFiring = false;
        mIsLaunchingMissile = false;
        mIsMarkedForRemoval = false;
        mFireRateLevel = 1;
        mSpreadLevel = 1;
        mMissileAmmo = 2;
        mDropPickupCommand = new Command();
        mTravelledDistance = 0.f;
        mDirectionIndex = 0;
        mHealthDisplay = null;
        mMissileDisplay = null;

        Utility.centerOrigin(mSprite);

        mFireCommand.category = Category.SCENE_AIR_LAYER;
        mFireCommand.action = (node, dt) -> createBullets(node, textures);

        mMissileCommand.category = Category.SCENE_AIR_LAYER;
        mMissileCommand.action   = (node, dt) -> createProjectile(node, Projectile.Type.MISSILE, 0.f, 0.5f, textures);

        mDropPickupCommand.category = Category.SCENE_AIR_LAYER;
        mDropPickupCommand.action   = (node, dt) -> createPickup(node, textures);

        TextNode healthDisplay = new TextNode(fonts, "");
        mHealthDisplay = healthDisplay;
        attachChild(healthDisplay);

        if (getCategory() == Category.PLAYER_AIRCRAFT) {
            TextNode missileDisplay = new TextNode(fonts, "");
            missileDisplay.setPosition(0, 70);
            mMissileDisplay = missileDisplay;
            attachChild(missileDisplay);
        }

        updateTexts();
    }

    @Override
    public byte getCategory() {
        if (isAllied())
            return Category.PLAYER_AIRCRAFT;
        else
            return Category.ENEMY_AIRCRAFT;
    }

    @Override
    public FloatRect getBoundingRect() {
        return getWorldTransform().transformRect(mSprite.getGlobalBounds());
    }

    @Override
    public boolean isMarkedForRemoval() {
        return mIsMarkedForRemoval;
    }



    public boolean isAllied() {
        return mType == Type.EAGLE;
    }

    public float getMaxSpeed() {
        return table.get(mType.ordinal()).speed;
    }

    public void	increaseFireRate() {
        if (mFireRateLevel < 10)
            ++mFireRateLevel;
    }

    public void	increaseSpread() {
        if (mSpreadLevel < 3)
            ++mSpreadLevel;
    }

    public void	collectMissiles(int count) {
        mMissileAmmo += count;
    }

    public void fire() {
        if (table.get(mType.ordinal()).fireInterval != Time.ZERO)
            mIsFiring = true;
    }

    public void	launchMissile() {
        if (mMissileAmmo > 0) {
            mIsLaunchingMissile = true;
            --mMissileAmmo;
        }
    }

    @Override
    protected void drawCurrent(RenderTarget target, RenderStates states) {
        target.draw(mSprite, states);
    }

    @Override
    protected void updateCurrent(Time dt, CommandQueue commands) {
        if (isDestroyed()) {
            //checkPickupDrop(commands);

            mIsMarkedForRemoval = true;
            return;
        }

        checkProjectileLaunch(dt, commands);

        updateMovementPattern(dt);
        super.updateCurrent(dt, commands);

        updateTexts();
    }

    private void updateMovementPattern(Time dt) {
        final ArrayList<Direction> directions = table.get(mType.ordinal()).directions;
        if (!directions.isEmpty()) {
            if (mTravelledDistance > directions.get(mDirectionIndex).distance) {
                mDirectionIndex = (mDirectionIndex + 1) % directions.size();
                mTravelledDistance = 0.f;
            }

            float radians = Utility.toRadian(directions.get(mDirectionIndex).angle + 90.f);
            float vx = (float) (getMaxSpeed() * Math.cos(radians));
            float vy = (float) (getMaxSpeed() * Math.sin(radians));

            setVelocity(vx, vy);

            mTravelledDistance += getMaxSpeed() * dt.asSeconds();
        }
    }

    private void checkPickupDrop(CommandQueue commands) {
//        if (!isAllied() && randomInt(3) == 0)
//            commands.push(mDropPickupCommand);
    }

    private void checkProjectileLaunch(Time dt, CommandQueue commands) {
        if (!isAllied())
            fire();

        if (mIsFiring && mFireCountdown.asMicroseconds() <= Time.ZERO.asMicroseconds()) {
            commands.push(mFireCommand);
            mFireCountdown = Time.add(mFireCountdown, Time.div(table.get(mType.ordinal()).fireInterval, (mFireRateLevel + 1.f)));
            mIsFiring = false;
        } else if (mFireCountdown.asMicroseconds() > Time.ZERO.asMicroseconds()) {
            mFireCountdown = Time.sub(mFireCountdown, dt);
            mIsFiring = false;
        }

        if (mIsLaunchingMissile) {
            commands.push(mMissileCommand);
            mIsLaunchingMissile = false;
        }
    }

    private void createBullets(SceneNode node, final TextureHolder textures) {
        Projectile.Type type = isAllied() ? Projectile.Type.ALLIED_BULLET : Projectile.Type.ENEMY_BULLET;

        switch (mSpreadLevel) {
            case 1:
                createProjectile(node, type, 0.0f, 0.5f, textures);
                break;

            case 2:
                createProjectile(node, type, -0.33f, 0.33f, textures);
                createProjectile(node, type, +0.33f, 0.33f, textures);
                break;

            case 3:
                createProjectile(node, type, -0.5f, 0.33f, textures);
                createProjectile(node, type,  0.0f, 0.5f, textures);
                createProjectile(node, type, +0.5f, 0.33f, textures);
                break;
        }
    }

    private void createProjectile(SceneNode node, Projectile.Type type, float xOffset, float yOffset, final TextureHolder textures) {
        Projectile projectile = new Projectile(type, textures);

        Vector2f offset = new Vector2f(xOffset * mSprite.getGlobalBounds().width, yOffset * mSprite.getGlobalBounds().height);
        Vector2f velocity = new  Vector2f(0, projectile.getMaxSpeed());

        float sign = isAllied() ? -1.f : +1.f;
        projectile.setPosition(Vector2f.add(getWorldPosition(), Vector2f.mul(offset, sign)));
        projectile.setVelocity(Vector2f.mul(velocity, sign));
        node.attachChild(projectile);
    }

    private void createPickup(SceneNode node, final TextureHolder textures) {
//        auto type = static_cast<Pickup::Type>(randomInt(Pickup::TypeCount));
//
//        std::unique_ptr<Pickup> pickup(new Pickup(type, textures));
//        pickup->setPosition(getWorldPosition());
//        pickup->setVelocity(0.f, 1.f);
//        node.attachChild(std::move(pickup));
    }

    private void updateTexts() {
        mHealthDisplay.setString(getHitpoints() + " HP");
        mHealthDisplay.setPosition(0.f, 50.f);
        mHealthDisplay.setRotation(-getRotation());

        if (mMissileDisplay != null) {
            if (mMissileAmmo == 0)
                mMissileDisplay.setString("");
            else
                mMissileDisplay.setString("M: " + mMissileAmmo);
        }
    }

    private Type mType;
    private Sprite mSprite;
    private Command mFireCommand;
    private Command	mMissileCommand;
    private Time mFireCountdown;
    private boolean mIsFiring;
    private boolean	mIsLaunchingMissile;
    private boolean	mIsMarkedForRemoval;

    private int	mFireRateLevel;
    private int	mSpreadLevel;
    private int	mMissileAmmo;

    private Command mDropPickupCommand;
    private float mTravelledDistance;
    private int	mDirectionIndex;
    private TextNode mHealthDisplay;
    private TextNode mMissileDisplay;

    private static final ArrayList<AircraftData> table = AircraftData.initializeAircraftData();
}
