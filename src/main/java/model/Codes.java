package model;

/**
 * Description of codes which signal changes.<br>
 * These codes are sent as identification to {@link broadcaster.ListBroadcaster ListBroadcaster} so
 * it can use correct {@link codes.IServerCode IServerCode} or {@link codes.IClientCode IClientCode}
 */
public class Codes {

     public final static String SERVER_NOW_PLAYING = "ServerNowPlayingCode";
     public final static String SERVER_MOVE_UP = "ServerMoveUpCode";
     public final static String SERVER_SONG_LIST = "ServerSongListCode";
     public final static String SERVER_QUEUE_LIST = "ServerQueueListCode";
     public final static String SERVER_SONG_PARTIAL_LIST = "ServerSongPartialListCode";
     public final static String CLIENT_QUEUE = "ClientQueueCode";
}
