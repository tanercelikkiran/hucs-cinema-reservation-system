package com.hucs_cinema.gui;

import java.util.ArrayList;
import java.util.List;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Film;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class RemoveFilmPane extends StackPane {

    /**
     * Constructor for remove film screen
     */
    public RemoveFilmPane() {
        Text infoText = new Text("Select the film that you desire to remove and then click OK.\n"); // Info text
        infoText.setTextAlignment(TextAlignment.CENTER); // Align text to center

        List<Film> filmList = new ArrayList<>(Main.FILM_MAP.values()); // List of films

        ComboBox<Film> filmBox = new ComboBox<>(); // Combo box for films

        filmBox.setItems(FXCollections.observableList(filmList)); // Set items in combo box
        filmBox.setEditable(false); // Disable editing
        filmBox.getSelectionModel().selectFirst(); // Select first item

        filmBox.setCellFactory(new Callback<ListView<Film>, ListCell<Film>>() { // Set cell factory
            @Override
            public ListCell<Film> call(ListView<Film> param) { // Call method for cell factory
                return new ListCell<Film>() { // Return new list cell
                    @Override
                    protected void updateItem(Film item, boolean empty) { // Update item method
                        getListView().setMinWidth(ListView.USE_PREF_SIZE); // Set min width
                        super.updateItem(item, empty); // Call super method
                        if (item == null || empty) { // If item is null or empty
                            setText(null); // Set text to null
                        } else {
                            setText(item.getName()); // Set text to film name
                        }
                    }
                };
            }
        });
        filmBox.setConverter(new StringConverter<Film>() { // Set string converter
            @Override
            public String toString(Film film) { // Convert to string method
                if (film == null) { // If film is null
                    return null; // Return null
                } else { // Else
                    return film.getName(); // Return film name
                }
            }

            @Override
            public Film fromString(String string) { // Convert from string method
                return null; // Return null
            }
        });

        Button btOK = new Button("OK"); // OK button
        btOK.setOnAction(e -> { // Set on action
            Film removedFilm = filmBox.getValue(); // Get selected film
            if (removedFilm != null) { // If film is not null
                Film.removeFilm(removedFilm, Main.FILM_MAP, Main.HALL_MAP); // Remove film
                List<Film> tempFilmList = new ArrayList<>(Main.FILM_MAP.values()); // Create new list of films
                filmBox.setItems(FXCollections.observableList(tempFilmList)); // Set items in combo box
                filmBox.getSelectionModel().selectFirst(); // Select first item
            }
        });

        Button btBack = new Button("<- BACK"); // Back button
        btBack.setOnAction(e -> Main.stage.setScene(new Scene(new UserPane(Main.currentUser)))); // Set on action to go
                                                                                                 // back to user pane

        HBox buttonPane = new HBox(15); // HBox for buttons
        buttonPane.getChildren().addAll(btBack, btOK); // Add buttons to HBox
        buttonPane.setAlignment(Pos.CENTER); // Align HBox to center
        buttonPane.setPadding(new Insets(15, 0, 0, 0)); // Set padding

        BorderPane mainPane = new BorderPane(); // Border pane for main pane
        mainPane.setMaxHeight(getHeight()); // Set max height
        mainPane.setPadding(new Insets(20)); // Set padding

        mainPane.setTop(infoText); // Add info text to top
        BorderPane.setAlignment(infoText, Pos.CENTER); // Align info text to center

        mainPane.setCenter(filmBox); // Add film box to center
        BorderPane.setAlignment(filmBox, Pos.CENTER); // Align film box to center

        mainPane.setBottom(buttonPane); // Add button pane to bottom
        BorderPane.setAlignment(buttonPane, Pos.CENTER); // Align button pane to center

        getChildren().add(mainPane); // Add main pane to children
    }
}