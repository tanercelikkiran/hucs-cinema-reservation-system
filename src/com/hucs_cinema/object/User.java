package com.hucs_cinema.object;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

import com.hucs_cinema.exception.*;

public class User {

    private final String USERNAME; //Username
    private String usernameWithDegree; //Username with admin or club member degree
    private final String PASSWORD; //Password
    private boolean admin; //Whether user is admin or not
    private boolean clubMember; //Whether user is club member or not
    //Extra feature variables (liked and disliked films)
    private ArrayList<Film> likedFilms = new ArrayList<>(); //Liked films
    private ArrayList<Film> dislikedFilms = new ArrayList<>(); //Disliked films

    /**
     * Constructor that creates new standard user.
     * <p>
     * Standard:
     * <li>admin = false</li>
     * <li>clubMember = false</li>
     * </p>
     * @param username User's username
     * @param password User's hashed password
     */
    public User(String username, String password) {
        this.USERNAME = username;
        this.usernameWithDegree = username;
        this.PASSWORD = password;
        this.clubMember = false;
        this.admin = false;
    }

    /**
     * Constructor that creates new user with given parameters
     * @param username User's name
     * @param password User's hashed password
     * @param clubMember Whether the user is club member or not
     * @param admin Whether the user is admin or not
     */
    public User(String username, String password, boolean clubMember, boolean admin) {
        this.USERNAME = username;

        if (clubMember && admin) { //If user is both admin and club member
            this.usernameWithDegree = username + " (Admin - Club Member)"; //Add both degrees
        }
        else if (clubMember) { //If user is club member
            this.usernameWithDegree = username + " (Club Member)"; //Add club member degree
        }
        else if (admin) { //If user is admin
            this.usernameWithDegree = username + " (Admin)"; //Add admin degree
        }
        else { //If user is neither admin nor club member
            this.usernameWithDegree = username; //Add no degree
        }

        this.PASSWORD = password;
        this.clubMember = clubMember;
        this.admin = admin;
    }

    /**
     * @return User's username
     */
    public String getUsername() {
        return USERNAME;
    }

    /**
     * @return User's name edited by admin or club membership situation
     */
    public String getUsernameWithDegree() {
        return usernameWithDegree;
    }

    /**
     * Sets the username with degree when the admin or
     * club member situation changes
     */
    public void setUsernameWithDegree() {
        if (clubMember && admin) {
            this.usernameWithDegree = USERNAME + " (Admin - Club Member)";
        }
        else if (clubMember) {
            this.usernameWithDegree = USERNAME + " (Club Member)";
        }
        else if (admin) {
            this.usernameWithDegree = USERNAME + " (Admin)";
        }
        else {
            this.usernameWithDegree = USERNAME;
        }
    }

    /**
     * @return Whether user is admin or not
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the admin situation
     * @param admin New admin situation
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
        this.setUsernameWithDegree();
    }

    /**
     * @return Whether user is club member or not
     */
    public boolean isClubMember() {
        return clubMember;
    }

    /**
     * Sets the club member situation
     * @param clubMember New club member situation
     */
    public void setClubMember(boolean clubMember) {
        this.clubMember = clubMember;
        this.setUsernameWithDegree();
    }

    /**
     * Matches the user's name and password.
     * @param username Username that user enters to username field
     * @param password Hashed version of the password that user enters to password field
     * @param isBanned Whether the user can try to log in or not
     * @param userMap HashMap that keeps the users
     * @return Logged user
     * @throws BannedAttemptException If the user is banned and still tries to log in
     * @throws EmptyKeyException If one of the fields is empty
     * @throws UnmatchedKeyException If the username is not in database or password is wrong
     */

