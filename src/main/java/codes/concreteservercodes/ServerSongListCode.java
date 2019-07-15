package codes.concreteservercodes;

import codes.IServerCode;
import model.PlayerData;
import song.MP3Song;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Sends list of currently loaded songs to all of clients.<br>
 * This code is also invoked if additional songs are opened.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ServerSongListCode implements IServerCode {


    @Override
    public void execute(PlayerData playerData, BufferedWriter writer) throws IOException {
            writer.write("SERVER_SONG_LIST");
            writer.newLine();
            for(MP3Song song : playerData.getMostRecentUpdate()) {
                writer.write(song.toString());
                writer.newLine();
            }
            writer.write("SERVER_BROADCAST_ENDED");
            writer.newLine();
            writer.flush();
    }
}
