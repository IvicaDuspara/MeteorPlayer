package gui;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.PlayerData;
import observers.GraphicalPlayerDataObserver;
import observers.PlayerDisplayObserver;
import observers.SwapObserver;
import song.MP3Song;

import java.util.ArrayList;
import java.util.List;


/**
 * Displays loaded and queued songs.<br>
 * This class is a view which is backed up by {@link PlayerData}.
 * It also implements methods which handle clicks on loaded songs.<br>
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class PlayerDisplay implements GraphicalPlayerDataObserver, SwapObserver {

    private static final int LIST_PREFERRED_WIDTH = 350;

    private ListView<MP3Song> loadedSongsView;

    private ListView<MP3Song> queuedSongsView;

    private ListView<MP3Song> queriedSongsView;

    private Label nowPlaying;

    private Label nextInQueue;

    private SongInformation songInformation;

    private TextField searchBar;

    private List<PlayerDisplayObserver> playerDisplayObserverList;
    
    private PlayerData playerData;

    /**
     * Constructs a new {@code PlayerDisplay}.
     *
     * @param playerData whose data is used as a model for views in this {@code PlayerDisplay}
     */
    public PlayerDisplay(PlayerData playerData) {
        this.playerData = playerData;
        this.playerData.addGraphicalPlayerDataObserver(this);
        this.loadedSongsView = new ListView<>(playerData.getLoadedSongs());
        this.queuedSongsView = new ListView<>(playerData.getQueuedSongs());
        this.queriedSongsView = new ListView<>(playerData.getQueriedSongs());
        this.nowPlaying = new Label("");
        this.nextInQueue = new Label("");
        this.loadedSongsView.setOnMouseClicked(l -> {
            if(l.getClickCount() == 2) {
                MP3Song selected = loadedSongsView.getSelectionModel().getSelectedItem();
                playerData.playOnClick(selected);
            }
        });
        loadedSongsView.setPrefWidth(LIST_PREFERRED_WIDTH);
        this.queriedSongsView.setOnMouseClicked(l -> {
            if(l.getClickCount() == 2) {
                MP3Song selected = queriedSongsView.getSelectionModel().getSelectedItem();
                playerData.playOnClick(selected);
                searchBar.setText("");
                restoreToLoadedView();
            }
        });
        queriedSongsView.setPrefWidth(LIST_PREFERRED_WIDTH);
        this.nowPlaying.getStyleClass().add("nowPlayingLabel");
        this.nextInQueue.getStyleClass().add("queueLabel");
        this.songInformation = new SongInformation();
        this.searchBar = new TextField();
        this.searchBar.setPromptText("Search song...");
        this.searchBar.getStyleClass().add("searchField");
        DynamicSearch ds = new DynamicSearch(playerData);
        this.searchBar.textProperty().addListener(ds);
        ds.addSwapObserver(this);
        this.playerDisplayObserverList = new ArrayList<>();
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
     *         view of all loaded songs of backing model.
     */
    public ListView<MP3Song> getLoadedSongsView() {
        return loadedSongsView;
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
     * Returns view of all queried songs of backing model.
     *
     * @return
     *         view of all queried songs of backing model.
     */
    public ListView<MP3Song> getQueriedSongsView() {
        return queriedSongsView;
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


    /**
     * Returns a search bar used for searching of loaded songs.<br>
     *
     * @return
     *         a search bar used for searching of loaded songs
     */
    public TextField getSearchBar() {
        return searchBar;
    }


    /**
     * Adds {@code observer} to this {@code PlayerDisplay}
     *
     * @param observer
     *        which is added
     */
    public void addPlayerDisplayObserver(PlayerDisplayObserver observer) {
        playerDisplayObserverList.add(observer);
    }


    /**
     * Removes {@code observer} from this {@code PlayerDisplay}
     *
     * @param observer
     *        which is removed
     */
    public void removePlayerDisplayObserver(PlayerDisplayObserver observer) {
        playerDisplayObserverList.remove(observer);
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

    @Override
    public void updateQueuedSongs(ObservableList<MP3Song> songs) {
        queuedSongsView.setItems(songs);
    }

    @Override
    public void swapToQueriedView() {
        for(PlayerDisplayObserver observer : playerDisplayObserverList) {
            observer.setQueriedList();
        }
    }

    @Override
    public void restoreToLoadedView() {
        for(PlayerDisplayObserver observer : playerDisplayObserverList) {
            observer.restoreToLoadedList();
        }
    }

    @Override
    public void updateQueriedView(ObservableList<MP3Song> filteredStuff) {
        playerData.setQueriedSongs(filteredStuff);
        queriedSongsView.setItems(filteredStuff);
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
