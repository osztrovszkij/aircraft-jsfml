package aircraft.gui;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import java.util.ArrayList;

/**
 * Created by roski on 5/13/2016.
 */
public class Container extends Component {
    public Container() {
        mChildren = new ArrayList<>();
        mSelectedChild = -1;
    }

    public void pack(Component component) {
        mChildren.add(component);

        if (!hasSelection() && component.isSelectable())
            select(mChildren.size() - 1);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void handleEvent(Event event) {
        if (hasSelection() && mChildren.get(mSelectedChild).isActive()) {
            mChildren.get(mSelectedChild).handleEvent(event);
        } else if (event.type == Event.Type.KEY_RELEASED) {
            if (event.asKeyEvent().key == Keyboard.Key.W || event.asKeyEvent().key == Keyboard.Key.UP) {
                selectPrevious();
            } else if (event.asKeyEvent().key == Keyboard.Key.S || event.asKeyEvent().key == Keyboard.Key.DOWN) {
                selectNext();
            } else if (event.asKeyEvent().key == Keyboard.Key.RETURN || event.asKeyEvent().key == Keyboard.Key.SPACE) {
                if (hasSelection())
                    mChildren.get(mSelectedChild).activate();
            }
        }
    }

    @Override
    public  void draw(RenderTarget target, RenderStates states) {
        states = new RenderStates(Transform.combine(states.transform, getTransform()));

       for (final Component child : mChildren)
            target.draw(child, states);
    }

    private boolean	hasSelection() {
        return mSelectedChild >= 0;

    }

    private void select(int index) {
        if (mChildren.get(index).isSelectable()) {
            if (hasSelection())
                mChildren.get(mSelectedChild).deselect();

            mChildren.get(index).select();
            mSelectedChild = index;
        }
    }

    private void selectNext() {
        if (!hasSelection())
            return;

        int next = mSelectedChild;

        do {
            next = (next + 1) % mChildren.size();
        } while (!mChildren.get(next).isSelectable());

        select(next);
    }

    private void selectPrevious() {
        if (!hasSelection())
            return;

        int prev = mSelectedChild;
        do {
            prev = (prev + mChildren.size() - 1) % mChildren.size();
        } while (!mChildren.get(prev).isSelectable());

        select(prev);
    }

    private ArrayList<Component> mChildren;
    private int	mSelectedChild;
}
