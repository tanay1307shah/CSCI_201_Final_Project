package sample;

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

public class Song {
    private String title;
    private List<String> artist;
    private String composer;
    private long length; //milliseconds
    private String genre;
    private String album;
    private Image img;
    private File file;
    private String year;
    private long id; //for database purposes, should be unique

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

    public File getFile() {
        return file;
    }

    public String getYear() {
        return year;
    }

    public long getId() {
        return id;
    }

    public Song(File audioFile, long id)
    {
        file =audioFile;
        AudioFile f = null;
        try {
            f = AudioFileIO.read(file);
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
            this.id = id;
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
        ", file=" + file +
        ", year='" + year + '\'' +
        ", id=" + id +
        '}';
        return out;
    }

}
