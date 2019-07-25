package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class which stores codes for communication.<br>
 * Codes are stored in a {@code map} key is a string which describes
 * an action and value is a simple class name of appropriate {@link codes.IClientCode IClientCode}
 * or {@link codes.IServerCode IServerCode}.<br>
 *
 * For example, when a now song starts playing connected clients should be notified.
 * This is done by calling {@link codes.concreteservercodes.ServerNowPlayingCode ServerNowPlayingCode}
 * whose simple class name is stored under key SERVER_NOW_PLAYING.<br>
 *
 * For actions of concrete codes, please see {@link codes.concreteclientcodes} or {@link codes.concreteservercodes}
 *
 * @author Ivica Duspara
 * @version 1.1
 */
public class Codes {

    private static Codes CODES_SINGLETON = null;

    private Map<String, String> codesMap = null;


    /**
     * Constructs {@code Codes} class.
     */
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

    /**
     * Returns a value under {@code key} from {@code codesMap}.<br>
     *
     * @param key
     *        whose value is returned
     *
     * @return
     *        value stored under {@code key}
     */
    public String getCodeValue(String key) {
        return CODES_SINGLETON.codesMap.get(key);
    }

    /**
     * Returns singleton instance of {@code Codes}
     *
     * @return
     *         singleton instance of {@code Codes}
     */
    public static Codes getInstance() {
        if(CODES_SINGLETON == null) {
            CODES_SINGLETON = new Codes();
        }
        return CODES_SINGLETON;
    }
}
