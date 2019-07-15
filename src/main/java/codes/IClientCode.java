package codes;

import model.PlayerData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract strategy for code communication.<br>
 *
 * Unlike {@link IServerCode} concrete codes of this class should perform both reading and writing.<br>
 *
 * When a client sends some code, appropriate {@code IClientCode} is called and server modifies {@link PlayerData}.
 * Then server will notify all other clients that a change occurred.<br>
 *
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface IClientCode {

    /**
     *
     * @param playerData
     *
     *
     * @param writers
     *        map of clients' writers which will be used for further notification
     *
     * @param reader
     *        of a client which sent code
     *
     * @throws IOException
     *         if an error occurs whil performing I/O operation
     */
    void execute(PlayerData playerData, Map<String, BufferedWriter> writers, BufferedReader reader) throws IOException;
}
