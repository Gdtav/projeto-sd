package dropmusic.server.data;

/**
 * The type User. This type can only view the information or create reviews.
 */
public class User {
    private String username;
    private String password;
    private boolean isEditor;

    private boolean login(String input) {
        return (input.equals(password));
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param password the password
     */
    public User(String username, String password, boolean isEditor) {
        this.username = username;
        this.password = password;
        this.isEditor = isEditor;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    private String getUsername(){
        return username;
    }
}
