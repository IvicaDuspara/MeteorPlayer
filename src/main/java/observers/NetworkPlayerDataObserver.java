package observers;

/**
 * This interface represents observers which broadcast change over a network.<br>
 *  More precisely once a change in {@link model.PlayerData PlayerData} happens,
 *  every connected client should be notified. These clients are represented by this interface.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface NetworkPlayerDataObserver {

    /**
     * Performs an update based on described {@code code}.
     *
     * @param code which describes an update
     */
    void update(String code);
}
