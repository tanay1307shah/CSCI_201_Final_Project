import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import java.io.File;

/**
 * Represents a song. Contains all pertinent metadata, as well as the file location and
 * song ID on the database
 */
public class Song {
    private String title;
    private List<String> artist;
    private String composer;
    private int length; //milliseconds
    private String genre;
    private String album;
    private Image img;
    private String fLocation;
    private String year;
    private int songID; //for database purposes, should be unique

    public String getTitle() {
        return title;
    }

    public List<String> getArtist() {
        return artist;
    }

    public String getComposer() {
        return composer;
    }

    public long getLength() {
        return length;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }

    public Image getImg() {
        return img;
    }

    public String getFileLocation() {
        return fLocation;
    }

    public String getYear() {
        return year;
    }

    public int getID() {
        return songID;
    }

    public Song(File audioFile)
    {
        fLocation =audioFile.getPath();
        AudioFile f = null;
        try {
            f = AudioFileIO.read(audioFile);
            Tag tag = f.getTag();
            length = f.getAudioHeader().getTrackLength();
            album = tag.getFirst(FieldKey.ALBUM);
            title = tag.getFirst(FieldKey.TITLE);
            year = tag.getFirst(FieldKey.YEAR);
            genre = tag.getFirst(FieldKey.GENRE);
            composer = tag.getFirst(FieldKey.COMPOSER);
            List<String> list = tag.getAll(FieldKey.ARTIST);
            Artwork artwork = tag.getFirstArtwork();
            img = (Image)artwork.getImage();
            this.songID = Database.createSong(fLocation);
        } catch (CannotReadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String out = "Song{" +
                "title='" + title + '\'' +
                ", artist=";
        if(artist != null) {
            for (String art : artist)
                out += art + "|";
        }
        out += ", composer='" + composer + '\'' +
        ", length=" + length +
        ", genre='" + genre + '\'' +
        ", album='" + album + '\'' +
        ", img=" + img +
        ", fLocation=" + fLocation +
        ", year='" + year + '\'' +
        ", id=" + songID +
        '}';
        return out;
    }

}
