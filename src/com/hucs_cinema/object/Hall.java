package com.hucs_cinema.object;

import java.util.ArrayList;
import java.util.HashMap;

import com.hucs_cinema.exception.EmptyKeyException;
import com.hucs_cinema.exception.ExistingKeyException;

public class Hall {

    private final Film FILM_OF_HALL; // Film that hall belongs to
    private final String NAME; // Hall's name
    private final int PRICE; // Hall's price
    private final int ROW; // Hall's row number
    private final int COLUMN; // Hall's column number
    private final ArrayList<Seat> SEAT_LIST = new ArrayList<>(); // List of the seats that belongs to this hall

    /**
     * Creates the hall with given parameters
     * @param film Which film does the hall belong to
     * @param name Hall's name
     * @param price Hall's price
     * @param row Hall's row number
     * @param column Hall's column number
     */
    public Hall(Film film, String name, int price, int row, int column) {
        this.FILM_OF_HALL = film;
        this.NAME = name;
        this.PRICE = price;
        this.ROW = row;
        this.COLUMN = column;
    }

    /**
     * @return Film that hall belongs to
     */
    public Film getFilmOfHall() {
        return FILM_OF_HALL;
    }

    /**
     * @return Hall's name
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return Hall's price
     */
    public int getPrice() {
        return PRICE;
    }

    /**
     * Adds seats to the hall
     * @param seat Seat to be added
     */
    public void addSeat(Seat seat) {
        SEAT_LIST.add(seat);
    }

    /**
     * Returns the list of the hall's seats
     * @return ArrayList of the seats
     */
    public ArrayList<Seat> getSeatList() {
        return SEAT_LIST;
    }

    /**
     * Adds a new hall to the given film.
     * @param film Which film does the hall belong to
     * @param name Hall's name
     * @param price Hall's price
     * @param row Hall's row number
     * @param column Hall's column number
     * @param hallMap HashMap that keeps halls
     * @throws ExistingKeyException If there is a hall with given name
     * @throws EmptyKeyException If one of the fields is empty
     */
    public static void addHall(Film film, String name, String price, int row, int column, HashMap<String, Hall> hallMap)
            throws ExistingKeyException, EmptyKeyException {
        if (name.equals("") || price.equals("")) { // If one of the parameters is empty
            throw new EmptyKeyException(); // Throw exception
        }

        int hallPrice = Integer.parseInt(price); // Hall's price

        if (hallPrice <= 0) { // If the price is non-positive
            throw new NumberFormatException(); // Throw exception
        }
        else if (hallMap.containsKey(name)) { // If the hall is already exists
            throw new ExistingKeyException(); // Throw exception
        }

        Hall hall = new Hall(film, name, hallPrice, row, column); // Create the hall

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                hall.addSeat(new Seat(hall, i, j, null, 0)); // Add seats to the hall
            }
        }
        hallMap.put(name, hall); // Add the hall to the HashMap

        film.addHall(hall); // Add the hall to the film
    }

    /**
     * Removes the hall from database and the film that hall belongs to
     * @param hall Hall that wanted to be removed
     * @param hallMap HashMap that keeps the halls
     */
    public static void removeHall(Hall hall, HashMap<String, Hall> hallMap) {
        hall.FILM_OF_HALL.removeHall(hall); // Remove the hall from the film
        hallMap.remove(hall.NAME); // Remove the hall from the HashMap
    }

    /**
     * Override because it's used in load method
     * @return Properties of the hall in accordance with the format in the "backup.dat"
     */
    @Override
    public String toString() {
        return "hall\t" + FILM_OF_HALL.getName() + "\t" + NAME + "\t" +
                PRICE + "\t" + ROW + "\t" + COLUMN + "\n";
    }
}
