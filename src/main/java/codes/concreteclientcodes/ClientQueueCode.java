package codes.concreteclientcodes;

import model.PlayerData;
import song.MP3Song;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;


/**
 *
 */
public class ClientQueueCode extends AbstractClientBroadcastCode {


    @Override
    public void execute(PlayerData playerData, Map<String, BufferedWriter> writers, BufferedReader reader) throws IOException {
        String UUID = reader.readLine();
        String songName = reader.readLine();
        MP3Song song = null;
        for(MP3Song m : playerData.getLoadedSongs()) {
            if(m.getFileName().equals(songName)) {
                song = m;
            }
        }
        int result = playerData.enqueueSong(UUID, song);
        for(Map.Entry<String, BufferedWriter> singleWriter : writers.entrySet()) {
            BufferedWriter writer = singleWriter.getValue();
            writer.write("SERVER_ENQUEUED");
            writer.newLine();
            writer.write(songName);
            writer.newLine();
            writer.write(UUID);
            writer.newLine();
            writer.write(Integer.toString(result));
            writer.newLine();
            writer.write("SERVER_BROADCAST_ENDED");
            writer.newLine();
            writer.flush();
        }
    }
}
