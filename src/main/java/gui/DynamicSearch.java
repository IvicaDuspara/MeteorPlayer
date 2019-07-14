package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.PlayerData;
import observers.SwapObserver;
import song.MP3Song;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Dynamically searches loaded songs of {@link PlayerData} model.<br>
 *
 *
 * @author Ivica Duspara
 * @version 1.0
 */
 class DynamicSearch implements ChangeListener<String> {

    private Pattern searchPattern;

    private Matcher matcher;

    private ObservableList<MP3Song> loadedSongs;

    private ObservableList<MP3Song> queriedSongs;

    private List<SwapObserver> swapObserverList;


    DynamicSearch(PlayerData playerData) {
        this.loadedSongs = playerData.getLoadedSongs();
        this.queriedSongs = playerData.getQueriedSongs();
        this.swapObserverList = new ArrayList<>();
    }


    /**
     * Adds {@code observer} to this {@code DynamicSearch}
     *
     * @param observer
     *        which is added
     */
    public void addSwapObserver(SwapObserver observer) {
        this.swapObserverList.add(observer);
    }


    /**
     * Removes {@code observer} from this {@code DynamicSearch}
     *
     * @param observer
     *        which is removed
     */
    public void removeSwapObserver(SwapObserver observer) {
        this.swapObserverList.remove(observer);
    }


    /**
     * Notifies observers to make a swap.
     */
    private void swapNotify() {
        for(SwapObserver observer : swapObserverList) {
            observer.swapToQueriedView();
        }
    }


    /**
     * Notifies observers to restore list view to loaded lists.
     */
    private void restoreNotify() {
        for(SwapObserver observer : swapObserverList) {
            observer.restoreToLoadedView();
        }
    }


    /**
     * Notifies on extra letter inputs.
     */
    private void queryNotify(ObservableList<MP3Song> filteredStuff) {
        for(SwapObserver observer : swapObserverList) {
            observer.updateQueriedView(filteredStuff);
        }
    }


    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        //first letter is typed.
        if(oldValue.isEmpty()) {
            searchPattern = Pattern.compile(newValue.toLowerCase());
            for(MP3Song song : loadedSongs) {
                String name = song.getFileName().toLowerCase();
                matcher = searchPattern.matcher(name);
                if(matcher.find()) {
                    queriedSongs.add(song);
                }
            }
            swapNotify();
        }
        else if(newValue.isEmpty()) {
            queriedSongs.clear();
            restoreNotify();
        }
        else {
            ObservableList<MP3Song> filteredStuff = FXCollections.observableArrayList();
            searchPattern = Pattern.compile(newValue.toLowerCase());
            for(MP3Song song: queriedSongs) {
                String name = song.getFileName().toLowerCase();
                matcher = searchPattern.matcher(name);
                if(matcher.find()) {
                    filteredStuff.add(song);
                }
            }
            /*
            * If an newValue is something misspelled queried songs are empty.
            * Then if an user deletes a character/characters to correct themselves, newValue would search in an empty list because
            * of spelling mistake that first occurred. The following code will check if filteredStuff is empty, and if it is it will look
            * for songs matching newValue in loadedSongs. This is a form of lazy error recovery
            * */
            if(filteredStuff.isEmpty()) {
                for(MP3Song song : loadedSongs) {
                    String name = song.getFileName().toLowerCase();
                    matcher = searchPattern.matcher(name);
                    if(matcher.find()) {
                        filteredStuff.add(song);
                    }
                }
            }
            queriedSongs = filteredStuff;
            queryNotify(filteredStuff);
        }
    }
}
