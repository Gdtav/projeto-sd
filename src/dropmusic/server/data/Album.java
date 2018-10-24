package dropmusic.server.data;

import java.util.List;

public class Album {
    private int year;
    private String name;
    private String label;
    private List<String> reviews;
    private Artist artist;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Album(int year, String name, String label, List<String> reviews, Artist artist) {
        this.year = year;
        this.name = name;
        this.label = label;
        this.reviews = reviews;
        this.artist = artist;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}