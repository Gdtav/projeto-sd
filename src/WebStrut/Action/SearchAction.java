package WebStrut.Action;

import WebStrut.Model.DropBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Search action.
 */
public class SearchAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String input;
    private String artist, album, desc;
    private String new_art, new_date1, new_date2, new_desc;
    private int rating;
    private String new_alb,new_alb_release;
    private String new_song, new_song_lyrics;

    /**
     * Albums string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String albums() throws RemoteException {
        // any username is accepted without confirmation (should check using RMI)
        ArrayList<String> albs = this.getDropBean().searchAlbums(input);
        this.session.put("albums", albs);
        this.session.put("search_result_alb", true);
        this.session.remove("search_result_art_info");
        this.session.remove("search_result_alb_info");
        this.session.remove("artist");
        this.session.remove("artist_albs");
        this.session.remove("album_reviews");
        this.session.remove("album_songs");
        return SUCCESS;
    }


    /**
     * Artists string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String artists() throws RemoteException {
        ArrayList<String> arts = this.getDropBean().searchArtists(input);
        this.session.put("artists", arts);
        this.session.put("search_result_art", true);
        this.session.remove("search_result_art_info");
        this.session.remove("search_result_alb_info");
        this.session.remove("artist");
        this.session.remove("artist_albs");
        this.session.remove("album_reviews");
        this.session.remove("album_songs");
        return SUCCESS;
    }

    /**
     * Artist info string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String artist_info() throws RemoteException {
        this.getDropBean().setArtist(this.artist);
        HashMap<String, String> art_info = this.getDropBean().artistInfo();
        ArrayList<HashMap<String, String>> art_albums = new ArrayList<>();

        for(int i=0;art_info.containsKey("album_"+i);i++) {
            HashMap<String, String> art_albums_map = new HashMap<>();
            art_albums_map.put("album_name",art_info.remove("album_"+i));
            art_albums_map.put("album_release",art_info.remove("album_release_"+i));
            art_albums.add(art_albums_map);
        }
        this.session.put("artist", art_info);
        this.session.put("artist_albs", art_albums);
        this.session.put("search_result_art_info", true);
        this.session.remove("search_result_alb_info");
        this.session.remove("album_reviews");
        this.session.remove("album_songs");

        return SUCCESS;
    }

    /**
     * Album info string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String album_info() throws RemoteException {
        this.getDropBean().setAlbum(this.album);
        HashMap<String, String> alb_info = this.getDropBean().albumInfo();
        ArrayList<HashMap<String, String>> reviews = new ArrayList<>();
        ArrayList<String> songs = new ArrayList<>();

        for(int i=0;alb_info.containsKey("song_"+i);i++) {
            songs.add(alb_info.get("song_"+i));
        }

        for(int i=0;alb_info.containsKey("review_score_"+i);i++) {
            HashMap<String, String> review = new HashMap<>();
            review.put("review_desc",alb_info.remove("review_description_"+i));
            review.put("review_score",alb_info.remove("review_score_"+i));
            reviews.add(review);
        }
        this.session.put("album", alb_info);
        this.session.put("album_songs", songs);
        this.session.put("album_reviews", reviews);
        this.session.put("search_result_alb_info", true);
        return SUCCESS;
    }

    /**
     * Review alb string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String review_alb() throws RemoteException {
        boolean response = this.getDropBean().review_alb(rating,desc);
        if(response == true)
            this.session.put("review_result", true);
        else
            this.session.put("review_result", false);
        return SUCCESS;
    }

    /**
     * Show insert artist string.
     *
     * @return the string
     */
    public String show_insert_artist() {
        this.session.put("insert_artist", true);

        return SUCCESS;
    }

    /**
     * Show insert album string.
     *
     * @return the string
     */
    public String show_insert_album() {
        this.session.put("insert_album", true);

        return SUCCESS;
    }

    /**
     * Show insert song string.
     *
     * @return the string
     */
    public String show_insert_song() {
        this.session.put("insert_song", true);

        return SUCCESS;
    }

    /**
     * New artist string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String new_artist() throws RemoteException {
        boolean response = this.getDropBean().add_art(new_art,new_date1,new_date2,new_desc);
        if(response == true)
            this.session.put("newArt_result", true);
        else
            this.session.put("newArt_result", false);
        return SUCCESS;
    }

    /**
     * New album string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String new_album() throws RemoteException {
        boolean response = this.getDropBean().add_alb(new_alb,new_alb_release);
        if(response == true)
            this.session.put("newAlb_result", true);
        else
            this.session.put("newAlb_result", false);
        return SUCCESS;
    }

    /**
     * New song string.
     *
     * @return the string
     * @throws RemoteException the remote exception
     */
    public String new_song() throws RemoteException {
        boolean response = this.getDropBean().add_song(new_song,new_song_lyrics);
        if(response == true)
            this.session.put("newSong_result", true);
        else
            this.session.put("newSong_result", false);
        return SUCCESS;
    }

    public String clean_artists() throws RemoteException {
        boolean response = this.getDropBean().clean_artists();
        if(response == true)
            this.session.put("cleanArtists_result", true);
        else
            this.session.put("cleanArtists_result", false);
        return SUCCESS;
    }
    /**
     * Gets drop bean.
     *
     * @return the drop bean
     */
    public DropBean getDropBean() {
        if(!session.containsKey("dropBean"))
            this.setHeyBean(new DropBean());
        return (DropBean) session.get("dropBean");
    }

    /**
     * Sets hey bean.
     *
     * @param dropBean the drop bean
     */
    public void setHeyBean(DropBean dropBean) {
        this.session.put("dropBean", dropBean);
    }

    /**
     * Sets input.
     *
     * @param input the input
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Sets art selected.
     *
     * @param artist the artist
     */
    public void setArt_selected(String artist) {
        this.artist = artist;
    }

    /**
     * Sets alb selected.
     *
     * @param album the album
     */
    public void setAlb_selected(String album) {
        this.album = album;
    }

    /**
     * Sets review score.
     *
     * @param score the score
     */
    public void setReview_score(int score) {
        this.rating = score;
    }

    /**
     * Sets review desc.
     *
     * @param desc the desc
     */
    public void setReview_desc(String desc) {
        this.desc = desc;
    }


    /**
     * Sets new art name.
     *
     * @param name the name
     */
    public void setNew_art_name(String name) {
        this.new_art = name;
    }

    /**
     * Sets new art start.
     *
     * @param date the date
     */
    public void setNew_art_start(String date) {
        this.new_date1 = date;
    }

    /**
     * Sets new art end.
     *
     * @param date the date
     */
    public void setNew_art_end(String date) {
        this.new_date2 = date;
    }

    /**
     * Sets new artist desc.
     *
     * @param desc the desc
     */
    public void setNew_artist_desc(String desc) {
        this.new_desc = desc;
    }


    /**
     * Sets new alb name.
     *
     * @param name the name
     */
    public void setNew_alb_name(String name) {
        this.new_alb = name;
    }

    /**
     * Sets new alb release.
     *
     * @param release the release
     */
    public void setNew_alb_release(String release) {
        this.new_alb_release = release;
    }

    /**
     * Sets new song name.
     *
     * @param name the name
     */
    public void setNew_song_name(String name) {
        this.new_song = name;
    }

    /**
     * Sets new song lyrics.
     *
     * @param lyrics the lyrics
     */
    public void setNew_song_lyrics(String lyrics) { this.new_song_lyrics = lyrics; }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
