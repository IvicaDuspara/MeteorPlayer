package model;

import broadcaster.ListBroadcaster;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import observers.GraphicalPlayerDataObserver;
import observers.NetworkPlayerDataObserver;
import song.MP3Song;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents a model for {@link player.MeteorPlayer MeteorPlayer}.<br>
 *
 * {@code PlayerData} provides information on loaded songs, queued songs, currently playing song,
 * next in queue song etc. It also handles methods for changing information on model.<br>
 *
 * All such changes should notify registered {@link NetworkPlayerDataObserver NetworkPlayerDataObserver}.
 * It also manages {@link MediaPlayer} which plays {@link MP3Song songs}.<br>
 *
 *
 * @author Ivica Duspara
 * @version  1.0
 */
public class PlayerData {

    private ObservableList<MP3Song> loadedSongs;

    private ObservableList<MP3Song> queuedSongs;

    private ObservableList<MP3Song> queriedSongs;

    private List<NetworkPlayerDataObserver> networkPlayerDataObserversList;

    private List<GraphicalPlayerDataObserver> graphicalPlayerDataObserversList;

    private ExecutorService pool;

    private Map<String,MP3Song> whomstQueued;

    private ListBroadcaster broadcaster;

    private int currentlyPlayingSongIndex;

    private MP3Song currentlyPlayingSong;

    private MP3Song nextInQueueSong;

    private MediaPlayer mediaPlayer;

    private boolean randomSong;

    private Random random;

    private Set<Path> keys;

    private final static String SERVER_NOW_PLAYING = "SERVER_NOW_PLAYING";

    private final static String SERVER_MOVE_UP = "SERVER_MOVE_UP";



    /**
     * Constructs a new {@code PlayerData}
     */
    public PlayerData() {
        this.loadedSongs = FXCollections.observableArrayList();
        this.queuedSongs = FXCollections.observableArrayList();
        this.queriedSongs = FXCollections.observableArrayList();
        this.networkPlayerDataObserversList = new ArrayList<>();
        this.graphicalPlayerDataObserversList = new ArrayList<>();
        this.keys = new HashSet<>();
        pool = Executors.newSingleThreadExecutor();
        whomstQueued = new LinkedHashMap<>();
        this.randomSong = false;
        currentlyPlayingSongIndex = 0;
        random = new Random();
    }


    /**
     * Sets whether {@code MeteorPlayer}  plays randomly.
     *
     * @param randomSong
     *        {@code} true if {@code MeteorPlayer} plays randomly.
     */
    public void setRandomSong(boolean randomSong) {
        this.randomSong = randomSong;
    }


    /**
     * Returns loaded songs of this {@code PlayerData}.
     *
     * @return loaded songs of this {@code PlayerData}
     */
    public ObservableList<MP3Song> getLoadedSongs() {
        return loadedSongs;
    }


    /**
     * Returns queued songs of this {@code PlayerData}.
     *
     * @return queued songs of this {@code PlayerData}
     */
    public ObservableList<MP3Song> getQueuedSongs() {
        return queuedSongs;
    }


    /**
     * Returns queried songs of this {@code PlayerData}.
     *
     * @return queried songs of this {@code PlayerData}
     */
    public ObservableList<MP3Song> getQueriedSongs() {
        return queriedSongs;
    }


    /**
     * Returns {@code Map} which holds information on which client queued which song.<br>
     * Keys of such {@code Map} are MAC addresses of clients and values are {@link MP3Song MP3song} which is queued.
     *
     * @return  {@code Map} which holds information on which client queued which song
     */
    public Map<String, MP3Song> getWhomstQueued() {
        return whomstQueued;
    }


    /**
     * Sets {@code queriedSongs} to {@code songs}
     *
     * @param songs
     *        which are used as new {@code queriedSongs}
     */
    public void setQueriedSongs(ObservableList<MP3Song> songs) {
        this.queriedSongs = songs;
    }


    /**
     * Adds {@code networkPlayerDataObserver} to this {@code PlayerData}
     *
     * @param networkPlayerDataObserver
     *        which is added to this {@code PlayerData}
     */
    public void addNetworkPlayerDataObserver(NetworkPlayerDataObserver networkPlayerDataObserver) {
        this.networkPlayerDataObserversList.add(networkPlayerDataObserver);
    }


