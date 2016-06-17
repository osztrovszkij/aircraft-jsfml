package aircraft.state;

import aircraft.TextureHolder;
import aircraft.gui.Button;
import aircraft.gui.Container;
import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/12/2016.
 */
public class MenuState extends State {

    public MenuState(StateStack stack, State.Context context) {
        super(stack, context);

        mGUIContainer = new Container();
        mBackgroundSprite = new Sprite();

        Texture texture = context.textures.get(TextureHolder.ID.TITLE_SCREEN);
        mBackgroundSprite.setTexture(texture);

        Button playButton = new Button(context.fonts, context.textures);
        playButton.setPosition(100, 300);
        playButton.setText("Play");
        playButton.setCallback(() -> {
            requestStackPop();
            requestStackPush(ID.GAME);
        });

        Button settingsButton = new Button(context.fonts, context.textures);
        settingsButton.setPosition(100, 350);
        settingsButton.setText("Settings");
        settingsButton.setCallback(() -> requestStackPush(ID.SETTINGS));

        Button exitButton = new Button(context.fonts, context.textures);
        exitButton.setPosition(100, 400);
        exitButton.setText("Exit");
        exitButton.setCallback(() -> requestStackPop());

        mGUIContainer.pack(playButton);
        mGUIContainer.pack(settingsButton);
        mGUIContainer.pack(exitButton);
    }

    @Override
    public void draw() {
        RenderWindow window = getContext().window;

        window.setView(window.getDefaultView());

        window.draw(mBackgroundSprite);
        window.draw(mGUIContainer);
    }

    @Override
    public boolean update(Time dt) {
        return true;
    }

    @Override
    public boolean handleEvent(Event event) {
        mGUIContainer.handleEvent(event);
        return false;
    }

    private Sprite mBackgroundSprite;
    private Container mGUIContainer;
}
