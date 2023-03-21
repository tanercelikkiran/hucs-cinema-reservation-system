package com.hucs_cinema.gui;

import com.hucs_cinema.exception.*;
import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Hall;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AddHallPane extends StackPane {

    /**
     * Constructor for adding hall screen
     */
    public AddHallPane() {
        Text filmInfoText = new Text(Main.chosenFilm.getNameWithDuration() + "\n"); // info text for film name and
                                                                                    // duration
        filmInfoText.setTextAlignment(TextAlignment.CENTER); // center the text

        ComboBox<Integer> hallRow = new ComboBox<>(); // combo box for hall row
        hallRow.getItems().addAll(3, 4, 5, 6, 7, 8, 9, 10); // add items to combo box
        hallRow.getSelectionModel().selectFirst(); // select first item as default

        ComboBox<Integer> hallColumn = new ComboBox<>(); // combo box for hall column
        hallColumn.getItems().addAll(3, 4, 5, 6, 7, 8, 9, 10); // add items to combo box
        hallColumn.getSelectionModel().selectFirst(); // select first item as default

        TextField nameField = new TextField(); // text field for hall name
        TextField priceField = new TextField(); // text field for hall price

        Label infoLabel = new Label(""); // info label
        infoLabel.setPadding(new Insets(10)); // set padding

        GridPane hallSettingsPane = new GridPane(); // pane for hall settings
        hallSettingsPane.setAlignment(Pos.CENTER); // center the pane
        hallSettingsPane.setVgap(10); // set vertical gap

        hallSettingsPane.add(new Label("Row:"), 0, 0); // add row label
        hallSettingsPane.add(hallRow, 1, 0); // add row combo box
        GridPane.setHalignment(hallRow, HPos.CENTER); // center the combo box

        hallSettingsPane.add(new Label("Column:"), 0, 1); // add column label
        hallSettingsPane.add(hallColumn, 1, 1); // add column combo box
        GridPane.setHalignment(hallColumn, HPos.CENTER); // center the combo box

        hallSettingsPane.add(new Label("Name:"), 0, 2); // add name label
        hallSettingsPane.add(nameField, 1, 2); // add name text field

        hallSettingsPane.add(new Label("Price"), 0, 3); // add price label
        hallSettingsPane.add(priceField, 1, 3); // add price text field

        Button btOK = new Button("OK"); // ok button for adding hall
        btOK.setOnAction(e -> { // set on action for ok button (add hall)
            String hallName = nameField.getText(); // get hall name
            String hallPrice = priceField.getText(); // get hall price
            int row = hallRow.getValue(); // get hall row
            int column = hallColumn.getValue(); // get hall column
            try { // try to add hall
                Hall.addHall(Main.chosenFilm, hallName, hallPrice, row, column, Main.HALL_MAP); // add hall
                nameField.clear();
                priceField.clear(); // clear text fields
                hallRow.getSelectionModel().selectFirst(); // select first item as default
                hallColumn.getSelectionModel().selectFirst(); // select first item as default
                infoLabel.setText("SUCCESS: Hall successfully created!"); // set info label
            } catch (EmptyKeyException ex) { // if hall name or price is empty
                if (hallName.equals("")) { // if hall name is empty
                    infoLabel.setText("ERROR: Hall name could not be empty!"); // set info label (Hall name could not be
                                                                               // empty)
                    Main.playErrorSound(); // play error sound
                } else { // if hall price is empty
                    infoLabel.setText("ERROR: Price could not be empty!"); // set info label (Price could not be empty)
                    Main.playErrorSound(); // play error sound
                }
            } catch (NumberFormatException ex) { // if price is not a number
                infoLabel.setText("ERROR: Price has to be a positive integer!"); // set info label (Price has to be a
                                                                                 // positive integer)
                priceField.clear(); // clear price text field
                Main.playErrorSound(); // play error sound
            } catch (ExistingKeyException ex) { // if hall name already exists
                infoLabel.setText("ERROR: Hall name already exists!"); // set info label (Hall name already exists)
                nameField.clear(); // clear name text field
                Main.playErrorSound(); // play error sound
            }
        });

        Button btBack = new Button("<- BACK"); // back button for going back to trailer pane
        btBack.setOnAction(e -> Main.stage.setScene(new Scene(new TrailerPane()))); // set on action for back button (go
                                                                                    // back to trailer pane)

        hallSettingsPane.add(btBack, 0, 4); // add back button
        GridPane.setHalignment(btBack, HPos.LEFT); // left align the back button

        hallSettingsPane.add(btOK, 1, 4); // add ok button
        GridPane.setHalignment(btOK, HPos.RIGHT); // right align the ok button

        BorderPane mainPane = new BorderPane(); // main pane
        mainPane.setMaxHeight(getHeight()); // set max height
        mainPane.setPadding(new Insets(10, 10, 10, 20)); // set padding

        mainPane.setTop(filmInfoText); // add film info text
        BorderPane.setAlignment(filmInfoText, Pos.CENTER); // center the film info text

        mainPane.setCenter(hallSettingsPane); // add hall settings pane
        mainPane.setBottom(infoLabel); // add info label
        BorderPane.setAlignment(infoLabel, Pos.CENTER); // center the info label

        getChildren().add(mainPane); // add main pane to stack pane
    }
}