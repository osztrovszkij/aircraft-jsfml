package aircraft.state;

import aircraft.FontHolder;
import aircraft.Utility;
import aircraft.gui.Button;
import aircraft.gui.Container;
import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/12/2016.
 */
public class PauseState extends State {
    public PauseState(StateStack stack, State.Context context) {
        super(stack, context);

        mBackgroundSprite = new Sprite();
        mPausedText = new Text();
        mGUIContainer = new Container();

        Font font = context.fonts.get(FontHolder.ID.MAIN);
        Vector2f windowSize = new Vector2f(context.window.getSize());

        mPausedText.setFont(font);
        mPausedText.setString("Game Paused");
        mPausedText.setCharacterSize(70);
        Utility.centerOrigin(mPausedText);
        mPausedText.setPosition(0.5f * windowSize.x, 0.4f * windowSize.y);

        Button returnButton = new Button(context.fonts, context.textures);
        returnButton.setPosition(0.5f * windowSize.x - 100, 0.4f * windowSize.y + 75);
        returnButton.setText("Return");
        returnButton.setCallback(() -> requestStackPop());

        Button backToMenuButton = new Button(context.fonts, context.textures);
        backToMenuButton.setPosition(0.5f * windowSize.x - 100, 0.4f * windowSize.y + 125);
        backToMenuButton.setText("Back to menu");
        backToMenuButton.setCallback(() -> {
            requestStateClear();
            requestStackPush(ID.MENU);
        });

        mGUIContainer.pack(returnButton);
        mGUIContainer.pack(backToMenuButton);
    }

    @Override
    public void draw() {
        RenderWindow window = getContext().window;
        window.setView(window.getDefaultView());

        RectangleShape backgroundShape = new RectangleShape();
        backgroundShape.setFillColor(new Color(0, 0, 0, 150));
        backgroundShape.setSize(window.getView().getSize());

        window.draw(backgroundShape);
        window.draw(mPausedText);
        window.draw(mGUIContainer);
    }

    @Override
    public boolean update(Time dt) {
        return false;
    }

    @Override
    public boolean handleEvent(Event event) {
        mGUIContainer.handleEvent(event);
        return false;
    }

    private Sprite mBackgroundSprite;
    private Text mPausedText;
    private Container mGUIContainer;
}
