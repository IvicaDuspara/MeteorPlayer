package gui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.PlayerData;
import observers.GraphicalPlayerDataObserver;
import song.MP3Song;

/**
 * Displays loaded and queued songs.<br>
 * This class is a view which is backed up by {@link PlayerData}.
 * It also implements methods which handle clicks on loaded songs.<br>
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class PlayerDisplay implements GraphicalPlayerDataObserver {
    private static final int LIST_PREFERED_WIDTH = 350;

    private ListView<MP3Song> loadedSongsView;

    private ListView<MP3Song> queuedSongsView;

    private ListView<MP3Song> queriedSongsView;

    private Label nowPlaying;

    private Label nextInQueue;


    /**
     * Constructs a new {@code PlayerDisplay}.
     *
     * @param playerDataReference whose data is used as a model for views in this {@code PlayerDisplay}
     */
    public PlayerDisplay(PlayerData playerDataReference) {
        playerDataReference.addGraphicalPlayerDataObserver(this);
        this.loadedSongsView = new ListView<>(playerDataReference.getLoadedSongs());
        this.queuedSongsView = new ListView<>(playerDataReference.getQueuedSongs());
        this.queriedSongsView = new ListView<>(playerDataReference.getQueriedSongs());
        this.nowPlaying = new Label("");
        this.nextInQueue = new Label("");
        this.loadedSongsView.setOnMouseClicked(l -> {
            if(l.getClickCount() == 2) {
                MP3Song selected = loadedSongsView.getSelectionModel().getSelectedItem();
                playerDataReference.playOnClick(selected);
            }
        });
        loadedSongsView.setPrefWidth(LIST_PREFERED_WIDTH);
        this.nowPlaying.getStyleClass().add("nowPlayingLabel");
        this.nextInQueue.getStyleClass().add("queueLabel");
    }


    /**
     * Returns {@code Label} which indicates which {@link MP3Song} is playing.
     *
     * @return
     *         {@code Label} which indicates which {@link MP3Song} is playing
     */
    public Label getNowPlaying() {
        return nowPlaying;
    }


    /**
     * Returns {@code Label} which indicates which {@link MP3Song} is next in queue.
     *
     * @return
     *         {@code Label} which indicates which {@link MP3Song} is next in queue
     */
    public Label getNextInQueue() {
        return nextInQueue;
    }

    /**
     * Returns view of all loaded songs of backing model.
     *
     * @return
     *        view of all loaded songs
     *
     */
    public ListView<MP3Song> getloadedSongsView() {
        return loadedSongsView;
    }


    /**
     * Returns view of all queried songs in search.
     *
     * @return
     *         view of all queried songs in search
     */
    public ListView<MP3Song> getQueriedSongsView() {
        return queriedSongsView;
    }


    /**
     * Returns view of all queued songs of backing model.
     *
     * @return
     *         view of all queued songs of backing model
     */
    public ListView<MP3Song> getQueuedSongsView() {
        return queuedSongsView;
    }



    @Override
    public void update(int currentIndex, MP3Song currentSong, MP3Song nextInLine) {
        loadedSongsView.getSelectionModel().select(currentIndex);
        loadedSongsView.getFocusModel().focus(currentIndex);
        loadedSongsView.scrollTo(currentIndex);
        if(currentSong != null) {
            nowPlaying.setText(currentSong.toString());
        }
        if(nextInLine != null) {
            nextInQueue.setText(nextInLine.toString());
        }
        else {
            nextInQueue.setText("");
        }

    }


}
