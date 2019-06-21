package song;

import javafx.beans.Observable;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Represents a playable mp3 song.<br>
 * Every object of this class will have underlying {@link Media} file where song is loaded and meta data which is extracted
 * from underlying {@code Media} object.<br>
 * Meta-data will include:
 * <ul>
 *     <li>Title</li>
 *     <li>Release year</li>
 *     <li>Artist</li>
 *     <li>Genre</li>
 *     <li>Album</li>
 * </ul>
 * Two {@code MP3Song} objects are compared based on <i>extracted</i> file name.
 *
 * @version 1.0
 * @author Ivica Duspara
 */
public class MP3Song implements Comparable<MP3Song>{

    /**
     * Extracted name
     */
    private String fileName;

    private String title;

    private String album;

    private String year;

    private String genre;

    private String artist;

    private Media mediaFile;

    private boolean metaDataSet;

    private Path filePath;


    /**
     * Constructs a new {@code MP3Song}.<br>
     * An exception will be thrown if any of arguments are {@code null}.
     *
     * @param mediaFile
     *        - MP3 file which is played.
     *
     * @param fileName
     *        - Name of a MP3 file
     *
     * @throws NullPointerException if {@code mediaFile} or {@code fileName} is {@code null}
     */
    public MP3Song(Media mediaFile, String fileName) {
        Objects.requireNonNull(mediaFile);
        Objects.requireNonNull(fileName);
        this.fileName = fileName;
        this.mediaFile = mediaFile;
        this.metaDataSet = false;
        this.filePath = Paths.get(mediaFile.getSource().split(":")[1]);

    }

    /**
     * Returns {@code fileName} of this {@code Song}.
     *
     * @return {@code fileName} of this {@code Song}
     */
    public String getFileName() {
        return fileName;
    }


    /**
     * Returns {@code title} of this {@code Song}.
     *
     * @return {@code title} of this {@code Song}
     */
    public String getTitle() {
        return title;
    }


    /**
     * Returns {@code album} of this {@code Song}.
     *
     * @return {@code album} of this {@code Song}
     */
    public String getAlbum() {
        return album;
    }


    /**
     * Returns {@code year} of this {@code Song}.
     *
     * @return {@code year} of this {@code Song}
     */
    public String getYear() {
        return year;
    }


    /**
     * Returns {@code genre} of this {@code Song}.
     *
     * @return {@code genre} of this {@code Song}
     */
    public String getGenre() {
        return genre;
    }


    /**
     * Returns {@code artist} of this {@code Song}.
     *
     * @return {@code artist} of this {@code Song}
     */
    public String getArtist() {
        return artist;
    }


    /**
     * Returns {@code mediaFile} of this {@code Song}.
     *
     * @return {@code mediaFile} of this {@code Song}
     */
    public Media getMediaFile() {
        return mediaFile;
    }


    /**
     * Returns {@code true} if {@link Media} meta-data is set.
     *
     * @return {@code true} if {@link Media} meta-data is set
     */
    public boolean ismetaDataSet() {
        return metaDataSet;
    }


    /**
     * Returns {@code filePath} of this {@code Song}.
     *
     * @return {@code filePath} of this {@code Song}
     */
    public Path getFilePath() {
        return filePath;
    }


    /**
     * Extracts meta data from {@code mediaFile} into appropriate fields of this {@code Song} object
     * if data has not already been extracted.
     *
     */
    public void extractMetaData() {
        if(!metaDataSet) {
            ObservableMap<String, Object> metaData  = mediaFile.getMetadata();
            this.title = metaData.get("title").toString();
            this.album = metaData.get("album").toString();
            this.artist = metaData.get("artist").toString();
            this.genre = metaData.get("genre").toString();
            this.year = metaData.get("year").toString();
            metaDataSet = true;
        }
    }

    public int compareTo(MP3Song o) {
        return fileName.compareTo(o.fileName);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == MP3Song.class) {
            MP3Song casted = (MP3Song) obj;
            return casted.fileName.equals(this.fileName);
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
