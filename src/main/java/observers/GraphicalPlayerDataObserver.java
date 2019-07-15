package observers;

import javafx.collections.ObservableList;
import song.MP3Song;

/**
 * An observer interface which is interested in changes in model, such as song change or a queue change.<br>
 * Model which broadcasts this information is {@link model.PlayerData PlayerData}.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface GraphicalPlayerDataObserver {

    /**
     * Updates PlayerDisplay with given parameters.
     *
     * @param currentIndex
     *        index of song currently playing
     *
     * @param currentSong
     *        song which is currently playing.
     *
     * @param nextInLine
     *        song which is next in queue for playing.
     *
     */
    void update(int currentIndex, MP3Song currentSong, MP3Song nextInLine);


    /**
     * Notifies PlayerDisplay to update displayed queued songs
     *
     * @param songs
     *        which will be displayed in ListView
     */
    void updateQueuedSongs(ObservableList<MP3Song> songs);

}
