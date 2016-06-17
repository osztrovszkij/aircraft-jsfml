package aircraft.gui;

import aircraft.*;
import org.jsfml.graphics.*;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/13/2016.
 */
public class Button extends Component {
    @FunctionalInterface
    public interface Callback {
        void exec();
    }

    public Button(final FontHolder fonts, final TextureHolder textures) {
        mCallback = null;
        mNormalTexture = textures.get(TextureHolder.ID.BUTTON_NORMAL);
        mSelectedTexture = textures.get(TextureHolder.ID.BUTTON_SELECTED);
        mPressedTexture = textures.get(TextureHolder.ID.BUTTON_PRESSED);
        mSprite = new Sprite();
        mText = new Text("", fonts.get(FontHolder.ID.MAIN), 16);
        mIsToggle = false;

        mSprite.setTexture(mNormalTexture);

        FloatRect bounds = mSprite.getLocalBounds();
        mText.setPosition(bounds.width / 2.f, bounds.height / 2.f);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setText(final String text) {
        mText.setString(text);
        Utility.centerOrigin(mText);
    }

    public void setToggle(boolean flag) {
        mIsToggle = flag;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void	select() {
        super.select();
        mSprite.setTexture(mSelectedTexture);
    }

    @Override
    public void	deselect() {
        super.deselect();
        mSprite.setTexture(mNormalTexture);
    }

    @Override
    public void	activate() {
        super.activate();

        if (mIsToggle)
            mSprite.setTexture(mPressedTexture);

        if (mCallback != null)
            mCallback.exec();

        if (!mIsToggle)
            deactivate();
    }

    @Override
    public void	deactivate() {
        super.deactivate();

        if (mIsToggle) {
            if (isSelected())
                mSprite.setTexture(mSelectedTexture);
            else
                mSprite.setTexture(mNormalTexture);
        }
    }

    @Override
    public void handleEvent(Event event) {

    }

    @Override
    public void draw(RenderTarget target, RenderStates states) {
        states = new RenderStates(Transform.combine(states.transform, getTransform()));
        target.draw(mSprite, states);
        target.draw(mText, states);
    }

    private Callback mCallback;
    private Texture mNormalTexture;
    private Texture mSelectedTexture;
    private Texture mPressedTexture;
    private Sprite mSprite;
    private Text mText;
    private boolean	mIsToggle;
}
