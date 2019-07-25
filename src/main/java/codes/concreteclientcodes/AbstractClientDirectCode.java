package codes.concreteclientcodes;

import codes.IClientCode;
import model.PlayerData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
public abstract class AbstractClientDirectCode implements IClientCode {
    @Override
    public final void execute(PlayerData playerData, Map<String, BufferedWriter> writers, BufferedReader reader) throws IOException, NoSuchMethodException {
        throw new NoSuchMethodException("Classes inheriting from AbstractClientDirectCode communicate only with one client. They do not broadcast");
    }
}
