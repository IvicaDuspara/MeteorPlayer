package codes.concreteclientcodes;

import broadcaster.ListBroadcaster;
import model.Codes;
import model.PlayerData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ClientSongRequestCode extends AbstractClientDirectCode{
    @Override
    public void execute(PlayerData playerData, BufferedWriter writer, BufferedReader reader) throws IOException, NoSuchMethodException {
        ListBroadcaster.getInstance().getCommunicationCodes().get(Codes.getInstance().getCodeValue("SERVER_SONG_LIST")).execute(playerData,writer);
    }
}
