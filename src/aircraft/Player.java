package aircraft;

import aircraft.command.Category;
import aircraft.command.Command;
import aircraft.command.CommandQueue;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roski on 5/12/2016.
 */
public class Player {
    public enum Action {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        FIRE,
        LAUNCH_MISSILE
    }

    public enum MissionStatus {
        MISSION_RUNNING,
        MISSION_SUCCESS,
        MISSION_FAILURE
    }

    public class AircraftMover implements Command.CommandAction<Aircraft> {
        public AircraftMover(float x, float y) {
            velocity = new Vector2f(x, y);
        }

        @Override
        public void exec(Aircraft aircraft, Time dt) {
            aircraft.accelerate(Vector2f.mul(velocity, aircraft.getMaxSpeed()));
        }

        private Vector2f velocity;
    }

    public Player() {
        mKeyBinding = new HashMap<>();
        mActionBinding = new HashMap<>();
        mCurrentMissionStatus = MissionStatus.MISSION_RUNNING;

        mKeyBinding.put(Keyboard.Key.LEFT,  Action.MOVE_LEFT);
        mKeyBinding.put(Keyboard.Key.RIGHT, Action.MOVE_RIGHT);
        mKeyBinding.put(Keyboard.Key.UP,    Action.MOVE_UP);
        mKeyBinding.put(Keyboard.Key.DOWN,  Action.MOVE_DOWN);
        mKeyBinding.put(Keyboard.Key.SPACE, Action.FIRE);
        mKeyBinding.put(Keyboard.Key.M,     Action.LAUNCH_MISSILE);

        initializeActions();

        for (Map.Entry<Action, Command> entry : mActionBinding.entrySet()) {
            entry.getValue().category = Category.PLAYER_AIRCRAFT;
        }
    }

    public void handleEvent(final Event event, CommandQueue commands) {
        if (event.type == Event.Type.KEY_PRESSED) {
            Action found =  mKeyBinding.get(event.asKeyEvent().key);

            if (found != null && !isRealtimeAction(found)) {
                commands.push(mActionBinding.get(found));
            }
        }
    }

    public void handleRealtimeInput(CommandQueue commands) {
        for (Map.Entry<Keyboard.Key, Action> entry : mKeyBinding.entrySet()) {
            if (Keyboard.isKeyPressed(entry.getKey()) && isRealtimeAction(entry.getValue())) {
                commands.push(mActionBinding.get(entry.getValue()));
            }
        }
    }

    public void assignKey(Action action, Keyboard.Key key) {
        for (Map.Entry<Keyboard.Key, Action> entry : mKeyBinding.entrySet()) {
            if (entry.getValue() == action) {
                mKeyBinding.remove(entry.getKey());
                break;
            }
        }

        mKeyBinding.put(key, action);
    }

    public Keyboard.Key getAssignedKey(Action action) {
        for (Map.Entry<Keyboard.Key, Action> entry : mKeyBinding.entrySet()) {
            if (entry.getValue() == action) {
                return entry.getKey();
            }
        }

        return Keyboard.Key.UNKNOWN;
    }

    public void setMissionStatus(MissionStatus status) {
        mCurrentMissionStatus = status;
    }

    public MissionStatus getMissionStatus() {
        return mCurrentMissionStatus;
    }

    private void initializeActions() {
        final float playerSpeed = 20.f;
        mActionBinding.put(Action.MOVE_LEFT, new Command());
        mActionBinding.put(Action.MOVE_RIGHT, new Command());
        mActionBinding.put(Action.MOVE_UP, new Command());
        mActionBinding.put(Action.MOVE_DOWN, new Command());
        mActionBinding.put(Action.FIRE, new Command());
        mActionBinding.put(Action.LAUNCH_MISSILE, new Command());

        mActionBinding.get(Action.MOVE_LEFT).action  = Command.derivedAction(new AircraftMover(-playerSpeed, 0.f));
        mActionBinding.get(Action.MOVE_RIGHT).action = Command.derivedAction(new AircraftMover(+playerSpeed, 0.f));
        mActionBinding.get(Action.MOVE_UP).action    = Command.derivedAction(new AircraftMover(0.f, -playerSpeed));
        mActionBinding.get(Action.MOVE_DOWN).action  = Command.derivedAction(new AircraftMover(0.f, +playerSpeed));

        mActionBinding.get(Action.FIRE).action = Command.derivedAction((node, dt) -> ((Aircraft)node).fire());
        mActionBinding.get(Action.LAUNCH_MISSILE).action = Command.derivedAction((node, dt) -> ((Aircraft)node).launchMissile());
    }

    private static boolean isRealtimeAction(Action action) {
        switch (action) {
            case MOVE_LEFT:
            case MOVE_RIGHT:
            case MOVE_UP:
            case MOVE_DOWN:
            case FIRE:
                return true;
            default:
                return false;
        }
    }

    private HashMap<Keyboard.Key, Action> mKeyBinding;
    private HashMap<Action, Command> mActionBinding;
    private MissionStatus mCurrentMissionStatus;
}
