package aircraft.state;

import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by roski on 5/12/2016.
 */
public class StateStack {
    public enum Action {
        PUSH,
        POP,
        CLEAR
    }

    public StateStack(State.Context context) {
        mStack = new LinkedList<>();
        mPendingList = new  ArrayList<>();
        mContext = context;
    }

    public void	update(Time dt) throws IOException {
        for (int i = 0; i < mStack.size(); i++) {
            if (!mStack.get(i).update(dt)) {
                break;
            }
        }

        applyPendingChanges();
    }

    public void	draw() {
        for (int i = mStack.size() - 1; i >= 0; i--) {
            mStack.get(i).draw();
        }
    }

    public void	handleEvent(final Event event) throws IOException {
        for (int i = 0; i < mStack.size(); i++) {
            if (!mStack.get(i).handleEvent(event)) {
                break;
            }
        }

        applyPendingChanges();
    }

    public void	pushState(State.ID stateID) {
        mPendingList.add(new PendingChange(Action.PUSH, stateID));
    }

    public void	popState() {
        mPendingList.add(new PendingChange(Action.POP, State.ID.NONE));
    }

    public void	clearStates() {
        mPendingList.add(new PendingChange(Action.CLEAR, State.ID.NONE));
    }

    public boolean isEmpty() {
        return mStack.isEmpty();
    }

    private State createState(State.ID stateID) throws IOException {
        switch (stateID) {
            case TITLE:
                return new TitleState(this, mContext);
            case MENU:
                return new MenuState(this, mContext);
            case GAME:
                return new GameState(this, mContext);
            case PAUSE:
                return new PauseState(this, mContext);
            case SETTINGS:
                return new SettingsState(this, mContext);
            case GAME_OVER:
                return new GameOverState(this, mContext);
            default:
                return null;
        }
    }

    private void applyPendingChanges() throws IOException {
        for (PendingChange change : mPendingList) {
            switch (change.action) {
                case PUSH:
                    mStack.push(createState(change.stateID));
                    break;

                case POP:
                    mStack.pop();
                    break;

                case CLEAR:
                    mStack.clear();
                    break;
            }
        }

        mPendingList.clear();
    }

    private class PendingChange {
        public PendingChange(Action action, State.ID stateID) {
            this.action = action;
            this.stateID = stateID;
        }

        public Action action;
        public State.ID stateID;
    }

    private LinkedList<State> mStack;
    private ArrayList<PendingChange> mPendingList;

    private State.Context mContext;
}
