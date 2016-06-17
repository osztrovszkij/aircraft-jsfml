package aircraft.state;

import aircraft.command.CommandQueue;
import aircraft.Player;
import aircraft.World;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import java.io.IOException;

/**
 * Created by roski on 5/12/2016.
 */
public class GameState extends State {
    public GameState(StateStack stack, State.Context context) throws IOException {
        super(stack, context);
        mWorld = new World(context.window, context.fonts);
        mPlayer = context.player;

        mPlayer.setMissionStatus(Player.MissionStatus.MISSION_RUNNING);
    }

    @Override
    public void draw() {
        mWorld.draw();
    }

    @Override
    public boolean update(Time dt) {
        mWorld.update(dt);

        if(!mWorld.hasAlivePlayer()) {
            mPlayer.setMissionStatus(Player.MissionStatus.MISSION_FAILURE);
            requestStackPush(ID.GAME_OVER);
        } else if(mWorld.hasPlayerReachedEnd()) {
            mPlayer.setMissionStatus(Player.MissionStatus.MISSION_SUCCESS);
            requestStackPush(ID.GAME_OVER);
        }

        CommandQueue commands = mWorld.getCommandQueue();
        mPlayer.handleRealtimeInput(commands);

        return true;
    }

    @Override
    public boolean handleEvent(Event event) {
        CommandQueue commands = mWorld.getCommandQueue();
        mPlayer.handleEvent(event, commands);

        if (event.type == Event.Type.KEY_PRESSED && event.asKeyEvent().key == Keyboard.Key.ESCAPE)
            requestStackPush(ID.PAUSE);

        return true;
    }

    private World mWorld;
    private Player mPlayer;
}
