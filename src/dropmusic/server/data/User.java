package dropmusic.server.data;

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

    public User(String username, String password, boolean isEditor) {
        this.username = username;
        this.password = password;
        this.isEditor = isEditor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    private String getUsername(){
        return username;
    }
}
