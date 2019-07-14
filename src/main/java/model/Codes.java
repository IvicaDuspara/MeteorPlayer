package model;

/**
 * Description of codes which signal changes.<br>
 * These codes are sent as identification to {@link broadcaster.ListBroadcaster ListBroadcaster} so
 * it can use correct {@link codes.ICommunicationCode ICommunicationCode} to handle requests / responses
 */
class Codes {

     final static String SERVER_NOW_PLAYING = "SERVER_NOW_PLAYING";
     final static String SERVER_MOVE_UP = "SERVER_MOVE_UP";
     final static String SERVER_SONG_LIST = "SERVER_SONG_LIST";
     final static String SERVER_QUEUE_LIST = "SERVER_QUEUE_LIST";

}
