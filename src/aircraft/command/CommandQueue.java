package aircraft.command;

import aircraft.command.Command;

import java.util.LinkedList;

/**
 * Created by roski on 5/12/2016.
 */
public class CommandQueue {
    public CommandQueue() {
        queue = new  LinkedList<>();
    }

    public void push(final Command command) {
        queue.push(command);
    }

    public Command	pop() {
        Command command = queue.getFirst();
        queue.pop();
        return command;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    private LinkedList<Command> queue;
}
