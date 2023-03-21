package com.hucs_cinema.gui;

import javafx.scene.layout.StackPane;

import com.hucs_cinema.exception.EmptyKeyException;
import com.hucs_cinema.exception.ExistingKeyException;
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

public class SignUpPane extends StackPane {

    /**
     * Constructor for sign up screen
     */
    public SignUpPane() {
        Text welcomeText = new Text("Welcome to the HUCS Cinema Reservation System!\n" +
                "Fill the form below to create a new account.\n" +
                "You can go to Log In page by clicking LOG IN button.\n"); // text for welcome message and instructions
        welcomeText.setTextAlignment(TextAlignment.CENTER); // center the text

        TextField usernameField = new TextField(); // text field for username
        PasswordField firstPasswordField = new PasswordField(); // password field for password
        PasswordField secondPasswordField = new PasswordField(); // password field for password confirmation

        Label infoLabel = new Label(""); // label for info text

        Button btSignUp = new Button("SIGN UP"); // button for sign up
        btSignUp.setOnAction(e -> { // when the button is clicked

            String username = usernameField.getText(); // get username from text field
            String password1 = User.hashPassword(firstPasswordField.getText()); // get password from password field
            String password2 = User.hashPassword(secondPasswordField.getText()); // get password from password field

            try { // try to sign up
                User.signUp(username, password1, password2, Main.USER_MAP); // sign up
                infoLabel.setText("SUCCESS: You have successfully registered with your credentials!"); // set info text
                                                                                                       // (success)
                usernameField.clear(); // clear username field
                firstPasswordField.clear(); // clear password field
                secondPasswordField.clear(); // clear password field
            } catch (EmptyKeyException ex) { // if any of the fields are empty
                infoLabel.setText("ERROR: None of the fields cannot be empty!"); // set info text (none of the fields
                                                                                 // can be empty)
                Main.playErrorSound(); // play error sound
            } catch (ExistingKeyException ex) { // if the username already exists
                infoLabel.setText("ERROR: This username already exists!"); // set info text (username already exists)
                usernameField.clear(); // clear username field
                Main.playErrorSound(); // play error sound
            } catch (UnmatchedKeyException ex) { // if the passwords are not matching
                infoLabel.setText("ERROR: Passwords are not matching!"); // set info text (passwords are not matching)
                firstPasswordField.clear(); // clear password field
                secondPasswordField.clear(); // clear password field
                Main.playErrorSound(); // play error sound
            }
        });

        Button btLogIn = new Button("LOG IN"); // button for log in
        btLogIn.setOnAction(e -> Main.stage.setScene(new Scene(new LogInPane()))); // when the button is clicked, go to
                                                                                   // log in screen

        GridPane signUpPane = new GridPane(); // grid pane for sign up form
        signUpPane.setAlignment(Pos.CENTER); // center the grid pane
        signUpPane.setPadding(new Insets(0, 0, 10, 0)); // set padding
        signUpPane.setHgap(10); // set horizontal gap
        signUpPane.setVgap(10); // set vertical gap

        signUpPane.add(new Label("Username:"), 0, 0); // add username label
        signUpPane.add(usernameField, 1, 0); // add username field

        signUpPane.add(new Label("Password:"), 0, 1); // add password label
        signUpPane.add(firstPasswordField, 1, 1); // add password field

        signUpPane.add(new Label("Password:"), 0, 2); // add password confirmation label
        signUpPane.add(secondPasswordField, 1, 2); // add password confirmation field

        signUpPane.add(btLogIn, 0, 3); // add log in button

        signUpPane.add(btSignUp, 1, 3); // add sign up button
        GridPane.setHalignment(btSignUp, HPos.RIGHT); // align sign up button to the right

        BorderPane mainPane = new BorderPane(); // border pane for main pane
        mainPane.setMaxHeight(getHeight()); // set max height
        mainPane.setPadding(new Insets(15, 30, 15, 30)); // set padding

        mainPane.setTop(welcomeText); // add welcome text
        BorderPane.setAlignment(welcomeText, Pos.CENTER); // center the welcome text

        mainPane.setCenter(signUpPane); // add sign up pane
        BorderPane.setAlignment(signUpPane, Pos.CENTER); // center the sign up pane

        mainPane.setBottom(infoLabel); // add info label
        BorderPane.setAlignment(infoLabel, Pos.CENTER); // center the info label

        getChildren().add(mainPane); // add main pane to the stack pane
    }
}