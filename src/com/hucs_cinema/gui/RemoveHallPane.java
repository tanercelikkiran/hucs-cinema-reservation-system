package com.hucs_cinema.gui;

import java.util.List;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Hall;

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

public class RemoveHallPane extends StackPane {

    /**
     * Constructor for remove hall screen
     */
    public RemoveHallPane() {

        Text infoText = new Text("Select the hall that you desire to remove from " +
                Main.chosenFilm.getName() + "and then click OK.\n"); // Info text
        infoText.setTextAlignment(TextAlignment.CENTER); // Align text to center

        List<Hall> hallList = Main.chosenFilm.getHallList(); // List of halls

        ComboBox<Hall> hallBox = new ComboBox<>(); // Combo box for halls

        hallBox.setItems(FXCollections.observableList(hallList)); // Set items in combo box
        hallBox.setEditable(false); // Disable editing
        hallBox.getSelectionModel().selectFirst(); // Select first item as default
        hallBox.setCellFactory(new Callback<ListView<Hall>, ListCell<Hall>>() { // Set cell factory for combo box
            @Override
            public ListCell<Hall> call(ListView<Hall> param) { // Call method for cell factory
                return new ListCell<Hall>() {
                    @Override
                    protected void updateItem(Hall item, boolean empty) { // Update item method
                        getListView().setMinWidth(ListView.USE_PREF_SIZE); // Set min width
                        super.updateItem(item, empty); // Call super method
                        if (item == null || empty) { // If item is null or empty
                            setText(null); // Set text to null
                        } else { // If item is not null or empty
                            setText(item.getName()); // Set text to hall name
                        }
                    }
                };
            }
        });
        hallBox.setConverter(new StringConverter<Hall>() { // Set string converter for combo box
            @Override
            public String toString(Hall hall) { // Convert hall to string
                if (hall == null) { // If hall is null
                    return null; // Return null
                } else { // If hall is not null
                    return hall.getName(); // Return hall name
                }
            }

            @Override
            public Hall fromString(String string) { // Convert string to hall
                return null; // Return null
            }
        });

        Button btOK = new Button("OK"); // OK button
        btOK.setOnAction(e -> { // Set on action for OK button
            Hall removedHall = hallBox.getValue(); // Get selected hall
            if (removedHall != null) { // If hall is not null
                Hall.removeHall(removedHall, Main.HALL_MAP); // Remove hall from map
                List<Hall> tempHallList = Main.chosenFilm.getHallList(); // Get list of halls
                hallBox.setItems(FXCollections.observableList(tempHallList)); // Set items in combo box
                hallBox.getSelectionModel().selectFirst(); // Select first item as default
            }
        });

        Button btBack = new Button("<- BACK"); // Back button
        btBack.setOnAction(e -> Main.stage.setScene(new Scene(new TrailerPane()))); // Set on action for back button as
                                                                                    // go to trailer pane

        HBox buttonPane = new HBox(15); // HBox for buttons
        buttonPane.getChildren().addAll(btBack, btOK); // Add buttons to HBox
        buttonPane.setAlignment(Pos.CENTER); // Align HBox to center
        buttonPane.setPadding(new Insets(15, 0, 0, 0)); // Set padding for HBox

        BorderPane mainPane = new BorderPane(); // BorderPane for main pane
        mainPane.setPadding(new Insets(20)); // Set padding for main pane
        mainPane.setMaxHeight(getHeight()); // Set max height for main pane

        mainPane.setTop(infoText); // Add info text to main pane
        BorderPane.setAlignment(infoText, Pos.CENTER); // Align info text to center

        mainPane.setCenter(hallBox); // Add hall combo box to main pane
        BorderPane.setAlignment(hallBox, Pos.CENTER); // Align hall combo box to center

        mainPane.setBottom(buttonPane); // Add button pane to main pane
        BorderPane.setAlignment(buttonPane, Pos.CENTER); // Align button pane to center

        getChildren().add(mainPane); // Add main pane to stack pane
    }
}