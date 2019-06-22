package gui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
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

    private SongInformation songInformation;


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
        this.songInformation = new SongInformation();
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


    /**
     * Returns a grid containing meta-data information on currently playing {@link MP3Song}.
     *
     * @return
     *         a grid containing meta-data information on currently playing {@link MP3Song}
     */
    public SongInformation getSongInformation() {
        return songInformation;
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
        this.songInformation.updateSongInformation(currentSong);
    }


    /**
     * Represents a simple grid which holds information on meta data of {@link MP3Song song} which
     * is currently played.<br>
     *
     * This data includes:
     * <ul>
     *     <li>Name of an artist</li>
     *     <li>Title of a song</li>
     *     <li>Album of a song</li>
     *     <li>Genre of a song</li>
     *     <li>Year of release / year encoded in meta data</li>
     * </ul>
     *
     *
     */
    private class SongInformation extends GridPane {

        private Label titleLabel;

        private Label artistLabel;

        private Label yearLabel;

        private Label albumLabel;

        private Label genreLabel;

        SongInformation() {
            super();
            getStyleClass().add("rootPane");
            setHgap(10);
            setVgap(10);
            titleLabel = new Label();
            artistLabel = new Label();
            yearLabel = new Label();
            albumLabel = new Label();
            genreLabel = new Label();
            titleLabel.getStyleClass().add("infoLabel");
            artistLabel.getStyleClass().add("infoLabel");
            yearLabel.getStyleClass().add("infoLabel");
            albumLabel.getStyleClass().add("infoLabel");
            genreLabel.getStyleClass().add("infoLabel");
            add(artistLabel, 0, 0);
            add(titleLabel,0,1);
            add(albumLabel,0,2);
            add(genreLabel,1,0);
            add(yearLabel,1,1);
        }

        /**
         * Updates appropriate labels which display meta data of {@code song}
         *
         * @param song whose meta data is displayed
         */
        void updateSongInformation(MP3Song song) {
            if(song != null) {
                titleLabel.setText("Title: " + (song.getTitle() == null ? "" : song.getTitle()));
                artistLabel.setText("Artist: " + (song.getArtist() == null ? "" : song.getArtist()));
                yearLabel.setText("Year: " + (song.getYear() == null ? "" : song.getYear()));
                albumLabel.setText("Album: " + (song.getAlbum() == null ? "" : song.getAlbum()));
                genreLabel.setText("Genre: " + (song.getGenre() == null ? "" : song.getGenre()));
            }
        }
    }


}
