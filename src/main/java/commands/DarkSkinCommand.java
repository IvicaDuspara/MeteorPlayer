package commands;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Switches player layout to dark mode.
 * Dark mode attributes are defined in /resources/matrix.css
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class DarkSkinCommand extends SceneCommand {


    /**
     * Constructs a new {@code DarkSkinCommand} with given parameters
     *
     * @param scene
     *        which is changed
     *
     * @param name
     *        name of this command
     */
    public DarkSkinCommand(Scene scene, String name) {
        super(scene, name);
        setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
    }

    @Override
    public void execute() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add("matrix.css");
    }
}
