package commands;

import javafx.scene.control.MenuItem;
import model.PlayerData;

/**
 * Models an abstract command which makes changes on a model such as opening songs or starting a network interface.<br>
 *
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public abstract class ModelCommand extends MenuItem{

    /**
     * PlayerData reference.
     */
    PlayerData playerData;

    /**
     * Constructs new {@code ModelCommand} with given parameters
     *
     * @param playerData
     *        model on which command is executed
     *
     * @param name
     *        of this {@code ModelCommand}
     */
    ModelCommand(PlayerData playerData, String name) {
        super(name);
        this.playerData = playerData;
        this.setOnAction(e -> execute());
    }

    public abstract void execute();
}
