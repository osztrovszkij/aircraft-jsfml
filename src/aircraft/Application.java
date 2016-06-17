package aircraft;

import aircraft.state.State;
import aircraft.state.StateStack;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.VideoMode;
import org.jsfml.window.Window;
import org.jsfml.window.event.Event;

import java.io.IOException;

/**
 * Created by roski on 5/12/2016.
 */
public class Application {
    public Application() throws IOException {
        mWindow = new RenderWindow(new VideoMode(1024, 768), "Aircraft", Window.CLOSE);
        mTextures = new TextureHolder();
        mFonts = new FontHolder();
        mPlayer = new Player();
        mStateStack = new StateStack(new State.Context(mWindow, mTextures, mFonts, mPlayer));
        mStatisticsText = new Text();
        mStatisticsUpdateTime = Time.ZERO;
        mStatisticsNumFrames = 0;

        mWindow.setKeyRepeatEnabled(false);

        mFonts.load(FontHolder.ID.MAIN, "Media/Sansation.ttf");

        mTextures.load(TextureHolder.ID.TITLE_SCREEN,	 "Media/Textures/TitleScreen.png");
        mTextures.load(TextureHolder.ID.BUTTON_NORMAL,	 "Media/Textures/ButtonNormal.png");
        mTextures.load(TextureHolder.ID.BUTTON_SELECTED, "Media/Textures/ButtonSelected.png");
        mTextures.load(TextureHolder.ID.BUTTON_PRESSED,	 "Media/Textures/ButtonPressed.png");

        mStatisticsText.setFont(mFonts.get(FontHolder.ID.MAIN));
        mStatisticsText.setPosition(5.f, 5.f);
        mStatisticsText.setCharacterSize(10);

        mStateStack.pushState(State.ID.TITLE);
    }

    public void run() throws IOException {
        Clock clock = new Clock();
        Time timeSinceLastUpdate = Time.ZERO;

        while (mWindow.isOpen()) {
            Time elapsedTime = clock.restart();
            timeSinceLastUpdate = Time.add(timeSinceLastUpdate, elapsedTime);

            while (timeSinceLastUpdate.compareTo(TimePerFrame) > 0) {
                timeSinceLastUpdate = Time.sub(timeSinceLastUpdate, TimePerFrame);

                processInput();
                update(TimePerFrame);

                if (mStateStack.isEmpty()) {
                    mWindow.close();
                }
            }

            updateStatistics(elapsedTime);
            render();
        }
    }

    private void processInput() throws IOException {
        Event event;

        while ((event = mWindow.pollEvent()) != null) {
            mStateStack.handleEvent(event);

            if (event.type == Event.Type.CLOSED) {
                mWindow.close();
            }
        }
    }

    private void update(Time dt) throws IOException {
        mStateStack.update(dt);
    }

    private void render() {
        mWindow.clear();

        mStateStack.draw();

        mWindow.setView(mWindow.getDefaultView());
        mWindow.draw(mStatisticsText);

        mWindow.display();
    }

    private void updateStatistics(Time dt) {
        mStatisticsUpdateTime = Time.add(mStatisticsUpdateTime, dt);
        mStatisticsNumFrames += 1;

        if (mStatisticsUpdateTime.compareTo(Time.getSeconds(1.0f)) >= 0) {
            mStatisticsText.setString(
                    "Frames / Second = " + mStatisticsNumFrames + "\n" +
                            "Time / Update = " + (mStatisticsUpdateTime.asMicroseconds() / mStatisticsNumFrames) + "us");

            mStatisticsUpdateTime = Time.sub(mStatisticsUpdateTime, Time.getSeconds(1.0f));
            mStatisticsNumFrames = 0;
        }
    }

    private static final Time TimePerFrame = Time.getSeconds(1.f/60.f);

    private RenderWindow mWindow;
    private TextureHolder mTextures;
    private FontHolder mFonts;
    private Player mPlayer;

    private StateStack mStateStack;

    private Text mStatisticsText;
    private Time mStatisticsUpdateTime;
    private int mStatisticsNumFrames;
}
