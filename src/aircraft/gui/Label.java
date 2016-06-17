package aircraft.gui;

import aircraft.FontHolder;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/13/2016.
 */
public class Label extends Component {
    public Label(final String text, final FontHolder fonts) {
        mText = new Text(text, fonts.get(FontHolder.ID.MAIN), 16);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    public void setText(final String text) {
        mText.setString(text);
    }

    @Override
    public void handleEvent(Event event) {

    }

    @Override
    public void draw(RenderTarget target, RenderStates states) {
        states = new RenderStates(Transform.combine(states.transform, getTransform()));
        target.draw(mText, states);
    }

    private Text mText;
}
