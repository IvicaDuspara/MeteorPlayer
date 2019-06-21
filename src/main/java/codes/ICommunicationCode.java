package codes;


import java.io.BufferedWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

/**
 * Abstract strategy for code communication.<br>
 * When {@code MeteorPlayer} communicates with clients on changes (song change, queue changed, more songs loaded etc.)
 * appropriate codes are sent.<br>
 * Each concrete strategy will simply send appropriate code, needed data and signal for an end.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface ICommunicationCode {


    void sendCode();
}
