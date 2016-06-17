package aircraft;

import aircraft.command.Category;
import aircraft.command.Command;
import aircraft.command.CommandQueue;
import javafx.util.Pair;
import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.util.*;

import static sun.audio.AudioPlayer.player;
import static sun.swing.MenuItemLayoutHelper.max;

/**
 * Created by roski on 5/11/2016.
 */
public class World {
    public World(RenderWindow window, FontHolder fonts) throws IOException {
        mWindow = window;
        mWorldView = new View(window.getDefaultView().getCenter(), window.getDefaultView().getSize());
        mFonts = fonts;
        mTextures = new TextureHolder();
        mSceneGraph = new SceneNode(Category.NONE);
        mSceneLayers = new HashMap<>();
        mWorldBounds = new FloatRect(0.f, 0.f, mWorldView.getSize().x, 2400.f);
        mSpawnPosition = new Vector2f(mWorldView.getSize().x / 2.f, mWorldBounds.height - mWorldView.getSize().y / 2.f);
        mScrollSpeed = -50.f;
        mPlayerAircraft = null;
        mEnemySpawnPoints = new ArrayList<>();
        mActiveEnemies = new ArrayList<>();

        mCommandQueue = new CommandQueue();

        loadTextures();
        buildScene();

        mWorldView.setCenter(mSpawnPosition);
    }

    public void update(Time dt) {
        mWorldView.move(0.f, mScrollSpeed * dt.asSeconds());
        mPlayerAircraft.setVelocity(0.f, 0.f);

        destroyEntitiesOutsideView();
        //guideMissiles();

        while (!mCommandQueue.isEmpty())
            mSceneGraph.onCommand(mCommandQueue.pop(), dt);
        adaptPlayerVelocity();

        handleCollisions();

        //mSceneGraph.removeWrecks();
        spawnEnemies();

        mSceneGraph.update(dt, mCommandQueue);
        adaptPlayerPosition();
    }

    public void draw() {
        mWindow.setView(mWorldView);
        mWindow.draw(mSceneGraph);
    }

    public CommandQueue getCommandQueue() {
        return mCommandQueue;
    }

    public boolean hasAlivePlayer() {
        return !mPlayerAircraft.isMarkedForRemoval();
    }

    public boolean hasPlayerReachedEnd() {
        //return !mWorldBounds.contains(mPlayerAircraft.getPosition());
        return !mWorldBounds.contains(Vector2f.add(mWorldView.getCenter(), Vector2f.neg(Vector2f.div(mWorldView.getSize(), 2.f))));
    }

    private void loadTextures() throws IOException {
        mTextures.load(TextureHolder.ID.EAGLE, "Media/Textures/Eagle.png");
        mTextures.load(TextureHolder.ID.RAPTOR, "Media/Textures/Raptor.png");
        mTextures.load(TextureHolder.ID.AVENGER, "Media/Textures/Avenger.png");
        mTextures.load(TextureHolder.ID.DESERT, "Media/Textures/Desert.png");

        mTextures.load(TextureHolder.ID.BULLET, "Media/Textures/Bullet.png");
        mTextures.load(TextureHolder.ID.MISSILE, "Media/Textures/Missile.png");

        mTextures.load(TextureHolder.ID.HEALTH_REFILL, "Media/Textures/HealthRefill.png");
        mTextures.load(TextureHolder.ID.MISSILE_REFILL, "Media/Textures/MissileRefill.png");
        mTextures.load(TextureHolder.ID.FIRE_SPREAD, "Media/Textures/FireSpread.png");
        mTextures.load(TextureHolder.ID.FIRE_RATE, "Media/Textures/FireRate.png");
    }

    private void adaptPlayerPosition() {
        FloatRect viewBounds = new FloatRect(Vector2f.add(mWorldView.getCenter(),
                Vector2f.neg(Vector2f.div(mWorldView.getSize(), 2.f))), mWorldView.getSize());
        final float borderDistance = 40.f;

        Vector2f position = mPlayerAircraft.getPosition();

        position = new Vector2f(Math.max(position.x,  viewBounds.left + borderDistance), position.y);
        position = new Vector2f(Math.min(position.x, viewBounds.left + viewBounds.width - borderDistance), position.y);
        position = new Vector2f(position.x, Math.max(position.y, viewBounds.top + borderDistance));
        position = new Vector2f(position.x, Math.min(position.y, viewBounds.top + viewBounds.height - borderDistance));

        mPlayerAircraft.setPosition(position);
    }

