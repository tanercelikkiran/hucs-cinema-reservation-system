package com.hucs_cinema.gui;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Film;
import com.hucs_cinema.object.User;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class UserPane extends StackPane {

    /**
     * Constructor for user screen
     * 
     * @param currentUser Logged user
     */
    public UserPane(User currentUser) {
        /*
         * If the user switches from the trailer screen to this screen,
         * user can choose a different film. So the currently selected
         * film made null.
         */
        Main.chosenFilm = null; // Set chosen film to null (no film is chosen)

        Text infoText = new Text("Welcome " + currentUser.getUsernameWithDegree() + "!\n" +
                "Select a film and then click OK to continue.\n"); // Info text
        infoText.setTextAlignment(TextAlignment.CENTER); // Align text to center

        List<Film> filmList = new ArrayList<>(Main.FILM_MAP.values()); // List of films
        ComboBox<Film> filmBox = new ComboBox<>(); // Combo box for films
        filmBox.setItems(FXCollections.observableList(filmList)); // Set items in combo box
        filmBox.setEditable(false); // Disable editing
        filmBox.getSelectionModel().selectFirst(); // Select first item as default

        /*
         * For all combo box nodes, it looks wrong if these two
         * methods are not written. It simply makes the names of
         * the objects appear (eg "nebiyilmaz"), not the
         * object's default names (eg "User @134").
         */

        // Method 1
        filmBox.setCellFactory(new Callback<ListView<Film>, ListCell<Film>>() {
            public ListCell<Film> call(ListView<Film> param) {
                return new ListCell<Film>() {
                    @Override
                    protected void updateItem(Film item, boolean empty) {
                        getListView().setMinWidth(ListView.USE_PREF_SIZE);
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        // Method 2
        filmBox.setConverter(new StringConverter<Film>() {
            @Override
            public String toString(Film film) {
                if (film == null) {
                    return null;
                } else {
                    return film.getName();
                }
            }

            public Film fromString(String string) {
                return null;
            }
        });

        Button btOK = new Button("OK"); // OK button
        btOK.setMinWidth(41); // Set minimum width
        btOK.setOnAction(e -> { // Set action
            Main.chosenFilm = filmBox.getValue(); // Set chosen film
            if (Main.chosenFilm != null) { // If a film is chosen
                Main.stage.setScene(new Scene(new TrailerPane())); // Go to trailer screen
            }
        });

        Button btLogOut = new Button("LOG OUT"); // Log out button
        btLogOut.setOnAction(e -> Main.stage.setScene(new Scene(new LogInPane()))); // Set action

        HBox boxPane = new HBox(15); // HBox for combo box and OK button
        boxPane.getChildren().addAll(filmBox, btOK); // Add combo box and OK button
        boxPane.setAlignment(Pos.CENTER); // Align to center

        GridPane centerPane = new GridPane(); // Grid pane for center
        centerPane.setVgap(20); // Set vertical gap
        centerPane.add(boxPane, 0, 0); // Add combo box and OK button
        GridPane.setHalignment(boxPane, HPos.CENTER); // Align to center
        centerPane.add(btLogOut, 0, 2); // Add log out button
        GridPane.setHalignment(btLogOut, HPos.RIGHT); // Align to right
        centerPane.setAlignment(Pos.CENTER); // Align to center

        /*
         * If the logged user is admin, these additional
         * buttons are added. "Add Film", "Remove Film", "Edit Users".
         */
        if (currentUser.isAdmin()) {
            Button btAddFilm = new Button("Add Film"); // Add film button
            btAddFilm.setOnAction(e -> Main.stage.setScene(new Scene(new AddFilmPane()))); // Set action for add film
                                                                                           // button

            Button btRemoveFilm = new Button("Remove Film"); // Remove film button
            btRemoveFilm.setOnAction(e -> Main.stage.setScene(new Scene(new RemoveFilmPane()))); // Set action for
                                                                                                 // remove film button

            Button btEdit = new Button("Edit Users"); // Edit users button
            btEdit.setOnAction(e -> Main.stage.setScene(new Scene(new EditUsersPane()))); // Set action for edit users
                                                                                          // button

            HBox buttonPane = new HBox(10); // HBox for buttons
            buttonPane.getChildren().addAll(btAddFilm, btRemoveFilm, btEdit); // Add buttons
            buttonPane.setAlignment(Pos.CENTER); // Align to center

            centerPane.add(buttonPane, 0, 1); // Add buttons
        }

        BorderPane mainPane = new BorderPane(); // Border pane for main pane
        mainPane.setMaxHeight(getHeight()); // Set maximum height
        mainPane.setPadding(new Insets(20)); // Set padding

        mainPane.setTop(infoText); // Add info text
        BorderPane.setAlignment(infoText, Pos.CENTER); // Align to center

        mainPane.setCenter(centerPane); // Add center pane
        BorderPane.setAlignment(centerPane, Pos.CENTER); // Align to center

        getChildren().add(mainPane); // Add main pane
    }
}