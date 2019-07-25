package codes;

import model.PlayerData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract strategy for code communication.<br>
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
     * @param writers
     *        map of clients' writers which will be used for further notification
     *
     * @param reader
     *        of a client which sent code
     *
     * @throws IOException
     *         if an error occurs while performing I/O operation
     *
     * @throws NoSuchMethodException
     *
     */
    void execute(PlayerData playerData, Map<String, BufferedWriter> writers, BufferedReader reader) throws IOException,NoSuchMethodException;



    /**
     *
     * @param playerData
     *
     * @param writer
     *
     * @param reader
     *
     * @throws IOException
     *
     * @throws NoSuchMethodException
     */
    void execute(PlayerData playerData, BufferedWriter writer, BufferedReader reader) throws IOException,NoSuchMethodException;

}
