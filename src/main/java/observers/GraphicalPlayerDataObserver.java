package observers;

import javafx.collections.ObservableList;
import javafx.util.Duration;
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
     * Notifies observer to swap element at {@code index} with {@code song}
     *
     * @param song
     *        which is inserted at {@code index}
     *
     * @param index
     *        at which {@code song} is inserted
     */
    void swapQueuedSongs(MP3Song song, int index);


    /**
     * Notifies observer to update text because of change in queue
     */
    void textNotify();


    /**
     * Updates server information with address at which {@code PlayerData}'s server is running.
     */
    void updateServerInfo();

    /**
     * Sends a request to {@link PlayerDisplayObserver observers} to update progress bar based
     * on currentTime and totalTime.
     *
     * @param currentTime
     *        of media which is currently playing
     *
     * @param totalTime
     *        of media which is currently playing
     */
    void updateTimeProperty(Duration currentTime, Duration totalTime);
}
