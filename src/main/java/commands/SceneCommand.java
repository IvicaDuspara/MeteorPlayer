package commands;

import javafx.scene.Scene;
import javafx.scene.control.MenuItem;


public abstract class SceneCommand extends MenuItem{

    /**
     * Scene reference
     */
    protected Scene scene;


    /**
     * Constructs a new {@code SceneCommand} with given parameters.
     *
     * @param scene
     *        which is changed
     *
     * @param name
     *        of this {@code SceneCommand}
     */
    SceneCommand(Scene scene, String name) {
        super(name);
        this.scene = scene;
        this.setOnAction(e -> execute());
    }

    /**
     * Executes this {@code SceneCommand}
     */
    public abstract void execute();
}
