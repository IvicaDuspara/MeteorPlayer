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
public class OpenCommand extends Command{

    /**
     * Used for choosing files.
     */
    private FileChooser fileChooser;


    /**
     * Constructs a newly allocated {@code OpenCommand} with given parameters
     *
     * @param stage
     * 		  {@code Stage} to which this {@code OpenCommand} is assigned
     *
     * @param playerData
     * 		  {@code PlayerData} in which selected files are stored
     *
     * @param commandName
     * 		  Name of this {@code OpenCommand}
     */
    public OpenCommand(Stage stage, PlayerData playerData, String commandName) {
        super(stage,playerData,commandName);
        this.fileChooser = new FileChooser();
        setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

    }


    @Override
    public void executeCommand() {
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        playerData.addSongs(files);

    }

}