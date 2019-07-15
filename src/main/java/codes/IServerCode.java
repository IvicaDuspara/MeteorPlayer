package codes;

import model.PlayerData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract strategy for code communication.<br>
 *
 * When {@link PlayerData model} performs an action which should notify interested observers (change of currently played song,
 * more songs added...) it will do so by using appropriate {@code IServerCode}.<br>
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface IServerCode {

    /**
     * Executes action which is specific for this {@code ICommunicationCode}
     *
     * @param playerData
     *        context on which action is performed
     *
     * @param writers
     *        map of client's socket writers which are used for writing to client
     *
     * @throws IOException
     *         if an error occurs while writing to any of writers in {@code writers}
     */
    void execute(PlayerData playerData, Map<String, BufferedWriter> writers) throws IOException;

}
