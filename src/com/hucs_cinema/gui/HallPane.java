package com.hucs_cinema.gui;

import java.io.File;
import java.io.FileNotFoundException;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Seat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class HallPane extends StackPane {

    /**
     * Constructor for hall screen
     * 
     * @throws FileNotFoundException If the seat images are not found
     */
    public HallPane() throws FileNotFoundException {
        // info text for film name and duration
        Text hallInfoText = new Text(
                Main.chosenHall.getFilmOfHall().getNameWithDuration() + " Hall: " + Main.chosenHall.getName() + "\n");
        hallInfoText.setTextAlignment(TextAlignment.CENTER); // center the text

        File emptySeatFile = new File("./assets/icons/empty_seat.png"); // file for empty seat image
        Image emptySeatImage = new Image(emptySeatFile.toURI().toString()); // image for empty seat

        File reservedSeatFile = new File("./assets/icons/reserved_seat.png"); // file for reserved seat image
        Image reservedSeatImage = new Image(reservedSeatFile.toURI().toString()); // image for reserved seat

        Label infoLabel = new Label(""); // label for info text
        infoLabel.setTextAlignment(TextAlignment.CENTER); // center the text

        GridPane paneForSeats = new GridPane(); // grid pane for seats
        paneForSeats.setAlignment(Pos.CENTER); // center the grid pane
        paneForSeats.setVgap(10); // set vertical gap
        paneForSeats.setHgap(10); // set horizontal gap

        /*
         * In this loop, all seat buttons initialized and added to the pane.
         * If the seat is reserved, the user cannot take it. So the button is disabled.
         * It is enabled if it can be reserved. When the button is clicked, it reserves
         * the selected seat. If it is a seat reserved by the logged user, it will be
         * refunded.
         */
        for (Seat seat : Main.chosenHall.getSeatList()) { // for each seat in the hall

            ImageView emptySeat = new ImageView(emptySeatImage); // image view for empty seat
            emptySeat.setFitHeight(50); // set height
            emptySeat.setFitWidth(50); // set width

            ImageView reservedSeat = new ImageView(reservedSeatImage); // image view for reserved seat
            reservedSeat.setFitHeight(50); // set height
            reservedSeat.setFitWidth(50); // set width

            Button seatButton = new Button(); // button for seat

            if (seat.getOwner() == null || seat.getOwner() == Main.currentUser) { // if the seat is not reserved or
                                                                                  // reserved by the logged user
                seatButton.setOnMouseClicked(e -> { // when the button is clicked
                    if (seat.getOwner() == null) { // if the seat is not reserved
                        int seatPrice = 0; // price of the seat
                        // if the logged user is a club member, the price will be discounted
                        if (Main.currentUser.isClubMember()) {
                            seatPrice = (int) (Main.chosenHall.getPrice() * ((100 - Main.DISCOUNT_PERCENTAGE) * 0.01));
                        } else {
                            seatPrice = Main.chosenHall.getPrice();
                        }
                        seat.setOwner(Main.currentUser); // set the owner of the seat to the logged user
                        seat.setPrice(seatPrice); // set the price of the seat
                        seatButton.setGraphic(reservedSeat); // set the image of the seat to reserved seat
                        infoLabel.setText("Seat at " + (GridPane.getRowIndex(seatButton) + 1)
                                + "-" + (GridPane.getColumnIndex(seatButton) + 1) +
                                " bought for " + seatPrice + " TL successfully!"); // set the info text to seat bought
                                                                                   // successfully
                    } else { // if the seat is reserved by the logged user
                        seat.setOwner(null); // set the owner of the seat to null
                        seat.setPrice(0); // set the price of the seat to 0
                        seatButton.setGraphic(emptySeat); // set the image of the seat to empty seat
                        infoLabel.setText("Seat at " + (GridPane.getRowIndex(seatButton) + 1)
                                + "-" + (GridPane.getColumnIndex(seatButton) + 1) +
                                " refunded successfully!"); // set the info text to seat refunded successfully
                    }
                });
                if (seat.getOwner() == null) { // if the seat is not reserved
                    seatButton.setGraphic(emptySeat); // set the image of the seat to empty seat
                } else { // if the seat is reserved
                    seatButton.setGraphic(reservedSeat); // set the image of the seat to reserved seat
                }
                paneForSeats.add(seatButton, seat.getColumn(), seat.getRow()); // add the seat button to the grid pane
            } else {
                seatButton.setGraphic(reservedSeat); // set the image of the seat to reserved seat
                seatButton.setDisable(true); // disable the button
                paneForSeats.add(seatButton, seat.getColumn(), seat.getRow()); // add the seat button to the grid pane
            }
        }

        Button btBack = new Button("<- BACK"); // button for going back to trailer screen

        btBack.setOnAction(e -> Main.stage.setScene(new Scene(new TrailerPane()))); // when the button is clicked, go
                                                                                    // back to trailer screen

        HBox labelPane = new HBox(); // pane for info text
        labelPane.setAlignment(Pos.CENTER); // center the pane
        labelPane.getChildren().add(infoLabel); // add the info text to the pane

        HBox buttonPane = new HBox(); // pane for back button
        buttonPane.getChildren().add(btBack); // add the back button to the pane

        VBox paneForInfo = new VBox(15); // pane for info text and back button
        paneForInfo.setAlignment(Pos.CENTER); // center the pane
        paneForInfo.setPadding(new Insets(15, 0, 15, 0)); // set padding
        paneForInfo.getChildren().addAll(labelPane, buttonPane); // add the info text and back button to the pane

        BorderPane mainPane = new BorderPane(); // main pane for the hall screen
        mainPane.setMaxHeight(getHeight()); // set max height
        mainPane.setPadding(new Insets(15, 50, 15, 50)); // set padding

        mainPane.setTop(hallInfoText); // add the info text to the main pane
        BorderPane.setAlignment(hallInfoText, Pos.CENTER); // center the info text

        mainPane.setCenter(paneForSeats); // add the grid pane for seats to the main pane
        BorderPane.setAlignment(paneForSeats, Pos.CENTER); // center the grid pane for seats

        mainPane.setBottom(paneForInfo); // add the pane for info text and back button to the main pane
        BorderPane.setAlignment(paneForInfo, Pos.CENTER); // center the pane for info text and back button

        getChildren().add(mainPane); // add the main pane to the hall screen
    }
}