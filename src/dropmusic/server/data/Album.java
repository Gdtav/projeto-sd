package dropmusic.server.data;

import java.util.List;

/**
 * The type Album.
 */
public class Album {
    private int year;
    private String name;
    private String label;
    private List<String> reviews;
    private Artist artist;

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets year.
     *
     * @param year the year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Instantiates a new Album.
     *
     * @param year    the year
     * @param name    the name
     * @param label   the label
     * @param reviews the reviews
     * @param artist  the artist
     */
    public Album(int year, String name, String label, List<String> reviews, Artist artist) {
        this.year = year;
        this.name = name;
        this.label = label;
        this.reviews = reviews;
        this.artist = artist;
    }

    /**
     * Gets reviews.
     *
     * @return the reviews
     */
    public List<String> getReviews() {
        return reviews;
    }

    /**
     * Sets reviews.
     *
     * @param reviews the reviews
     */
    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    /**
     * Gets artist.
     *
     * @return the artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * Sets artist.
     *
     * @param artist the artist
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}