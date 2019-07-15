package codes;

import model.PlayerData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract strategy for code communication.<br>
 * When {@code MeteorPlayer} communicates with clients on changes (song change, queue changed, more songs loaded etc.)
 * appropriate codes are sent.<br>
 * Each concrete strategy will simply send appropriate code, needed data and signal for an end.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface ICommunicationCode {

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
