package aircraft.command;

import aircraft.SceneNode;
import aircraft.command.Category;
import org.jsfml.system.Time;

/**
 * Created by roski on 5/12/2016.
 */
public class Command {
    public Command() {
        action = null;
        category = Category.NONE;
    }

    @FunctionalInterface
    public interface CommandAction<T extends SceneNode> {
        void exec(T node, Time dt);
    }

    public static <Function extends CommandAction> CommandAction derivedAction(Function fn) {
        return (SceneNode node, Time dt) -> {
            fn.exec((node), dt);
        };
    }

    public CommandAction action;
    public byte category;
}
