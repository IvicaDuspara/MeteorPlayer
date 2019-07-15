package codes.concretecodes;

import codes.ICommunicationCode;
import model.PlayerData;
import song.MP3Song;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Sends list of currently loaded songs to all of clients.<br>
 * This code is also invoked if additional songs are opened.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ServerSongListCode implements ICommunicationCode {


    @Override
    public void execute(PlayerData playerData, Map<String, BufferedWriter> writers) throws IOException {
        for(Map.Entry<String,BufferedWriter> singleEntry : writers.entrySet()) {
            BufferedWriter writer = singleEntry.getValue();
            writer.write("SERVER_SONG_LIST");
            System.out.println("Code failed?!");
            writer.newLine();
            for(MP3Song song : playerData.getMostRecentUpdate()) {
                writer.write(song.toString());
                System.out.println("Failed in writing" );
                writer.newLine();
            }
            writer.write("SERVER_BROADCAST_ENDED");
            writer.newLine();
            writer.flush();
        }
    }
}
