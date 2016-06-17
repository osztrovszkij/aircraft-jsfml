package aircraft;

import aircraft.command.Category;
import aircraft.command.Command;
import aircraft.command.CommandQueue;
import javafx.util.Pair;
import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


/**
 * Created by roski on 5/11/2016.
 */
public class SceneNode extends BasicTransformable implements Drawable {
    public SceneNode(byte category) {
        mChildren = new ArrayList<SceneNode>();
        mParent = null;
        mDefaultCategory = category;
    }

    public void	attachChild(SceneNode child) {
        child.mParent = this;
        mChildren.add(child);
    }

    public SceneNode detachChild(final SceneNode node) {
        int found = mChildren.indexOf(node);
        SceneNode result = mChildren.get(found);
        result.mParent = null;
        mChildren.remove(found);
        return result;
    }

    public void	update(Time dt, CommandQueue commands) {
//        for (SceneNode child : mChildren) {
//            System.out.println(child.getClass().getName());
//        }
        updateCurrent(dt, commands);
        updateChildren(dt, commands);
    }

    public Vector2f getWorldPosition() {
        return getWorldTransform().transformPoint(new Vector2f(0, 0));
    }

    public Transform getWorldTransform() {
        Transform transform = Transform.IDENTITY;
        for (SceneNode node = this; node != null; node = node.mParent) {
            transform = Transform.combine(node.getTransform(), transform);
        }
        return transform;
    }

    public void onCommand(final Command command, Time dt) {
        if (command.category == getCategory()) {
            command.action.exec(this, dt);
        }

        for (SceneNode child : mChildren) {
            child.onCommand(command, dt);
        }
    }

    public byte getCategory() {
        return mDefaultCategory;
    }


    public void	checkSceneCollision(SceneNode sceneGraph, Set<Pair<SceneNode, SceneNode>> collisionPairs) {
        checkNodeCollision(sceneGraph, collisionPairs);

        for (SceneNode child : sceneGraph.mChildren)
            checkSceneCollision(child, collisionPairs);
    }

    public void	checkNodeCollision(SceneNode node, Set<Pair<SceneNode, SceneNode>> collisionPairs) {
        if (this != node && collision(this, node) && !isDestroyed() && !node.isDestroyed())
            collisionPairs.add(Utility.minmax(this, node));

        for (SceneNode child : mChildren)
            child.checkNodeCollision(node, collisionPairs);
    }

    public void	removeWrecks() {
        mChildren.removeIf(SceneNode::isMarkedForRemoval);

        for (SceneNode child : mChildren) {
            if (child.isMarkedForRemoval()) {
                child.removeWrecks();
            }
        }
    }

    public FloatRect getBoundingRect() {
        return new FloatRect(0, 0, 0, 0);
    }

    public boolean isMarkedForRemoval() {
        return isDestroyed();
    }

    public boolean isDestroyed() {
        return false;
    }

    protected void updateCurrent(Time dt, CommandQueue commands) {
        // Do nothing by default
        removeWrecks();
    }

    private void updateChildren(Time dt, CommandQueue commands) {
        for (SceneNode child : mChildren) {
            child.update(dt, commands);
        }
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderStates = new RenderStates(Transform.combine(renderStates.transform, getTransform()));
        drawCurrent(renderTarget, renderStates);
        drawChildren(renderTarget, renderStates);
    }

    protected void drawCurrent(RenderTarget target, RenderStates states) {
        // Do nothing by default
    }

    private void drawChildren(RenderTarget target, RenderStates states) {
        for (SceneNode child : mChildren) {
            child.draw(target, states);
        }
    }

    private void drawBoundingRect(RenderTarget target, RenderStates states) {
        FloatRect rect = getBoundingRect();

        RectangleShape shape = new RectangleShape();
        shape.setPosition(new Vector2f(rect.left, rect.top));
        shape.setSize(new Vector2f(rect.width, rect.height));
        shape.setFillColor(Color.TRANSPARENT);
        shape.setOutlineColor(Color.GREEN);
        shape.setOutlineThickness(1.f);

        target.draw(shape);
    }

    public static boolean collision(final SceneNode lhs, final SceneNode rhs) {
        FloatRect rect = lhs.getBoundingRect().intersection(rhs.getBoundingRect());

        if (rect == null)
            return false;
        else
            return true;
    }

    public static float	distance(final SceneNode lhs, final SceneNode rhs) {
        return Utility.length(Vector2f.add(lhs.getWorldPosition(), Vector2f.neg(rhs.getWorldPosition())));
    }

    private ArrayList<SceneNode> mChildren;
    private SceneNode mParent;
    byte mDefaultCategory;
}
