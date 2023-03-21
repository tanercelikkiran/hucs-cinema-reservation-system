package com.hucs_cinema.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.hucs_cinema.gui.LogInPane;
import com.hucs_cinema.object.*;

public class Main extends Application {

    //These are static variables because they are used in multiple classes
    public static final HashMap<String, User> USER_MAP = new HashMap<>(); //username, user
    public static final HashMap<String, Film> FILM_MAP = new HashMap<>(); //name, film
    public static final HashMap<String, Hall> HALL_MAP = new HashMap<>(); //name, hall

    public static Stage stage; //Main stage

    public static User currentUser = null; //Current user
    public static Film chosenFilm = null; //Chosen film
    public static Hall chosenHall = null; //Chosen hall

    public static final PropertyReader PR = new PropertyReader(); //Property reader
    public static final String TITLE = PR.getTitle(); //Title of the program
    public static final int MAX_ERROR_NUM = PR.getMaxErrorNum(); //Maximum number of wrong attempts
    public static final int DISCOUNT_PERCENTAGE = PR.getDiscountPercentage(); //Discount percentage for club members
    public static final int BLOCK_TIME = PR.getBlockTime(); //Block time for banned users

    public static final String LOGO_PATH = new File("./assets/icons/logo.png").toURI().toString(); //Logo path
    public static final String ERROR_SOUND_PATH = new File("./assets/effects/error.mp3").toURI().toString(); //Error sound path

    //These are atomic variables because they were used in lambda expression
    public static AtomicBoolean isBanned = new AtomicBoolean(false); //Is the user banned?
    public static AtomicInteger wrongAttempt = new AtomicInteger(0); //Number of wrong attempts
    public static AtomicLong banStart = new AtomicLong(0); //Ban start time

    public static boolean isExtraLoaded; //Is extra data loaded?

    /**
     * Backs up the user, film, hall and seats to "backup.dat"
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage; //Sets the main stage
        primaryStage.getIcons().add(new Image(LOGO_PATH)); //Sets the logo
        primaryStage.setTitle(TITLE); //Sets the title
        primaryStage.setOnCloseRequest(e -> backup()); //Backs up the data when the program is closed
        primaryStage.setScene(new Scene(new LogInPane())); //Sets the scene
        primaryStage.show(); //Shows the stage
    }

    public static void main(String[] args) {
        load(); //Loads the data
        launch(args); //Launches the program
    }

    /**
     * Loads the user, film, hall and seats from "backup.dat" and loads "extra.dat"
     */
    public static void load(){

        //"backup.dat" loader
        try (Scanner backup = new Scanner(new File("./assets/data/backup.dat"))) {
            String backupLine; //Line of the file
            while (backup.hasNextLine()) { //While there is a line
                backupLine = backup.nextLine(); //Reads the line
                String[] list = backupLine.split("\t"); //Splits the line by tab
                switch (list[0]) {
                    case "user":
                        String username = list[1]; //Username
                        String password = list[2]; //Password
                        boolean isClubMember = Boolean.parseBoolean(list[3]); //Is club member?
                        boolean isAdmin = Boolean.parseBoolean(list[4]); //Is admin?

                        USER_MAP.put(username ,new User(username, password, isClubMember, isAdmin)); //Adds the user to the map
                        break;
                    case "film":
                        String filmName = list[1]; //Film name
                        String path = list[2]; //Path
                        int duration = Integer.parseInt(list[3]); //Duration

                        FILM_MAP.put(filmName ,new Film(filmName, path, duration)); //Adds the film to the map
                        break;
                    case "hall":
                        String filmOfHallName = list[1]; //Film name of the hall
                        String hallName = list[2]; //Hall name
                        int price = Integer.parseInt(list[3]); //Price
                        int row = Integer.parseInt(list[4]); //Row
                        int column = Integer.parseInt(list[5]); //Column
                        Film filmOfHall = FILM_MAP.get(filmOfHallName); //Gets the film of the hall
                        Hall hall = new Hall(filmOfHall, hallName, price, row, column); //Creates the hall

                        filmOfHall.addHall(hall); //Adds the hall to the film
                        HALL_MAP.put(hallName ,hall); //Adds the hall to the map
                        break;
                    case "seat":
                        String seatOfHallName = list[2]; //Hall name of the seat
                        int rowOfSeat = Integer.parseInt(list[3]); //Row of the seat
                        int columnOfSeat = Integer.parseInt(list[4]); //Column of the seat
                        String ownerOfSeat = list[5]; //Owner of the seat
                        int priceOfBought = Integer.parseInt(list[6]); //Price of the bought seat
                        Hall seatOfHall = HALL_MAP.get(seatOfHallName); //Gets the hall of the seat

                        if (!(ownerOfSeat.equals("null"))) { //If the seat is bought
                            User owner = USER_MAP.get(ownerOfSeat); //Gets the owner of the seat
                            seatOfHall.addSeat(new Seat(seatOfHall, rowOfSeat, columnOfSeat, owner, priceOfBought)); //Adds the seat to the hall
                        }
                        else { //If the seat is not bought
                            seatOfHall.addSeat(new Seat(seatOfHall, rowOfSeat, columnOfSeat, null, priceOfBought)); //Adds the seat to the hall as null 
                        }
                        break;
                }
            }
        }
        catch (Exception e) { //If there is an error
            //Creates the default user admin with password
            USER_MAP.put("admin", new User("admin", "password", true, true));
        }

        //"extra.dat" loader
        extra:
        try (Scanner extra = new Scanner(new File ("./assets/data/extra.dat"))){
            isExtraLoaded = true; //Sets the extra data loaded to true
            String extraLine; //Line of the file
            if (FILM_MAP.isEmpty()) { //If there is no film
                break extra; //Breaks the loop
            }
            while (extra.hasNextLine()) { //While there is a line
                extraLine = extra.nextLine(); //Reads the line
                String[] extraList = extraLine.split("\t"); //Splits the line by tab

                User user = USER_MAP.get(extraList[0]); //Gets the user

                Film film = FILM_MAP.get(extraList[1]); //Gets the film

                boolean isLiked = Boolean.parseBoolean(extraList[2]); //Is the film liked?

                if (isLiked) { //If the film is liked
                    user.like(film); //Likes the film
                }
                else { //If the film is not liked
                    user.dislike(film); //Dislikes the film
                }
            }
        } catch (FileNotFoundException e) { //If there is an error
            isExtraLoaded = false; //Sets the extra data loaded to false
        }
    }

