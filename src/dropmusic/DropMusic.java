package dropmusic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DROPMUSIC
 * Program's remote interface
 * Failover, Client, Server, RMIServer, DropMusic, MulticastListener classes created by Guilherme Tavares,
 * revised and corrected by Pedro Silva.
 * MulticastServer created by Pedro Silva.
 */
public interface DropMusic extends Remote {

    /**
     * Register.
     *
     * @param username the username
     * @param password the password
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    boolean register(String username, String password) throws RemoteException;

    /**
     * Logon user array list.
     *
     * @param username the username
     * @param password the password
     * @return the array list
     * @throws RemoteException the remote exception
     */
    ArrayList<String> logonUser(String username, String password) throws RemoteException;

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
     *
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
     * Show artist info hash map.
     *
     * @param input the input
     * @return the hash map
     * @throws RemoteException the remote exception
     */
    HashMap<String, String> showArtistInfo(String input) throws RemoteException;

    /**
     * Edit album info.
     *
     * @param input the input
     * @throws RemoteException the remote exception
     */
    void editAlbumInfo(HashMap<String, String> input) throws RemoteException;

    /**
     * Show album info hash map.
     *
     * @param input the input
     * @return the hash map
     * @throws RemoteException the remote exception
     */
    HashMap<String, String> showAlbumInfo(String input) throws RemoteException;

    /**
     * Add artist string.
     *
     * @param name  the name
     * @param date1 the date 1
     * @param date2 the date 2
     * @param desc  the desc
     * @return the string
     * @throws RemoteException the remote exception
     */
    boolean addArtist(String name, String date1, String date2, String desc) throws RemoteException;

    /**
     * Add album string.
     *
     * @param name   the name
     * @param date   the date
     * @param artist the artist
     * @return the string
     * @throws RemoteException the remote exception
     */
    boolean addAlbum(String name, String date, String artist) throws RemoteException;

    /**
     * Add song boolean.
     *
     * @param name   the name
     * @param lyrics the lyrics
     * @param artist the artist
     * @param album  the album
     * @return the boolean
     * @throws RemoteException the remote exception
     */
    boolean addSong(String name, String lyrics, String artist, String album) throws RemoteException;
}
