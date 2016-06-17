package aircraft;

import aircraft.command.Category;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;

/**
 * Created by roski on 5/13/2016.
 */
public class TextNode extends SceneNode{
    public TextNode(final FontHolder fonts, final String text) {
        super(Category.NONE);

        mText = new Text();
        mText.setFont(fonts.get(FontHolder.ID.MAIN));
        mText.setCharacterSize(20);
        setString(text);
    }

    public void setString(final String text) {
        mText.setString(text);
        Utility.centerOrigin(mText);
    }

    @Override
    protected void drawCurrent(RenderTarget target, RenderStates states) {
        target.draw(mText, states);
    }

    private Text mText;
}
