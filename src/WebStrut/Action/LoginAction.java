package WebStrut.Action;

import WebStrut.Model.DropBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * The type Login action.
 */
public class LoginAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String username = null, password = null;

    @Override
    public String execute() throws RemoteException {
        if(this.username != null && !username.equals("")) {
            this.getDropBean().setUsername(this.username);
            this.getDropBean().setPassword(this.password);
            session.put("username", username);
            if(!this.getDropBean().getUserMatchesPassword())
                return LOGIN;
            session.put("loggedin", true); // this marks the user as logged in
            return SUCCESS;
        }
        else
            return LOGIN;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username; // will you sanitize this input? maybe use a prepared statement?
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password; // what about this input?
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

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
