package commands;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PlayerData;

import java.io.File;
import java.util.List;

/**
 * Opens a dialog window where multiple files can be selected.
 * Selected files are added to a play list if they are not already in it.
 *
 *
 * @author Ivica Duspara
 * @version 1.0
 *
 */
public class OpenCommand extends ModelCommand{

    /**
     * Used for choosing files.
     */
    private FileChooser fileChooser;

    private Stage stage;

    /**
     * Constructs a newly allocated {@code OpenCommand} with given parameters
     *
     *
     * @param playerData
     * 		  {@code PlayerData} in which selected files are stored
     *
     * @param name
     * 		  of this {@code OpenCommand}
     */
    public OpenCommand(Stage stage, PlayerData playerData, String name) {
        super(playerData,name);
        this.stage = stage;
        this.fileChooser = new FileChooser();
        setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

    }


    @Override
    public void execute() {
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        playerData.addSongs(files);

    }

}