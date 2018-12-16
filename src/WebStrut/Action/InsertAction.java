package WebStrut.Action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class InsertAction extends ActionSupport implements SessionAware {
    private Map<String,Object> session;
    String[] select = {"Artist", "Album"};

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    //public insert()

    public Map<String, Object> getSession() {
        return session;
    }

    public String[] getSelect() {
        return select;
    }

    public void setSelect(String[] select) {
        this.select = select;
    }
}
