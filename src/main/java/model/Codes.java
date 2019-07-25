package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Description of codes which signal changes.<br>
 * These codes are sent as identification to {@link broadcaster.ListBroadcaster ListBroadcaster} so
 * it can use correct {@link codes.IServerCode IServerCode} or {@link codes.IClientCode IClientCode}
 *
 * @author Ivica Duspara
 * @version 1.1
 */
public class Codes {

    private static Codes CODES_SINGLETON = null;

    private Map<String, String> codesMap = null;

    private Codes() {
        codesMap = new HashMap<>();
        codesMap.put("SERVER_NOW_PLAYING","ServerNowPlayingCode");
        codesMap.put("SERVER_MOVE_UP","ServerMoveUpCode");
        codesMap.put("SERVER_SONG_LIST","ServerSongListCode");
        codesMap.put("SERVER_QUEUE_LIST","ServerQueueListCode");
        codesMap.put("SERVER_SONG_PARTIAL_LIST","ServerSongPartialListCode");
        codesMap.put("CLIENT_QUEUE","ClientQueueCode");
        codesMap.put("CLIENT_SONGS_REQUEST","ClientSongRequestCode");
        codesMap.put("CLIENT_QUEUE_REQUEST","ClientQueueRequestCode");
        codesMap.put("CLIENT_NOW_PLAYING_REQUEST","ClientNowPlayingRequestCode");
    }

    public String getCodeValue(String key) {
        return CODES_SINGLETON.codesMap.get(key);
    }

    public static Codes getInstance() {
        if(CODES_SINGLETON == null) {
            CODES_SINGLETON = new Codes();
        }
        return CODES_SINGLETON;
    }
}
