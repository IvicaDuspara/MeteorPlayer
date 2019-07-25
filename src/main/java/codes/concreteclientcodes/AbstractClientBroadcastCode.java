package codes.concreteclientcodes;

import codes.IClientCode;
import model.PlayerData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 */
public abstract class AbstractClientBroadcastCode implements IClientCode {
    @Override
    public final void execute(PlayerData playerData, BufferedWriter writer, BufferedReader reader) throws IOException,NoSuchMethodException {
        throw new NoSuchMethodException("Classes inheriting from AbstractClientBroadcastCode must broadcast, not communicate only with one client");
    }
}