    /**
     * Removes {@code networkPlayerDataObserver} from this {@code PlayerData}
     *
     * @param networkPlayerDataObserver
     *        which is removed from this {@code PlayerData}
     */
    public void removeNetworkPlayerDataObserver(NetworkPlayerDataObserver networkPlayerDataObserver) {
        this.networkPlayerDataObserversList.remove(networkPlayerDataObserver);
    }


    /**
     * Notifies {@link NetworkPlayerDataObserver observers} registered to this {@code PlayerData}.
     */
    private void notifyNetworkPlayerDataObservers(String code) {
        for(NetworkPlayerDataObserver networkPlayerDataObserver : networkPlayerDataObserversList) {
            networkPlayerDataObserver.update(code);
        }
    }


    /**
     * Adds {@code graphicalPlayerDataObserver} to this {@code PlayerData}.
     *
     * @param graphicalPlayerDataObserver
     *        which is added to this {@code PlayerData}
     */
    public void addGraphicalPlayerDataObserver(GraphicalPlayerDataObserver graphicalPlayerDataObserver) {
        this.graphicalPlayerDataObserversList.add(graphicalPlayerDataObserver);
    }


    /**
     * Removes {@code graphicalPlayerDataObserver} from this {@code PlayerData}
     *
     * @param graphicalPlayerDataObserver
     *        which is removed from this {@code PlayerData}
     */
    public void removeGraphicalPlayerDataObserver(GraphicalPlayerDataObserver graphicalPlayerDataObserver) {
        this.graphicalPlayerDataObserversList.remove(graphicalPlayerDataObserver);
    }


    /**
     * Notifies {@link GraphicalPlayerDataObserver observers} registered to this {@code PlayerData}
     */
    private void notifyGraphicalPlayerDataObservers() {
        for(GraphicalPlayerDataObserver graphicalPlayerDataObserver : graphicalPlayerDataObserversList) {
            graphicalPlayerDataObserver.update(currentlyPlayingSongIndex,currentlyPlayingSong,nextInQueueSong);
        }
    }


    /**
     * Plays next {@link MP3Song}. This method will check whether {@link #queuedSongs} is empty. If it isn't
     * next queued song is played, and queue is updated. If it is empty next song from {@link #loadedSongs}
     * will be played.<br>
     *
     * For more information see {@link #playNextLoaded()} and {@link #playNextQueued()}
     *
     */
    public synchronized void playNextSong() {
        if(!queuedSongs.isEmpty()) {
            playNextQueued();
        }
        else {
            playNextLoaded();
        }
    }


    /**
     * Removes head of {@link #queuedSongs}. Returns removed head.<br>
     *
     * @return head of {@link #queuedSongs}
     */
    private synchronized MP3Song dequeueSong() {
        MP3Song removed = queuedSongs.remove(0);
        String key = "";
        for(Map.Entry<String,MP3Song> w :whomstQueued.entrySet()) {
            if(w.getValue().equals(removed)) {
                key = w.getKey();
                break;
            }
        }
        whomstQueued.remove(key);
        return removed;
    }


    /**
     * Plays {@code clickedSong}. This method is called when a user
     * clicks on a song which they wish to play.
     *
     */
    public synchronized void playOnClick(MP3Song clickedSong) {
        currentlyPlayingSong = clickedSong;
        currentlyPlayingSongIndex = loadedSongs.indexOf(clickedSong);
        playSong(currentlyPlayingSong);
    }


    /**
     * Plays next {@link MP3Song} in {@link #loadedSongs}. If {@link #randomSong} is {@code true}
     * random song will be selected.<br>
     *
     * Note that {@code PlayerData} has no memory of order of random songs.
     */
    private void playNextLoaded() {
        if(!randomSong) {
            currentlyPlayingSongIndex++;
            if(currentlyPlayingSongIndex == loadedSongs.size()) {
                currentlyPlayingSongIndex = 0;
            }
            currentlyPlayingSong = loadedSongs.get(currentlyPlayingSongIndex);
        }
        else {
            int randomIndex = 0;
            while(randomIndex == currentlyPlayingSongIndex) {
                randomIndex = random.nextInt(loadedSongs.size());
            }
            currentlyPlayingSongIndex = randomIndex;
            currentlyPlayingSong = loadedSongs.get(currentlyPlayingSongIndex);
        }
        playSong(currentlyPlayingSong);
    }


