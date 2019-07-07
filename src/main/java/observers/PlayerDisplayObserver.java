package observers;

/**
 * An observer which is interested in changes of a view, such as swapping of loaded list with search result.<br>
 * View which broadcasts this changes is implemented in {@link gui.PlayerDisplay PlayerDisplay}
 *
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface PlayerDisplayObserver {

    /**
     * Stashes list of loaded songs and swaps it with a list of queried songs which
     * is returned from {@link gui.DynamicSearch DynamicSearch}.
     */
    void setQueriedList();

    /**
     * Restores a list to list of loaded songs.
     */
    void restoreToLoadedList();
}