    public static User logIn(String username, String password, boolean isBanned, HashMap<String, User> userMap)
            throws UnmatchedKeyException, EmptyKeyException, BannedAttemptException {
        if (isBanned) { //If the user is banned
            throw new BannedAttemptException(); //Throw exception
        }
        if (username.equals("") || password.equals("1B2M2Y8AsgTpgAmY7PhCfg==")) { // If one of the fields are empty 
            //("1B2M2Y8AsgTpgAmY7PhCfg==" is the encoded format of empty string)
            throw new EmptyKeyException(); //Throw exception
        }
        else if (!userMap.containsKey(username)) { //If the username is not in database
            throw new UnmatchedKeyException(); //Throw exception
        }
        User user = userMap.get(username); //Get the user from the database
        if (!Objects.equals(password, user.PASSWORD)){ //If the password is wrong
            throw new UnmatchedKeyException(); //Throw exception
        }
        else { //If the username and password are correct
            return user; //Return the user
        }
    }

    /**
     * Creates new user and adds to the database
     * @param username Username that user enters to  username field
     * @param password1 Hashed version of the password that user enters to first password field
     * @param password2 Hashed version of the password that user enters to second password field
     * @param userMap HashMap that keeps the users
     * @throws EmptyKeyException If one of the fields are empty
     * @throws ExistingKeyException if there is a user with this username in the database
     * @throws UnmatchedKeyException If the username is not in database or password is wrong
     */
    public static void signUp(String username, String password1, String password2, HashMap<String, User> userMap)
            throws ExistingKeyException, UnmatchedKeyException, EmptyKeyException {
        if (username.equals("") || password1.equals("1B2M2Y8AsgTpgAmY7PhCfg==")
                || password2.equals("1B2M2Y8AsgTpgAmY7PhCfg==")) { //If one of the fields are empty
            throw new EmptyKeyException(); //Throw exception
        }
        else if (userMap.containsKey(username)) { //If there is a user with this username in the database
            throw new ExistingKeyException(); //Throw exception
        }
        else if (!password1.equals(password2)){ //If the passwords are not matched
            throw new UnmatchedKeyException(); //Throw exception
        }
        else { //If the username and password are correct
            userMap.put(username, new User(username, password1)); //Add the user to the database
        }
    }

    /**
     * Returns Base64 encoded version of MD5 hashed version of the given password
     * @param password Password to be hashed
     * @return Base64 encoded version of MD5 hashed version of password
     */
    public static String hashPassword(String password) {
        byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8); //Converts the password to bytes
        byte[] md5Digest; //MD5 hashed version of the password
        try {
            md5Digest = MessageDigest.getInstance("MD5").digest(bytesOfPassword); //Hashes the password with MD5 algorithm
        } catch (NoSuchAlgorithmException e) { //If there is no MD5 algorithm
            return null; //Return null
        }
        return Base64.getEncoder().encodeToString(md5Digest); //Returns Base64 encoded version of MD5 hashed version of password
    }

    //extra feature methods

    public ArrayList<Film> getLikedFilms() {
        return likedFilms;
    }

    public ArrayList<Film> getDislikedFilms() {
        return dislikedFilms;
    }

    /**
     * Likes the film
     * @param film Liked film
     */
    public void like(Film film) {
        likedFilms.add(film);
        film.addLike();
    }

    /**
     * Removes the like
     * @param film Film that like removed
     */
    public void unlike(Film film) {
        likedFilms.remove(film);
        film.removeLike();
    }

    /**
     * Dislikes the film
     * @param film Disliked film
     */
    public void dislike(Film film) {
        dislikedFilms.add(film);
        film.addDislike();
    }

    /**
     * Removes the dislike
     * @param film Film that dislike removed
     */
    public void undislike(Film film) {
        dislikedFilms.remove(film);
        film.removeDislike();
    }

    /**
     * Override because it's used in the load method
     * @return Properties of the user in accordance with the format in the "backup.dat"
     */
    @Override
    public String toString() {
        return "user\t" + USERNAME + "\t" + PASSWORD +
                "\t" + clubMember + "\t" + admin + "\n";
    }
}
