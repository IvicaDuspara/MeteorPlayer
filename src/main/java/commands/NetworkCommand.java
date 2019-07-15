package commands;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import model.PlayerData;

/**
 * Starts network broadcast of {@link PlayerData}
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class NetworkCommand extends ModelCommand {

    /**
     * Constructs a new {@code NetworkCommand} with given parameters
     *
     * @param playerData
     *        whose broadcast is started
     *
     * @param commandName
     *        name of this command
     */
    public NetworkCommand(PlayerData playerData, String commandName) {
        super(playerData, commandName);
        setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));

    }


    @Override
    public void execute() {
        playerData.startBroadcast();
    }

}
