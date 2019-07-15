package codes.concreteclientcodes;

import codes.IClientCode;
import model.PlayerData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ivica Duspara
 * @version 1.0
 */
public class ClientQueueCode implements IClientCode {


    @Override
    public void execute(PlayerData playerData, Map<String, BufferedWriter> writers, BufferedReader reader) throws IOException {
        String UUID = reader.readLine();
        String songName = reader.readLine();
        int result = playerData.enqueueSong(UUID, songName);
        System.out.println("UUID: " + UUID + " songName: " + songName + " result: " + result);
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
