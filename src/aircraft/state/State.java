package aircraft.state;

import aircraft.Aircraft;
import aircraft.FontHolder;
import aircraft.Player;
import aircraft.TextureHolder;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/12/2016.
 */
public abstract class State {
    public static class Context {
        public Context(RenderWindow window, TextureHolder textures, FontHolder fonts, Player player) {
            this.window = window;
            this.textures = textures;
            this.fonts = fonts;
            this.player = player;
        }

        public RenderWindow window;
        public TextureHolder textures;
        public FontHolder fonts;
        public Player player;
    }

    public enum ID {
        NONE,
        TITLE,
        MENU,
        GAME,
        PAUSE,
        SETTINGS,
        GAME_OVER
    }

    public State(StateStack stack, Context context) {
        mStack = stack;
        mContext = context;
    }

    public abstract void draw();
    public abstract boolean	update(Time dt);
    public abstract boolean	handleEvent(final Event event);

    protected void requestStackPush(State.ID stateID) {
        mStack.pushState(stateID);
    }

    protected void requestStackPop() {
        mStack.popState();
    }

    protected void requestStateClear() {
        mStack.clearStates();
    }

    protected Context getContext() {
        return mContext;
    }

    private StateStack mStack;
    private Context mContext;
}
