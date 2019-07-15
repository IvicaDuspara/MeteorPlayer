package codes.concreteservercodes;

import codes.IServerCode;
import model.PlayerData;

import java.io.BufferedWriter;
import java.io.IOException;


/**
 * Sends information to clients to move their queue by one up.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ServerMoveUpCode implements IServerCode {

    @Override
    public void execute(PlayerData playerData, BufferedWriter writer) throws IOException {
            writer.write("SERVER_MOVE_UP");
            writer.newLine();
            writer.write("SERVER_BROADCAST_ENDED");
            writer.newLine();
            writer.flush();
    }
}
