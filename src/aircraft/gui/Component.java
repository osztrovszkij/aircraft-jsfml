package aircraft.gui;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/13/2016.
 */
public abstract class Component extends BasicTransformable implements Drawable {
    public  Component() {
        mIsSelected = false;
        mIsActive = false;
    }

    public abstract boolean isSelectable();

    public boolean isSelected() {
        return mIsSelected;
    }

    public void	select() {
        mIsSelected = true;
    }

    public void	deselect() {
        mIsSelected = false;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void	activate() {
        mIsActive = true;
    }

    public void	deactivate() {
        mIsActive = false;
    }

    public abstract void handleEvent(final Event event);

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {

    }

    private boolean	mIsSelected;
    private boolean	mIsActive;
}
