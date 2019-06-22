package commands;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import model.PlayerData;

/**
 * Switches player layout to dark mode.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class DarkSkinCommand extends Command {

    private Scene scene;

    public DarkSkinCommand(Stage stage, PlayerData playerData, String commandName, Scene scene) {
        super(stage, playerData, commandName);
        this.scene = scene;
        setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
    }

    @Override
    public void executeCommand() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add("matrix.css");
    }
}
