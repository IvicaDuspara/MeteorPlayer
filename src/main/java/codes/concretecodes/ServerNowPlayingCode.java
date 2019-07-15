package codes.concretecodes;

import codes.ICommunicationCode;
import model.PlayerData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Sends currently playing song to client.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ServerNowPlayingCode implements ICommunicationCode {

    @Override
    public void execute(PlayerData playerData, Map<String, BufferedWriter> writers) throws IOException {

    }
}
