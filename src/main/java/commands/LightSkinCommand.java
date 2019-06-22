package commands;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PlayerData;

/**
 *
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class LightSkinCommand extends Command {

    private Scene scene;

    LightSkinCommand(Stage stage, PlayerData playerData, String commandName, Scene scene) {
        super(stage, playerData, commandName);
        this.scene = scene;
    }

    @Override
    public void executeCommand() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add("light.css");
    }
}
