package com.hucs_cinema.object;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.exception.*;

public class Film {
    private final String NAME; // Name of the film
    private final String NAME_WITH_DURATION; // Name of the film with the duration
    private final int DURATION; // Duration of the film
    private final String PATH; // Path of the film
    private final String RELATIVE_PATH; // Relative path of the film (for database)
    private final ArrayList<Hall> HALL_LIST = new ArrayList<>(); // List of the halls that belongs to this movie
    //Extra feature variables
    private int likeNum; // Number of likes
    private int dislikeNum; // Number of dislikes

    /**
     * Constructor for film
     * @param name Name of the film
     * @param path Relative path of the film
     * @param duration Duration of the film
     */
    public Film(String name, String path, int duration) {
        this.NAME = name;
        this.NAME_WITH_DURATION = name + " (" + duration + " minutes)";
        this.RELATIVE_PATH = path;
        this.PATH = "./assets/trailers/" + path;
        this.DURATION = duration;
    }

    /**
     * @return Film's name
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return Film's name with the duration
     */
    public String getNameWithDuration() {
        return NAME_WITH_DURATION;
    }

    /**
     * @return ArrayList of the halls that belongs to this movie
     */
    public ArrayList<Hall> getHallList() {
        return HALL_LIST;
    }

    /**
     * @return Trailer path of the film
     */
    public String getPath() {
        return PATH;
    }

    /**
     * Adds new hall to the hall list
     * @param hall Hall that wanted to add
     */
    public void addHall(Hall hall){
        this.HALL_LIST.add(hall);
    }

    /**
     * Removes the hall from hall list
     * @param hall Hall that wanted to be removed
     */
    public void removeHall(Hall hall) {
        this.HALL_LIST.remove(hall);
    }

    /**
     * Creates new film with given parameters and adds to the database
     * @param name Name of the new film
     * @param path Relative path
     * @param duration Duration
     * @param filmMap HashMap that keeps the films
     * @throws EmptyKeyException If one of the parameters is empty
     * @throws FileNotFoundException If there is no such trailer in the given path
     * @throws NumberFormatException If the duration is such a string or non-positive number
     * @throws ExistingKeyException If the film is already exists
     */
    public static void addFilm(String name, String path, String duration, HashMap<String, Film> filmMap)
            throws EmptyKeyException, ExistingKeyException, FileNotFoundException {

        if (name.equals("") || path.equals("") || duration.equals("")) { // If one of the parameters is empty
            throw new EmptyKeyException(); // Throw exception
        }

        int filmDuration = Integer.parseInt(duration); // Parse duration to int

        if (filmDuration <= 0) { // If the duration is non-positive number
            throw new NumberFormatException(); // Throw exception (NumberFormatException is a subclass of IllegalArgumentException)
        }
        else if (filmMap.containsKey(name)) { // If the film is already exists
            throw new ExistingKeyException(); // Throw exception
        }
        filmMap.put(name, new Film(name, path, filmDuration)); // Add new film to the database
    }

    /**
     * Removes the given film and removes its halls from database
     * @param film The film that user want to remove
     * @param filmMap HashMap that keeps the films
     * @param hallMap HashMap that keeps the halls
     */
    public static void removeFilm(Film film, HashMap<String, Film> filmMap, HashMap<String, Hall> hallMap) {
        for (User user: Main.USER_MAP.values()) { // For every user
            if (user.getLikedFilms().contains(film)) { // If the user liked the film
                user.unlike(film); // Remove the film from the liked list
            }
            else if (user.getDislikedFilms().contains(film)) { // If the user disliked the film
                user.undislike(film); // Remove the film from the disliked list
            }
        }
        for (Hall hall: film.HALL_LIST) { // For every hall that belongs to the film
            hallMap.remove(hall.getName()); // Remove the hall from the database
        }
        filmMap.remove(film.getName()); // Remove the film from the database
    }

    //Extra feature methods

    /**
     * @return How many likes does this film have
     */
    public int getLikeNum() {
        return likeNum;
    }

    /**
     * Adds one like
     */
    public void addLike() {
        this.likeNum++;
    }

    /**
     * Removes one like
     */
    public void removeLike() {
        this.likeNum--;
    }

    /**
     * @return How many likes does this film have
     */
    public int getDislikeNum() {
        return dislikeNum;
    }

    /**
     * Adds one dislike
     */
    public void addDislike() {
        this.dislikeNum++;
    }

    /**
     * Removes one dislike
     */
    public void removeDislike() {
        this.dislikeNum--;
    }

    /**
     * Override because it's used in load method
     * @return Properties of the film in accordance with the format in the "backup.dat"
     */
    @Override
    public String toString() {
        return "film\t" + NAME + "\t" + RELATIVE_PATH + "\t" + DURATION + "\n";
    }
}