    /**
     * Rewrites the final state of the database to "backup.dat" and "extra.dat"
     */
    public static void backup() {
        File backup = new File("./assets/data/backup.dat"); //Backup file
        try (BufferedWriter backupWriter = new BufferedWriter(new FileWriter(backup, false))) { //Buffered writer
            for (User user: USER_MAP.values()) { //For each user
                backupWriter.write(user.toString()); //Writes the user
            }
            for (Film film: FILM_MAP.values()) { //For each film
                backupWriter.write(film.toString()); //Writes the film
                for (Hall hall: film.getHallList()) { //For each hall of the film
                    backupWriter.write(hall.toString()); //Writes the halls
                    for (Seat seat: hall.getSeatList()) { //For each seat of the hall
                        backupWriter.write(seat.toString()); //Writes the seats
                    }
                }
            }
        }
        catch (Exception e) { //If there is an error
            e.printStackTrace(); //Prints the error
        }

        if (isExtraLoaded) { //If the extra data is loaded
            File extra = new File("./assets/data/extra.dat"); //Extra file
            try (BufferedWriter extraWriter = new BufferedWriter(new FileWriter(extra, false))) { //Buffered writer
                for (User user: USER_MAP.values()) { //For each user
                    for (Film likedFilm: user.getLikedFilms()) { //For each liked film of the user
                        extraWriter.write(user.getUsername() + "\t" + likedFilm.getName() + "\t"+ "true\n"); //Writes the liked film
                    }
                    for (Film dislikedFilm: user.getDislikedFilms()) { //For each disliked film of the user
                        extraWriter.write(user.getUsername() + "\t" + dislikedFilm.getName() + "\t"+ "false\n"); //Writes the disliked film
                    }
                }
            }
            catch (Exception e) { //If there is an error
                e.printStackTrace(); //Prints the error
            }
        }
    }

    /**
     * Plays the error sound
     */
    public static void playErrorSound() {
        MediaPlayer player = new MediaPlayer(new Media(ERROR_SOUND_PATH)); //Creates the media player
        player.play(); //Plays the sound
    }
}