    private void adaptPlayerVelocity() {
        Vector2f velocity = mPlayerAircraft.getVelocity();

        if (velocity.x != 0.f && velocity.y != 0.f) {
            mPlayerAircraft.setVelocity(Vector2f.div(velocity, (float)Math.sqrt(2.f)));
        }

        mPlayerAircraft.accelerate(0.f, mScrollSpeed);
    }

    private void handleCollisions() {
        Set<Pair<SceneNode, SceneNode>> collisionPairs = new HashSet<>();
        mSceneGraph.checkSceneCollision(mSceneGraph, collisionPairs);

        for (Pair<SceneNode, SceneNode> pair : collisionPairs) {

            if (matchesCategories(pair, Category.PLAYER_AIRCRAFT, Category.ENEMY_AIRCRAFT)) {
                Aircraft player = (Aircraft)(pair.getKey());
                Aircraft enemy  = (Aircraft)(pair.getValue());

                player.damage(enemy.getHitpoints());
                enemy.destroy();
            } else if (matchesCategories(pair, Category.PLAYER_AIRCRAFT, Category.PICKUP)) {
                Aircraft player = (Aircraft)(pair.getKey());
                Pickup pickup = (Pickup)(pair.getValue());

                pickup.apply(player);
                pickup.destroy();
            } else if (matchesCategories(pair, Category.ENEMY_AIRCRAFT, Category.ALLIED_PROJECTILE)
                    || matchesCategories(pair, Category.PLAYER_AIRCRAFT, Category.ENEMY_PROJECTILE))
            {
                Class c = pair.getKey().getClass();
                if (c.getName() == "aircraft.Aircraft") {
                    Aircraft aircraft = (Aircraft)(pair.getKey());
                    Projectile projectile = (Projectile)(pair.getValue());

                    aircraft.damage(projectile.getDamage());
                    projectile.destroy();
                } else {
                    Aircraft aircraft = (Aircraft)(pair.getValue());
                    Projectile projectile = (Projectile)(pair.getKey());

                    aircraft.damage(projectile.getDamage());
                    projectile.destroy();
                }

            }
        }
    }

    private void buildScene() {
        for (Layer layerName : Layer.values()) {
            byte category = (layerName == Layer.AIR) ? Category.SCENE_AIR_LAYER : Category.NONE;
            SceneNode layer = new SceneNode(category);
            mSceneLayers.put(layerName, layer);
            mSceneGraph.attachChild(layer);
        }

        Texture texture = mTextures.get(TextureHolder.ID.DESERT);
        IntRect textureRect = new IntRect(mWorldBounds);
        texture.setRepeated(true);

        SpriteNode backgroundSprite = new SpriteNode(texture, textureRect);
        backgroundSprite.setPosition(mWorldBounds.left, mWorldBounds.top);
        mSceneLayers.get(Layer.BACKGROUND).attachChild(backgroundSprite);

        Aircraft player = new Aircraft(Aircraft.Type.EAGLE, mTextures, mFonts);
        mPlayerAircraft = player;
        mPlayerAircraft.setPosition(mSpawnPosition);
        mSceneLayers.get(Layer.AIR).attachChild(player);

        addEnemies();
    }

    private void addEnemies() {
        addEnemy(Aircraft.Type.RAPTOR,    0.f,  500.f);
        addEnemy(Aircraft.Type.RAPTOR,    0.f, 1000.f);
        addEnemy(Aircraft.Type.RAPTOR, +100.f, 1100.f);
        addEnemy(Aircraft.Type.RAPTOR, -100.f, 1100.f);
        addEnemy(Aircraft.Type.AVENGER, -70.f, 1400.f);
        addEnemy(Aircraft.Type.AVENGER, -70.f, 1600.f);
        addEnemy(Aircraft.Type.AVENGER,  70.f, 1400.f);
        addEnemy(Aircraft.Type.AVENGER,  70.f, 1600.f);

        mEnemySpawnPoints.sort((lhs, rhs) -> {
            if (lhs.y < rhs.y)
                return -1;
            if (lhs.y > rhs.y)
                return 1;
            return 0;
        });
    }

