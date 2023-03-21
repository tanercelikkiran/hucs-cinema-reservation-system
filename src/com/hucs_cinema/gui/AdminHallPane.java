package com.hucs_cinema.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Seat;
import com.hucs_cinema.object.User;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class AdminHallPane extends StackPane {

    /**
     * Constructor for hall pane but for admins
     * 
     * @throws FileNotFoundException If the extra images are not found
     */
    public AdminHallPane() throws FileNotFoundException {

        Text hallInfoText = new Text(
                Main.chosenHall.getFilmOfHall().getNameWithDuration() + " Hall: " + Main.chosenHall.getName() + "\n");
        hallInfoText.setTextAlignment(TextAlignment.CENTER);

        File emptySeatFile = new File("./assets/icons/empty_seat.png");
        Image emptySeatImage = new Image(emptySeatFile.toURI().toString());

        File reservedSeatFile = new File("./assets/icons/reserved_seat.png");
        Image reservedSeatImage = new Image(reservedSeatFile.toURI().toString());

        Label clickInfoLabel = new Label("");
        clickInfoLabel.setTextAlignment(TextAlignment.CENTER);

        Label mouseInfoLabel = new Label("");
        mouseInfoLabel.setTextAlignment(TextAlignment.CENTER);

        List<User> userList = new ArrayList<>(Main.USER_MAP.values());

        ComboBox<User> userBox = new ComboBox<>();

        userBox.setItems(FXCollections.observableList(userList));
        userBox.setEditable(false);
        userBox.getSelectionModel().selectFirst();

        userBox.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        getListView().setMinWidth(ListView.USE_PREF_SIZE);
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getUsername());
                        }
                    }
                };
            }
        });
        userBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user == null) {
                    return null;
                } else {
                    return user.getUsername();
                }
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });

        GridPane paneForSeats = new GridPane();
        paneForSeats.setAlignment(Pos.CENTER);
        paneForSeats.setVgap(10);
        paneForSeats.setHgap(10);

        /*
         * There is a similar structure here compared to the other hall screen.
         * But no buttons are disabled here. Each seat can be reserved for the
         * selected person and existing reservations may be cancelled. When the
         * cursor is on the button, it is written whether the seat was bought
         * or not, and if so, how much was bought by whom. As soon as the cursor
         * goes out, the text disappears. In addition, after the new reservation
         * process, another informative text will appear below.
         */
        for (Seat seat : Main.chosenHall.getSeatList()) {

            ImageView emptySeat = new ImageView(emptySeatImage);
            emptySeat.setFitHeight(50);
            emptySeat.setFitWidth(50);

            ImageView reservedSeat = new ImageView(reservedSeatImage);
            reservedSeat.setFitHeight(50);
            reservedSeat.setFitWidth(50);

            Button seatButton = new Button();

            seatButton.setOnMouseEntered(e -> {
                if (seat.getOwner() == null) {
                    mouseInfoLabel.setText("Not bought yet!");
                } else {
                    mouseInfoLabel.setText("Bought by " + seat.getOwner().getUsername() +
                            " for " + seat.getPrice() + " TL!");
                }
            });

            seatButton.setOnMouseExited(e -> mouseInfoLabel.setText(""));

            seatButton.setOnMouseClicked(e -> {
                User chosenUser = userBox.getValue();
                if (chosenUser != null) {
                    if (seat.getOwner() == null) {
                        seat.setOwner(chosenUser);
                        seatButton.setGraphic(reservedSeat);
                        int seatPrice;
                        if (chosenUser.isClubMember()) {
                            seatPrice = (int) (Main.chosenHall.getPrice() * ((100 - Main.DISCOUNT_PERCENTAGE) * 0.01));
                        } else {
                            seatPrice = Main.chosenHall.getPrice();
                        }
                        seat.setPrice(seatPrice);
                        clickInfoLabel.setText("Seat at " + (GridPane.getRowIndex(seatButton) + 1)
                                + "-" + (GridPane.getColumnIndex(seatButton) + 1) +
                                " bought for " + chosenUser.getUsername() + " for " + seatPrice + " TL successfully!");
                    } else {
                        seatButton.setGraphic(emptySeat);
                        seat.setOwner(null);
                        seat.setPrice(0);
                        clickInfoLabel.setText("Seat at " + (GridPane.getRowIndex(seatButton) + 1)
                                + "-" + (GridPane.getColumnIndex(seatButton) + 1) +
                                " refunded successfully!");
                    }
                }
            });

            if (seat.getOwner() == null) {
                seatButton.setGraphic(emptySeat);
                paneForSeats.add(seatButton, seat.getColumn(), seat.getRow());
            } else {
                seatButton.setGraphic(reservedSeat);
                paneForSeats.add(seatButton, seat.getColumn(), seat.getRow());
            }
        }

        Button btBack = new Button("<- BACK");
        btBack.setOnAction(e -> Main.stage.setScene(new Scene(new TrailerPane())));

        HBox paneForUserBox = new HBox();
        paneForUserBox.setPadding(new Insets(10, 0, 0, 0));
        paneForUserBox.getChildren().add(userBox);
        paneForUserBox.setAlignment(Pos.CENTER);

        HBox mouseInfoPane = new HBox();
        mouseInfoPane.setAlignment(Pos.CENTER);
        mouseInfoPane.getChildren().add(mouseInfoLabel);

        HBox clickInfoPane = new HBox();
        clickInfoPane.setAlignment(Pos.CENTER);
        clickInfoPane.getChildren().add(clickInfoLabel);

        HBox paneForBtBack = new HBox();
        paneForBtBack.getChildren().add(btBack);

        VBox infoPane = new VBox(15);
        infoPane.getChildren().addAll(paneForUserBox, mouseInfoPane, clickInfoPane, paneForBtBack);

        BorderPane mainPane = new BorderPane();
        mainPane.setMaxHeight(getHeight());
        mainPane.setPadding(new Insets(15, 50, 15, 50));

        mainPane.setTop(hallInfoText);
        BorderPane.setAlignment(hallInfoText, Pos.CENTER);

        mainPane.setCenter(paneForSeats);
        BorderPane.setAlignment(paneForSeats, Pos.CENTER);

        mainPane.setBottom(infoPane);
        BorderPane.setAlignment(infoPane, Pos.CENTER);

        getChildren().add(mainPane);
    }
}