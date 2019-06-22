package commands;

import java.util.Objects;

import model.PlayerData;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


/**
 * Wrapper class for a {@link MenuItem}.<br>
 * Each {@code Command} object has an command assigned to it upon instantiation.
 *
 *
 * @author Ivica Duspara
 * @version 1.0
 *
 */
public abstract class Command extends MenuItem{

    /**
     * {@code Stage} which contains this {@code Command}.
     */
    Stage stage;

    /**
     * {@code PlayerData} which this command uses.
     */
    PlayerData playerData;

    /**
     * Constructs a newly allocated {@code Command} with given parameters.
     *
     * @param s {@code Stage} which this {@code Command} uses
     *
     * @param p {@link PlayerData} which this {@code Command} uses
     *
     * @param commandName Name of a command
     *
     * @throws NullPointerException if any of given parameters is {@code null}
     */
    Command(Stage s, PlayerData p, String commandName) {
        super(commandName);
        Objects.requireNonNull(s);
        Objects.requireNonNull(p);
        this.stage = s;
        this.playerData = p;
        this.setOnAction((e) -> executeCommand());
    }


    /**
     * Executes an action assigned to this {@code Command} object.<br>
     * Formally, this method is set as a {@link MenuItem#onActionProperty() onActionProperty} of this {@code Command}.
     */
    public abstract void executeCommand();
}