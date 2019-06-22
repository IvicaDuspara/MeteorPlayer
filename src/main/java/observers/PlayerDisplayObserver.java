package observers;

/**
 * Observer interface implemented by {@link player.MeteorPlayer MeteorPlayer}.<br>
 * Any action which changes layout of MeteorPlayer is done in {@link gui.PlayerDisplay PlayerDisplay}
 * which in turn notifies {@code PlayerDisplayObservers}.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface PlayerDisplayObserver {

    /**
     *
     */
    void setQueriedList();

    /**
     *
     */
    void restoreToLoadedList();
}
