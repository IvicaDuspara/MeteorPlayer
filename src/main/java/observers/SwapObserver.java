package observers;

/**
 * This interface represents observers which are interested in changes in {@link gui.DynamicSearch DynamicSearch}
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface SwapObserver {

    /**
     * Swaps currently viewed list from all loaded songs to queried songs.
     */
    void swapToQueriedView();

    /**
     * Restores currently viewed list to loaded songs.
     */
    void restoreToLoadedView();
}
