package codes.concreteservercodes;

import codes.IServerCode;
import model.PlayerData;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Sends currently playing song to client.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ServerNowPlayingCode implements IServerCode {

    @Override
    public void execute(PlayerData playerData, BufferedWriter writer) throws IOException {
            writer.write("SERVER_NOW_PLAYING");
            writer.newLine();
            writer.write(playerData.getCurrentlyPlayingSong().toString());
            writer.newLine();
            writer.write("SERVER_BROADCAST_ENDED");
            writer.newLine();
            writer.flush();
    }
}