    /**
     * Plays next {@link MP3Song} in {@link #queuedSongs}.
     */
    private void playNextQueued() {
        MP3Song top = dequeueSong();
        notifyNetworkPlayerDataObservers(SERVER_MOVE_UP);
        if(queuedSongs.isEmpty()) {
            nextInQueueSong = null;
        }
        else {
            nextInQueueSong = queuedSongs.get(0);
        }
        currentlyPlayingSong = top;
        currentlyPlayingSongIndex = loadedSongs.indexOf(currentlyPlayingSong);
        playSong(currentlyPlayingSong);
    }




    /**
     * Prepares {@code MediaPlayer} and plays {@code song}.
     *
     * @param song
     *        which is played
     */
    private void playSong(MP3Song song) {
        prepareMediaPlayer(song);
        mediaPlayer.play();
        notifyNetworkPlayerDataObservers(SERVER_NOW_PLAYING);
        song.extractMetaData();
        notifyGraphicalPlayerDataObservers();
    }


    /**
     * Prepares {@code mediaPlayer} for playing {@code song}.<br>
     * Media Player is prepared by setting behaviour of {@code mediaPlayer} upon completion of {@code song}.
     *
     *
     * @param song
     *        which will be played
     */
    private void prepareMediaPlayer(final MP3Song song) {
        if(song != null) {
            if(mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            mediaPlayer = new MediaPlayer(song.getMediaFile());
            mediaPlayer.setOnEndOfMedia(this::playNextSong);
        }
    }


    /**
     * Plays previous song. This method is called when a user clicks previous button.<br>
     * If {@link #randomSong} is {@code true} a random song will be played.<br>
     * Note that {@code PlayerData} doesn't remember random order.
     *
     */
    public void playPreviousLoaded() {
        if(!randomSong) {
            currentlyPlayingSongIndex--;
            if(currentlyPlayingSongIndex <= -1) {
                currentlyPlayingSongIndex = loadedSongs.size() - 1;
            }
            currentlyPlayingSong = loadedSongs.get(currentlyPlayingSongIndex);
        }
        else {
            int randomIndex = 0;
            while(randomIndex == currentlyPlayingSongIndex) {
                randomIndex = random.nextInt(loadedSongs.size());
            }
            currentlyPlayingSongIndex = randomIndex;
            currentlyPlayingSong = loadedSongs.get(currentlyPlayingSongIndex);
        }
        playSong(currentlyPlayingSong);
    }


    /**
     * Plays or pauses a song.
     */
    public void togglePlay() {
        if(mediaPlayer != null) {
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            }
            else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            }
        }
    }


    /**
     * Adds {@code songs} to {@link #loadedSongs} if they are not added already.<br>
     * Extraction is done from {@link PopulateSongsListJob}.
     *
     *
     * @param files which will be added
     */
    public void addSongs(List<File> files) {
        if(files != null) {
            PopulateSongsListJob populationJob = new PopulateSongsListJob(files);
            try {
                pool.submit(populationJob).get();
                notifyGraphicalPlayerDataObservers();
            } catch (InterruptedException | ExecutionException ex) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR,ex.getMessage(), ButtonType.CLOSE);
                    alert.setTitle("Error");
                    alert.setContentText("Failed while loading files.\n" + ex.getMessage());
                    alert.showAndWait();
                });
            }
        }
    }

    /**
     * Closes {@code PlayerData}
     */
    public void closePlayerData() {
        pool.shutdown();
    }

    /**
     *
     * Represents a job which adds songs to {@link #loadedSongs}.<br>
     * Only those files which are already not loaded will be added to {@code PlayerData}
     * {@code PopulateSongsListJob} will never delete already loaded songs -> it will simply add them.
     *
     * @author Ivica Duspara
     * @version 1.0
     */
    private class PopulateSongsListJob implements Runnable {

        /**
         * Reference to listed files which will be loaded to {@code PlayerData}
         */
        private List<File> files;


        /**
         * Constructs a new {@code PopulateSongListJob}
         *
         * @param files
         *        which will be converted into {@link MP3Song files}
         */
        public PopulateSongsListJob(List<File> files) {
            this.files = files;
        }

        @Override
        public void run() {
            for(File file : files) {
                Path path = file.toPath();
                if(!keys.contains(path)) {
                    keys.add(path);
                    loadedSongs.add(new MP3Song(new Media(file.toURI().toString()), file.getName()));
                }
            }
        }
    }
}
