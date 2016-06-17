package aircraft.state;

import aircraft.FontHolder;
import aircraft.Player;
import aircraft.Utility;
import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

/**
 * Created by roski on 5/13/2016.
 */
public class GameOverState extends State {
    public GameOverState(StateStack stack, Context context) {
        super(stack, context);

        mGameOverText = new Text();
        mElapsedTime = Time.ZERO;

        Font font = context.fonts.get(FontHolder.ID.MAIN);
        Vector2f windowSize = new Vector2f(context.window.getSize());

        mGameOverText.setFont(font);

        if (context.player.getMissionStatus() == Player.MissionStatus.MISSION_FAILURE)
            mGameOverText.setString("Mission failed!");
        else
            mGameOverText.setString("Mission successful!");

        mGameOverText.setCharacterSize(70);
        Utility.centerOrigin(mGameOverText);
        mGameOverText.setPosition(0.5f * windowSize.x, 0.4f * windowSize.y);
    }

    @Override
    public void draw() {
        RenderWindow window = getContext().window;
        window.setView(window.getDefaultView());

        RectangleShape backgroundShape = new RectangleShape();
        backgroundShape.setFillColor(new Color(0, 0, 0, 150));
        backgroundShape.setSize(window.getView().getSize());

        window.draw(backgroundShape);
        window.draw(mGameOverText);
    }

    @Override
    public boolean update(Time dt) {
        mElapsedTime = Time.add(mElapsedTime, dt);
        if (mElapsedTime.asMicroseconds() > Time.getSeconds(3).asMicroseconds()) {
            requestStateClear();
            requestStackPush(ID.MENU);
        }
        return false;
    }

    @Override
    public boolean handleEvent(Event event) {
        return false;
    }

    private Text mGameOverText;
    private Time mElapsedTime;
}
