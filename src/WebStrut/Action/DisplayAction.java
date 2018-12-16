package WebStrut.Action;

import WebStrut.Model.DropBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.HashMap;
import java.util.Map;

public class DisplayAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private HashMap<String, String> print;

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public HashMap<String, String> getPrint() {
        return print;
    }

    public void setPrint(HashMap<String, String> print) {
        this.print = print;
    }

    public DropBean getDropBean() {
        if(!session.containsKey("dropBean"))
            this.setDropBean(new DropBean());
        return (DropBean) session.get("dropBean");
    }

    public void setDropBean(DropBean dropBean) {
        this.session.put("dropBean", dropBean);
    }

    @Override
    public String execute() throws Exception {

        print.toString();
        return SUCCESS;
    }
}
