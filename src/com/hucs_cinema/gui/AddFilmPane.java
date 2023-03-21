package com.hucs_cinema.gui;

import java.io.FileNotFoundException;

import com.hucs_cinema.exception.EmptyKeyException;
import com.hucs_cinema.exception.ExistingKeyException;
import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Film;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AddFilmPane extends StackPane {

    /**
     * Constructor for add film screen
     */
    public AddFilmPane() {

        Text infoText = new Text("Please give name, relative path of the trailer and duration of the film.\n"); // info text
        infoText.setTextAlignment(TextAlignment.CENTER); // center the text

        GridPane paneForFilm = new GridPane(); // pane for film info
        paneForFilm.setAlignment(Pos.CENTER); // center the pane
        paneForFilm.setHgap(10); // set horizontal gap
        paneForFilm.setVgap(10); // set vertical gap

        TextField nameField = new TextField(); // text field for film name
        TextField pathField = new TextField(); // text field for film path
        TextField durationField = new TextField(); // text field for film duration

        paneForFilm.add(new Label("Name:"), 0, 0); // add name label
        paneForFilm.add(nameField, 1, 0); // add name field

        paneForFilm.add(new Label("Trailer (Path):"), 0, 1); // add path label
        paneForFilm.add(pathField, 1, 1); // add path field

        paneForFilm.add(new Label("Duration:"), 0, 2); // add duration label
        paneForFilm.add(durationField,1, 2); // add duration field

        Label infoLabel = new Label(""); // info label
        infoLabel.setPadding(new Insets(10, 0, 0, 0)); // set padding
        infoLabel.setTextAlignment(TextAlignment.CENTER); // center the text

        Button btBack = new Button("<- BACK"); // back button
        btBack.setOnAction(e -> Main.stage.setScene(new Scene(new UserPane(Main.currentUser)))); // set action for back button (go back to user pane)

        Button btOK = new Button("OK"); // ok button
        btOK.setOnAction(e -> { // set action for ok button (add film)
            String filmName = nameField.getText(); // get film name
            String filmPath = pathField.getText(); // get film path
            String filmDuration = durationField.getText(); // get film duration
            try { // try to add film
                Film.addFilm(filmName, filmPath, filmDuration, Main.FILM_MAP); // add film
                nameField.clear(); pathField.clear(); durationField.clear(); // clear text fields
                infoLabel.setText("SUCCESS: Film successfully created!"); // set info label
            }
            catch (EmptyKeyException ex) { // if there is an empty key
                if (filmName.equals("")) { // if film name is empty
                    infoLabel.setText("ERROR: Film name could not be empty!"); // set info label (Film name could not be empty)
                    Main.playErrorSound(); // play error sound
                }
                else if (filmPath.equals("")) { // if film path is empty
                    infoLabel.setText("ERROR: Trailer path could not be empty!"); // set info label (Trailer path could not be empty)
                    Main.playErrorSound(); // play error sound
                }
                else {
                    infoLabel.setText("ERROR: Duration could not be empty!"); // set info label (Duration could not be empty)
                    Main.playErrorSound(); // play error sound
                }
            }
            catch (NumberFormatException ex) { // if duration is not a number
                infoLabel.setText("ERROR: Duration has to be a positive integer!"); // set info label (Duration has to be a positive integer)
                durationField.clear(); // clear duration field
                Main.playErrorSound(); // play error sound
            }
            catch (FileNotFoundException ex) { // if there is no such a trailer
                infoLabel.setText("ERROR: There is no such a trailer!"); // set info label (There is no such a trailer)
                pathField.clear(); // clear path field
                Main.playErrorSound(); // play error sound
            }
            catch (ExistingKeyException ex) { // if film already exists
                infoLabel.setText("ERROR: Film already exists!"); // set info label (Film already exists)
                nameField.clear(); // clear name field
                Main.playErrorSound(); // play error sound
            }
        });

        paneForFilm.add(btBack, 0, 3); // adding back button
        GridPane.setHalignment(btBack, HPos.LEFT); // setting back button to left
        paneForFilm.add(btOK, 1, 3); // adding ok button
        GridPane.setHalignment(btOK, HPos.RIGHT); // setting ok button to right

        BorderPane mainPane = new BorderPane(); // main pane
        mainPane.setPadding(new Insets(10, 10, 10, 20)); // setting padding
        mainPane.setMaxHeight(getHeight()); // setting max height

        mainPane.setTop(infoText); // adding info text
        BorderPane.setAlignment(infoText, Pos.CENTER); // centering the text

        mainPane.setCenter(paneForFilm); // adding pane for film
        BorderPane.setAlignment(paneForFilm, Pos.CENTER); // centering the pane

        mainPane.setBottom(infoLabel); // adding info label
        BorderPane.setAlignment(infoLabel, Pos.CENTER); // centering the label

        getChildren().add(mainPane); // adding main pane
    }
}