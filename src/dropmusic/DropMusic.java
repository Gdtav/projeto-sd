/**
 * DROPMUSIC
 * Program's remote interface
 * Failover, Client, Server, RMIServer, DropMusic, MulticastListener classes created by Guilherme Tavares,
 * revised and corrected by Pedro Silva.
 * MulticastServer created by Pedro Silva.
 */

package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The interface Dropmusic.
 */
interface DropMusic extends Remote {

    /**
     * Register user.
     *
     * @param username the username
     * @param password the password
     * @throws RemoteException the remote exception
     */
    void register(String username, String password) throws RemoteException;

    /**
     * Logon method.
     *
     * @param username the username
     * @param password the password
     * @return the boolean [ ] which contains information about success of authentication and editor privileges
     * @throws RemoteException the remote exception
     */
    boolean[] logonUser(String username, String password) throws RemoteException;

    /**
     * Artist search array list.
     *
     * @param input the input
     * @return the array list
     * @throws RemoteException the remote exception
     */
    ArrayList<String> artistSearch(String input) throws RemoteException;

    /**
     * Album search array list.
     *
     * @param input the input
     * @return the array list
     * @throws RemoteException the remote exception
     */
    ArrayList<String> albumSearch(String input) throws RemoteException;

    /**
     * Album from artist search array list.
     * @param input the input
     * @return the array list
     * @throws RemoteException the remote exception
     */
    ArrayList<String> albumFromArtistSearch(String input) throws RemoteException;

    /**
     * Make editor boolean.
     *
     * @param input the input
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    boolean makeEditor(String input) throws RemoteException;

    /**
     * Review album boolean.
     *
     * @param grade    the grade
     * @param review   the review
     * @param album    the album
     * @param username the username
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    boolean reviewAlbum(int grade, String review, String album, String username) throws RemoteException;

    /**
     * Is alive.
     *
     * @throws RemoteException the remote exception
     */
    void isAlive() throws RemoteException;

    /**
     * Edit artist info.
     *
     * @param input the input
     * @throws RemoteException the remote exception
     */
    void editArtistInfo(HashMap<String, String> input) throws RemoteException;

    /**
     * Show artist info array list.
     *
     * @param input the input
     * @return the array list
     * @throws RemoteException the remote exception
     */
    ArrayList<String> showArtistInfo(String input) throws RemoteException;

    /**
     * Edit album info.
     *
     * @param input the input
     * @throws RemoteException the remote exception
     */
    void editAlbumInfo(HashMap<String, String> input) throws RemoteException;

    /**
     * Show album info array list.
     *
     * @param input the input
     * @return the array list
     * @throws RemoteException the remote exception
     */
    ArrayList<String> showAlbumInfo(String input) throws RemoteException;

    /**
     * Gets ip.
     *
     * @return the ip
     * @throws RemoteException the remote exception
     */
    String getIP() throws RemoteException;
}
