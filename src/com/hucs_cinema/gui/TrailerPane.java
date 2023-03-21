package com.hucs_cinema.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.Hall;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class TrailerPane extends StackPane {

    /**
     * Constructor for film trailer screen
     */
    public TrailerPane() {
        /*
         * If the user switches from the hall screen to this screen,
         * user can choose a different hall. So the currently selected
         * film made null.
         */
        Main.chosenHall = null; // Make the selected hall null for the next selection

        Text filmInfoText = new Text(Main.chosenFilm.getNameWithDuration() + "\n"); // Get the name and duration of the
                                                                                    // film
        filmInfoText.setTextAlignment(TextAlignment.CENTER); // Align the text

        File video = new File(Main.chosenFilm.getPath()); // Get the path of the film
        String videoUrl = video.toURI().toString(); // Convert the path to URI

        Media media = new Media(videoUrl); // Create a media object
        MediaPlayer mediaPlayer = new MediaPlayer(media); // Create a media player object
        MediaView mediaView = new MediaView(mediaPlayer); // Create a media view object
        mediaView.setFitWidth(640); // Set the width of the media view
        mediaView.setPreserveRatio(true); // Preserve the ratio of the media view

        Button btPlay = new Button(">"); // Create a play button
        btPlay.setMinWidth(50); // Set the minimum width of the button
        btPlay.setOnAction(e -> { // Set the action of the button
            if (btPlay.getText().equals(">")) { // If the button text is ">", then play the video
                mediaPlayer.play(); // Play the video
                btPlay.setText("||"); // Change the button text to "||"
            } else { // If the button text is "||", then pause the video
                mediaPlayer.pause(); // Pause the video
                btPlay.setText(">"); // Change the button text to ">" again
            }
        });

        Duration fiveSecForward = Duration.seconds(5.0); // Create a duration object for 5 seconds
        Duration fiveSecBackward = Duration.seconds(-5.0); // Create a duration object for -5 seconds

        Button btJumpForward = new Button(">>"); // Create a jump forward button
        btJumpForward.setMinWidth(50); // Set the minimum width of the button
        btJumpForward.setOnAction(e -> mediaPlayer.seek(mediaPlayer.getCurrentTime().add(fiveSecForward))); // Set the
                                                                                                            // action of
                                                                                                            // the
                                                                                                            // button to
                                                                                                            // jump
                                                                                                            // forward 5
                                                                                                            // seconds

        Button btJumpBackward = new Button("<<"); // Create a jump backward button
        btJumpBackward.setMinWidth(50); // Set the minimum width of the button
        btJumpBackward.setOnAction(e -> mediaPlayer.seek(mediaPlayer.getCurrentTime().add(fiveSecBackward))); // Set the
                                                                                                              // action
                                                                                                              // of the
                                                                                                              // button
                                                                                                              // to jump
                                                                                                              // backward
                                                                                                              // 5
                                                                                                              // seconds

        Button btRewind = new Button("|<<"); // Create a rewind button
        btRewind.setMinWidth(50); // Set the minimum width of the button
        btRewind.setOnAction(e -> mediaPlayer.seek(Duration.ZERO)); // Set the action of the button to rewind to the
                                                                    // beginning

        Slider slVolume = new Slider(); // Create a volume slider
        slVolume.setOrientation(Orientation.VERTICAL); // Set the orientation of the slider to vertical

        slVolume.setPrefWidth(10); // Set the preferred width of the slider
        slVolume.setMaxWidth(Region.USE_PREF_SIZE); // Set the maximum width of the slider
        slVolume.setMinWidth(30); // Set the minimum width of the slider

        slVolume.setValue(50); // Set the initial value of the slider
        mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100)); // Bind the volume of the media player
                                                                                 // to the value of the slider

        VBox mediaController = new VBox(10); // Create a vertical box for the media controller
        mediaController.getChildren().addAll(btPlay, btJumpForward, btJumpBackward, btRewind, slVolume); // Add the
                                                                                                         // buttons and
                                                                                                         // the slider
                                                                                                         // to the
                                                                                                         // vertical box
        mediaController.setAlignment(Pos.CENTER); // Align the vertical box to the center

        List<Hall> hallList = Main.chosenFilm.getHallList(); // Get the list of halls that the film is playing

        ComboBox<Hall> hallBox = new ComboBox<>(); // Create a combo box for the halls

        hallBox.setItems(FXCollections.observableList(hallList)); // Set the items of the combo box to the list of halls
        hallBox.setEditable(false); // Set the combo box to non-editable
        hallBox.getSelectionModel().selectFirst(); // Select the first item of the combo box

        // Same methods that already mentioned for halls

        hallBox.setCellFactory(new Callback<ListView<Hall>, ListCell<Hall>>() {
            @Override
            public ListCell<Hall> call(ListView<Hall> param) {
                return new ListCell<Hall>() {
                    @Override
                    protected void updateItem(Hall item, boolean empty) {
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
        hallBox.setConverter(new StringConverter<Hall>() {
            @Override
            public String toString(Hall hall) {
                if (hall == null) {
                    return null;
                } else {
                    return hall.getName();
                }
            }

            @Override
            public Hall fromString(String string) {
                return null;
            }
        });

        Button btBack = new Button("<- BACK"); // Create a back button
        btBack.setOnAction(e -> { // Set the action of the button
            mediaPlayer.stop(); // Stop the video
            Main.stage.setScene(new Scene(new UserPane(Main.currentUser))); // Go back to the user pane
        });

        Button btOK = new Button("OK"); // Create an OK button
        btOK.setOnAction(e -> { // Set the action of the button
            mediaPlayer.stop(); // Stop the video
            Main.chosenHall = hallBox.getValue(); // Set the chosen hall to the selected hall in the combo box
            try {
                if (Main.chosenHall != null) { // If the chosen hall is not null, then go to the hall pane
                    if (Main.currentUser.isAdmin()) { // If the current user is an admin
                        Main.stage.setScene(new Scene(new AdminHallPane())); // Go to the admin hall pane
                    } else { // If the current user is not an admin
                        Main.stage.setScene(new Scene(new HallPane())); // Go to the hall pane
                    }
                }
            } catch (FileNotFoundException ex) { // Catch the exception if the file is not found
                ex.printStackTrace();
            }
        });

        HBox manageButtonPane = new HBox(10); // Create a horizontal box for the buttons
        manageButtonPane.setAlignment(Pos.CENTER); // Align the horizontal box to the center

        if (Main.currentUser.isAdmin()) { // If the current user is an admin
            Button btAddHall = new Button("Add Hall"); // Create an add hall button
            btAddHall.setOnAction(e -> { // Set the action of the button
                mediaPlayer.stop(); // Stop the video
                Main.stage.setScene(new Scene(new AddHallPane())); // Go to the add hall pane
            });
            Button btRemoveHall = new Button("Remove Hall"); // Create a remove hall button
            btRemoveHall.setOnAction(e -> { // Set the action of the button
                mediaPlayer.stop(); // Stop the video
                Main.stage.setScene(new Scene(new RemoveHallPane())); // Go to the remove hall pane
            });
            manageButtonPane.getChildren().addAll(btBack, btAddHall, btRemoveHall, hallBox, btOK); // Add the buttons to
                                                                                                   // the horizontal box
        } else {
            manageButtonPane.getChildren().addAll(btBack, hallBox, btOK); // Add the buttons to the horizontal box
        }

        VBox buttonPane = new VBox(20); // Create a vertical box for the buttons
        buttonPane.setPadding(new Insets(15, 0, 15, 0)); // Set the padding of the vertical box
        buttonPane.setAlignment(Pos.CENTER); // Align the vertical box to the center

        /*
         * EXTRA FEATURE:
         * A like dislike system was made here. User can like or dislike the movie.
         * The number of likes and dislikes for the selected movie is written on the
         * trailer screen. If the user likes or dislikes the movie, when the system
         * is turned off and on again, the button still remains as it is, so it
         * maintains its status. Also, two buttons cannot be pressed at the same time.
         * One button blocks the other one.
         */

        if (Main.isExtraLoaded) { // If the extra feature is loaded
            Text likeText = new Text(String.valueOf(Main.chosenFilm.getLikeNum())); // Create a text for the number of
                                                                                    // likes
            likeText.setTextAlignment(TextAlignment.CENTER); // Align the text to the center
            Text dislikeText = new Text(String.valueOf(Main.chosenFilm.getDislikeNum())); // Create a text for the
                                                                                          // number of dislikes
            dislikeText.setTextAlignment(TextAlignment.CENTER); // Align the text to the center

            File unlikedFile = new File("./assets/icons/unliked.png"); // Create a file for the unliked image
            Image unlikedImage = new Image(unlikedFile.toURI().toString()); // Create an image for the unliked image
            ImageView unliked = new ImageView(unlikedImage); // Create an image view for the unliked image
            unliked.setFitWidth(20); // Set the width of the image view
            unliked.setPreserveRatio(true); // Set the ratio of the image view

            File likedFile = new File("./assets/icons/liked.png"); // Create a file for the liked image
            Image likedImage = new Image(likedFile.toURI().toString()); // Create an image for the liked image
            ImageView liked = new ImageView(likedImage); // Create an image view for the liked image
            liked.setFitWidth(20); // Set the width of the image view
            liked.setPreserveRatio(true); // Set the ratio of the image view

            File undislikedFile = new File("./assets/icons/undisliked.png"); // Create a file for the undisliked image
            Image undislikedImage = new Image(undislikedFile.toURI().toString()); // Create an image for the undisliked
                                                                                  // image
            ImageView undisliked = new ImageView(undislikedImage); // Create an image view for the undisliked image
            undisliked.setFitWidth(20); // Set the width of the image view
            undisliked.setPreserveRatio(true); // Set the ratio of the image view

            File dislikedFile = new File("./assets/icons/disliked.png"); // Create a file for the disliked image
            Image dislikedImage = new Image(dislikedFile.toURI().toString()); // Create an image for the disliked image
            ImageView disliked = new ImageView(dislikedImage); // Create an image view for the disliked image
            disliked.setFitWidth(20); // Set the width of the image view
            disliked.setPreserveRatio(true); // Set the ratio of the image view

            Button likeButton = new Button(); // Create a like button
            Button dislikeButton = new Button(); // Create a dislike button

            likeButton.setOnAction(e -> { // Set the action of the like button
                if (!Main.currentUser.getLikedFilms().contains(Main.chosenFilm)) { // If the current user has not liked
                                                                                   // the movie
                    Main.currentUser.like(Main.chosenFilm); // Like the movie
                    likeButton.setGraphic(liked); // Set the image of the button to the liked image
                    if (Main.currentUser.getDislikedFilms().contains(Main.chosenFilm)) { // If the current user has
                                                                                         // disliked the movie
                        dislikeButton.fire(); // Press the dislike button
                    }
                } else { // If the current user has liked the movie
                    Main.currentUser.unlike(Main.chosenFilm); // Unlike the movie
                    likeButton.setGraphic(unliked); // Set the image of the button to the unliked image
                }
                likeText.setText(String.valueOf(Main.chosenFilm.getLikeNum())); // Set the text of the like text to the
                                                                                // number of likes
            });

            dislikeButton.setOnAction(e -> { // Set the action of the dislike button
                if (!Main.currentUser.getDislikedFilms().contains(Main.chosenFilm)) { // If the current user has not
                                                                                      // disliked the movie
                    Main.currentUser.dislike(Main.chosenFilm); // Dislike the movie
                    dislikeButton.setGraphic(disliked); // Set the image of the button to the disliked image
                    if (Main.currentUser.getLikedFilms().contains(Main.chosenFilm)) { // If the current user has liked
                                                                                      // the movie
                        likeButton.fire(); // Press the like button
                    }
                } else { // If the current user has disliked the movie
                    Main.currentUser.undislike(Main.chosenFilm); // Undislike the movie
                    dislikeButton.setGraphic(undisliked); // Set the image of the button to the undisliked image
                }
                dislikeText.setText(String.valueOf(Main.chosenFilm.getDislikeNum())); // Set the text of the dislike
                                                                                      // text to the number of dislikes
            });

            if (Main.currentUser.getLikedFilms().contains(Main.chosenFilm)) { // If the current user has liked the movie
                likeButton.setGraphic(liked); // Set the image of the button to the liked image
                dislikeButton.setGraphic(undisliked); // Set the image of the button to the undisliked image
            } else if (Main.currentUser.getDislikedFilms().contains(Main.chosenFilm)) { // If the current user has
                                                                                        // disliked the movie
                likeButton.setGraphic(unliked); // Set the image of the button to the unliked image
                dislikeButton.setGraphic(disliked); // Set the image of the button to the disliked image
            } else {
                likeButton.setGraphic(unliked); // Set the image of the button to the unliked image
                dislikeButton.setGraphic(undisliked); // Set the image of the button to the undisliked image
            }

            HBox likeDislikePane = new HBox(10); // Create a pane for the like and dislike buttons
            likeDislikePane.setMaxWidth(690); // Set the max width of the pane

            likeDislikePane.getChildren().addAll(likeButton, likeText, dislikeButton, dislikeText); // Add the buttons
                                                                                                    // and texts to the
                                                                                                    // pane
            buttonPane.getChildren().add(likeDislikePane); // Add the pane to the button pane
        }

        buttonPane.getChildren().add(manageButtonPane); // Add the manage button pane to the button pane

        BorderPane mainPane = new BorderPane(); // Create a border pane for the main pane
        mainPane.setMaxSize(800, 500); // Set the max size of the main pane
        mainPane.setPadding(new Insets(20)); // Set the padding of the main pane

        mainPane.setTop(filmInfoText); // Set the top of the main pane to the film info text
        BorderPane.setAlignment(filmInfoText, Pos.CENTER); // Align the film info text to the center

        mainPane.setCenter(mediaView); // Set the center of the main pane to the media view

        mainPane.setRight(mediaController); // Set the right of the main pane to the media controller
        BorderPane.setAlignment(mediaController, Pos.CENTER); // Align the media controller to the center

        mainPane.setBottom(buttonPane); // Set the bottom of the main pane to the button pane
        BorderPane.setAlignment(buttonPane, Pos.CENTER); // Align the button pane to the center

        getChildren().add(mainPane); // Add the main pane to the children of the scene
        setPrefSize(850, 550); // Set the preferred size of the scene
    }
}