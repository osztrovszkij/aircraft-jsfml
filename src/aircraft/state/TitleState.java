package aircraft.state;

import aircraft.FontHolder;
import aircraft.TextureHolder;
import aircraft.Utility;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/12/2016.
 */
public class TitleState extends State {
    public TitleState(StateStack stack, State.Context context) {
        super(stack, context);

        mBackgroundSprite = new Sprite();
        mText = new Text();
        mShowText = true;
        mTextEffectTime = Time.ZERO;

        mBackgroundSprite.setTexture(context.textures.get(TextureHolder.ID.TITLE_SCREEN));

        mText.setFont(context.fonts.get(FontHolder.ID.MAIN));
        mText.setString("Press any key to start");
        Utility.centerOrigin(mText);
        mText.setPosition(Vector2f.div(context.window.getView().getSize(), 2.f));
    }

    @Override
    public void draw() {
        RenderWindow window = getContext().window;
        window.draw(mBackgroundSprite);

        if (mShowText)
            window.draw(mText);
    }

    @Override
    public boolean update(Time dt) {
        mTextEffectTime = Time.add(mTextEffectTime, dt);

        if (mTextEffectTime.asMicroseconds() >= Time.getSeconds(0.5f).asMicroseconds()) {
            mShowText = !mShowText;
            mTextEffectTime = Time.ZERO;
        }

        return true;
    }

    @Override
    public boolean handleEvent(Event event) {
        if (event.type == Event.Type.KEY_PRESSED) {
            requestStackPop();
            requestStackPush(ID.MENU);
        }

        return true;
    }

    private Sprite mBackgroundSprite;
    private Text mText;

    private boolean	mShowText;
    private Time mTextEffectTime;
}
