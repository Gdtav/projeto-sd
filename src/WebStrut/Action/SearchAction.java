package WebStrut.Action;

import WebStrut.Model.DropBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
        ArrayList<String> arts = this.getDropBean().showArtistInfo(artist);
        this.session.put("artist", arts);
        this.session.put("search_result_art", true);
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
