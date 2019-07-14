package commands;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Switches player layout to light mode.
 * Light mode attributes are defined in resources/light.css
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class LightSkinCommand extends SceneCommand {


    /**
     * Constructs a new {@code LightSkinCommand} with given parameters
     *
     * @param scene
     *        which is changed
     *
     * @param name
     *        name of this command
     */
    public LightSkinCommand(Scene scene, String name) {
        super(scene,name);
        setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
    }

    @Override
    public void execute() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add("light.css");
    }
}
