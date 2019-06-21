package observers;

import song.MP3Song;

/**
 * This interface represents observers which handle GUI changes in an application {@link player.MeteorPlayer MeteorPlayer}.<br>
 * More precisely once a change happens in {@link model.PlayerData PlayerData} these observers handle GUI.
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public interface GraphicalPlayerDataObserver {

    /**
     * Updates GUI with given parameters.
     *
     * @param currentIndex
     *        index of song currently playing
     *
     * @param currentSong
     *        song which is currently playing.
     *
     * @param nextInLine
     *        song which is next in queue for playing.
     *
     */
    void update(int currentIndex, MP3Song currentSong, MP3Song nextInLine);

}
