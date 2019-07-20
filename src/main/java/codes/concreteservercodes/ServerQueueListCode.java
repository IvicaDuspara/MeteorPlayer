package codes.concreteservercodes;

import codes.IServerCode;
import model.PlayerData;
import song.MP3Song;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Sends list of currently queued songs to client.<br>
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ServerQueueListCode implements IServerCode {

    @Override
    public void execute(PlayerData playerData, BufferedWriter writer) throws IOException {
            writer.write("SERVER_QUEUE_LIST");
            writer.newLine();
            for(Map.Entry<String, MP3Song> wqentry : playerData.getWhomstQueued().entrySet()) {
                writer.write(wqentry.getValue().toString());
                writer.newLine();
                writer.write(wqentry.getKey());
                writer.newLine();
            }
            writer.write("SERVER_BROADCAST_ENDED");
            writer.newLine();
            writer.flush();
    }
}
