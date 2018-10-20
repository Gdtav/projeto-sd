package dropmusic.server.data;

/**
 * The type Admin. This a subclass of User with administrator privileges.
 * It is the first User to be created and stored in the users list.
 */
public class Admin extends User{

    /**
     * Instantiates a new Admin.
     *
     * @param username the username
     * @param password the password
     */
    public Admin(String username, String password) {
        super(username, password);
    }

}
