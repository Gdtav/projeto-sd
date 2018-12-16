package WebStrut.Action;

import WebStrut.Model.DropBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String input;
    private String artist, album, song;

    public String albums() throws RemoteException {
        // any username is accepted without confirmation (should check using RMI)
        ArrayList<String> albs = this.getDropBean().searchAlbums(input);
        this.session.put("albums", albs);
        this.session.put("search_result_alb", true);
        return SUCCESS;
    }


    public String artists() throws RemoteException {
        ArrayList<String> arts = this.getDropBean().searchArtists(input);
        this.session.put("artists", arts);
        this.session.put("search_result_art", true);
        return SUCCESS;
    }

    public String artist_info() throws RemoteException {
        HashMap<String, String> art_info = this.getDropBean().artistInfo(artist);
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

        return SUCCESS;
    }

    public DropBean getDropBean() {
        if(!session.containsKey("dropBean"))
            this.setHeyBean(new DropBean());
        return (DropBean) session.get("dropBean");
    }

    public void setHeyBean(DropBean dropBean) {
        this.session.put("dropBean", dropBean);
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setArt_selected(String artist) {
        this.artist = artist;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
