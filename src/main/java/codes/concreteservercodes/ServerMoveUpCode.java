package codes.concreteservercodes;

import codes.IServerCode;
import model.PlayerData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Sends information to clients to move their queue by one up.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ServerMoveUpCode implements IServerCode {

    @Override
    public void execute(PlayerData playerData, Map<String, BufferedWriter> writers) throws IOException {

    }
}
