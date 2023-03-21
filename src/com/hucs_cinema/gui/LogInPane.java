package com.hucs_cinema.gui;

import com.hucs_cinema.exception.BannedAttemptException;
import com.hucs_cinema.exception.EmptyKeyException;
import com.hucs_cinema.exception.UnmatchedKeyException;
import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.User;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LogInPane extends StackPane {
    public LogInPane() {

        /*
        If the user is logged out, there is no user logged in.
        This expression was written for this.
         */
        Main.currentUser = null; // set current user to null

        Text welcomeText = new Text("Welcome to the HUCS Cinema Reservation System!\n" +
            "Please enter your credentials below and click LOG IN.\n" + 
            "You can create a new account by clicking SIGN UP button.\n"); // text for welcome message and instructions
        welcomeText.setTextAlignment(TextAlignment.CENTER); // center the text

        TextField usernameField = new TextField(); // text field for username
        PasswordField passwordField = new PasswordField(); // password field for password

        Label infoLabel = new Label(""); // label for info text

        Button btLogIn = new Button("LOG IN"); // button for log in 
        /*
        For banning situation, it keeps the system milliseconds of
        pressing the LOGIN button when the user reaches the
        number of incorrect entries. After that, it receives
        and evaluates keypress milliseconds on subsequent banned login
        attempts.
         */

        btLogIn.setOnAction(e -> {
            String username = usernameField.getText(); // get username from text field
            String password = User.hashPassword(passwordField.getText()); // get password from password field

            if (System.currentTimeMillis() - Main.banStart.get() >= Main.BLOCK_TIME * 1000L) { // if the user is not banned
                Main.isBanned.set(false); // set banned to false
            }
            try { // try to log in
                Main.currentUser = User.logIn(username, password, Main.isBanned.get(), Main.USER_MAP); // log in
                Main.stage.setScene(new Scene(new UserPane(Main.currentUser))); // set scene to user pane
            }
            catch (BannedAttemptException ex) { // if the user is banned
                passwordField.clear(); // clear password field
                infoLabel.setText("ERROR: Please wait until end of the " + Main.BLOCK_TIME 
                        + " seconds to make a new operation!"); // set info text (wait until end of the block time)
                Main.playErrorSound(); // play error sound
            }
            catch (EmptyKeyException ex) { // if the user does not enter anything
                infoLabel.setText("ERROR: None of the fields can be empty!"); // set info text (none of the fields can be empty)
                Main.playErrorSound(); // play error sound
            }
            catch(UnmatchedKeyException ex) { // if the user enters wrong credentials
                Main.wrongAttempt.getAndUpdate(value -> (value + 1) % 6); // increase the wrong attempt number
                if (Main.wrongAttempt.get() == Main.MAX_ERROR_NUM) { // if the user reaches the number of incorrect entries
                    Main.isBanned.set(true); // set banned to true
                    Main.banStart.set(System.currentTimeMillis()); // set ban start time
                    passwordField.clear(); // clear password field
                    infoLabel.setText("ERROR: Please wait for " + Main.BLOCK_TIME
                            + " seconds to make a new operation!"); // set info text (wait for block time)
                    Main.playErrorSound(); // play error sound
                }
                else { // if the user does not reach the number of incorrect entries
                    passwordField.clear(); // clear password field
                    infoLabel.setText("ERROR: There is no such a credential!"); // set info text (there is no such a credential)
                    Main.playErrorSound(); // play error sound
                }
            }
        });

        Button btSignUp = new Button("SIGN UP"); // button for sign up
        btSignUp.setOnAction(e -> Main.stage.setScene(new Scene(new SignUpPane()))); // set scene to sign up pane

        GridPane logInPane = new GridPane(); // grid pane for log in pane
        logInPane.setAlignment(Pos.CENTER); // center the grid pane
        logInPane.setPadding(new Insets(0, 0, 10, 0)); // set padding
        logInPane.setHgap(10); // set horizontal gap
        logInPane.setVgap(10); // set vertical gap

        logInPane.add(new Label("Username:"), 0, 0); // add username label
        logInPane.add(usernameField, 1, 0); // add username field

        logInPane.add(new Label("Password:"), 0, 1); // add password label 
        logInPane.add(passwordField, 1, 1); // add password field

        logInPane.add(btSignUp, 0, 2); // add sign up button

        logInPane.add(btLogIn, 1, 2); // add log in button
        GridPane.setHalignment(btLogIn, HPos.RIGHT); // set log in button to right

        BorderPane mainPane = new BorderPane(); // border pane for main pane
        mainPane.setMaxHeight(getHeight()); // set max height
        mainPane.setPadding(new Insets(15, 25, 20, 30)); // set padding

        mainPane.setTop(welcomeText); // add welcome text
        BorderPane.setAlignment(welcomeText, Pos.CENTER); // center the welcome text

        mainPane.setCenter(logInPane); // add log in pane
        BorderPane.setAlignment(logInPane, Pos.CENTER); // center the log in pane

        mainPane.setBottom(infoLabel); // add info label
        BorderPane.setAlignment(infoLabel, Pos.CENTER); // center the info label

        getChildren().add(mainPane); // add main pane to the stack pane
    }
}
