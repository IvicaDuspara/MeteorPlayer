package commands;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import model.PlayerData;

/**
 * Switches player layout to light mode.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class LightSkinCommand extends Command {

    private Scene scene;

    public LightSkinCommand(Stage stage, PlayerData playerData, String commandName, Scene scene) {
        super(stage, playerData, commandName);
        this.scene = scene;
        setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
    }

    @Override
    public void executeCommand() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add("light.css");
    }
}