    private void addEnemy(Aircraft.Type type, float relX, float relY) {
        SpawnPoint spawn = new SpawnPoint(type, mSpawnPosition.x + relX, mSpawnPosition.y - relY);
        mEnemySpawnPoints.add(spawn);
    }

    private void spawnEnemies() {
        while (!mEnemySpawnPoints.isEmpty()
                && mEnemySpawnPoints.get(mEnemySpawnPoints.size() - 1).y > getBattlefieldBounds().top)
        {
            SpawnPoint spawn = mEnemySpawnPoints.get(mEnemySpawnPoints.size() - 1);

            Aircraft enemy = new Aircraft(spawn.type, mTextures, mFonts);
            enemy.setPosition(spawn.x, spawn.y);
            enemy.setRotation(180.f);

            mSceneLayers.get(Layer.AIR).attachChild(enemy);

            mEnemySpawnPoints.remove(mEnemySpawnPoints.size() - 1);
        }
    }

    private void destroyEntitiesOutsideView() {
        Command command = new Command();
        command.category = Category.PROJECTILE | Category.ENEMY_AIRCRAFT;
        command.action = Command.derivedAction((node, dt) -> {
            if (getBattlefieldBounds().intersection(((Entity)node).getBoundingRect()) != null)
                ((Entity)node).destroy();
        });
        mCommandQueue.push(command);
    }

    private void guideMissiles() {
//        Command enemyCollector = new Command();
//        enemyCollector.category = Category.ENEMY_AIRCRAFT;
//        enemyCollector.action = Command.derivedAction((node, dt) -> {
//            if (!(node).isDestroyed())
//                mActiveEnemies.add((Aircraft)node);
//        });
//
//        Command missileGuider = new Command();
//        missileGuider.category = Category.ALLIED_PROJECTILE;
//        missileGuider.action = Command.derivedAction((node, dt) -> {
//            if (!((Projectile)node).isGuided())
//            return;
//
//        float minDistance = Float.MAX_VALUE;
//        Aircraft closestEnemy = null;
//
//        for (Aircraft enemy : mActiveEnemies) {
//            float enemyDistance = SceneNode.distance(node, enemy);
//
//            if (enemyDistance < minDistance) {
//                closestEnemy = enemy;
//                minDistance = enemyDistance;
//            }
//        }
//
//        if (closestEnemy != null)
//            ((Projectile)node).guideTowards(closestEnemy.getWorldPosition());
//        });
//
//        mCommandQueue.push(enemyCollector);
//        mCommandQueue.push(missileGuider);
//        mActiveEnemies.clear();
    }

    private FloatRect getViewBounds() {
        return new FloatRect(Vector2f.add(mWorldView.getCenter(), Vector2f.neg(Vector2f.div(mWorldView.getSize(), 2.f))), mWorldView.getSize());
    }

    private FloatRect getBattlefieldBounds() {
        FloatRect bounds = getViewBounds();
        float top = bounds.top - 100.f;
        float height = bounds.height + 100.f;
        bounds = new FloatRect(bounds.left, top, bounds.width, height);
        return bounds;
    }

    private enum Layer {
        BACKGROUND,
        AIR
    }

    private class SpawnPoint {
        public SpawnPoint(Aircraft.Type type, float x, float y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        public Aircraft.Type type;
        public float x;
        public float y;
    }

    private static boolean matchesCategories(Pair<SceneNode, SceneNode> colliders, byte type1, byte type2) {
        byte category1 = colliders.getKey().getCategory();
        byte category2 = colliders.getValue().getCategory();

        if ((type1 & category1) > 0 && (type2 & category2) > 0) {
            return true;
        } else if ((type1 & category2) > 0 && (type2 & category1) > 0) {
            colliders = new Pair<>(colliders.getValue(), colliders.getKey());
            return true;
        } else {
            return false;
        }
    }

    private RenderWindow mWindow;
    private View mWorldView;
    private TextureHolder mTextures;
    private FontHolder mFonts;

    private SceneNode mSceneGraph;
    private HashMap<Layer, SceneNode> mSceneLayers;
    private CommandQueue mCommandQueue;

    private FloatRect mWorldBounds;
    private Vector2f mSpawnPosition;
    private float mScrollSpeed;
    private Aircraft mPlayerAircraft;

    private ArrayList<SpawnPoint> mEnemySpawnPoints;
    private ArrayList<Aircraft> mActiveEnemies;
}
