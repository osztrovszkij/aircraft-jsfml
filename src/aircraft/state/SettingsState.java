package aircraft.state;

import aircraft.Player;
import aircraft.TextureHolder;
import aircraft.gui.Button;
import aircraft.gui.Container;
import aircraft.gui.Label;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import java.util.ArrayList;

/**
 * Created by roski on 5/13/2016.
 */
public class SettingsState extends State {
    public SettingsState(StateStack stack, State.Context context) {
        super(stack, context);

        mBindingButtons = new ArrayList<>();
        mBindingLabels = new ArrayList<>();
        mGUIContainer = new Container();
        mBackgroundSprite = new Sprite();

        mBackgroundSprite.setTexture(context.textures.get(TextureHolder.ID.TITLE_SCREEN));

        addButtonLabel(Player.Action.MOVE_LEFT,   300.f, "Move Left",  context);
        addButtonLabel(Player.Action.MOVE_RIGHT,  350.f, "Move Right", context);
        addButtonLabel(Player.Action.MOVE_UP,     400.f, "Move Up",    context);
        addButtonLabel(Player.Action.MOVE_DOWN,   450.f, "Move Down",  context);
        addButtonLabel(Player.Action.FIRE,			 500.f, "Fire",    context);
        addButtonLabel(Player.Action.LAUNCH_MISSILE, 550.f, "Missile", context);

        updateLabels();

        Button backButton = new Button(context.fonts, context.textures);
        backButton.setPosition(80.f, 620.f);
        backButton.setText("Back");
        backButton.setCallback(() -> requestStackPop());

        mGUIContainer.pack(backButton);
    }

    @Override
    public void draw() {
        RenderWindow window = getContext().window;

        window.draw(mBackgroundSprite);
        window.draw(mGUIContainer);
    }

    @Override
    public boolean update(Time dt) {
        return true;
    }

    @Override
    public boolean handleEvent(Event event) {
        boolean isKeyBinding = false;

        for (Player.Action action : Player.Action.values()) {
            if (mBindingButtons.get(action.ordinal()).isActive()) {
                isKeyBinding = true;

                if (event.type == Event.Type.KEY_RELEASED) {
                    getContext().player.assignKey(action, event.asKeyEvent().key);
                    mBindingButtons.get(action.ordinal()).deactivate();
                }

                break;
            }
        }

        if (isKeyBinding)
            updateLabels();
        else
            mGUIContainer.handleEvent(event);

        return false;
    }

    private void updateLabels() {
        Player player = getContext().player;

        for (Player.Action action : Player.Action.values()) {
            Keyboard.Key key = player.getAssignedKey(action);
            mBindingLabels.get(action.ordinal()).setText(key.name());
        }
    }

    private void addButtonLabel(Player.Action action, float y, final String text, Context context) {
        mBindingButtons.add(action.ordinal(), new Button(context.fonts, context.textures));
        mBindingButtons.get(action.ordinal()).setPosition(80.f, y);
        mBindingButtons.get(action.ordinal()).setText(text);
        mBindingButtons.get(action.ordinal()).setToggle(true);

        mBindingLabels.add(action.ordinal(), new Label("", context.fonts));
        mBindingLabels.get(action.ordinal()).setPosition(300.f, y + 15.f);

        mGUIContainer.pack(mBindingButtons.get(action.ordinal()));
        mGUIContainer.pack(mBindingLabels.get(action.ordinal()));
    }

    private Sprite mBackgroundSprite;
    private Container mGUIContainer;
    private ArrayList<Button>  mBindingButtons;
    private ArrayList<Label> mBindingLabels;
}
