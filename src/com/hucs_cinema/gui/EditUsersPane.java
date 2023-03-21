package com.hucs_cinema.gui;

import java.util.ArrayList;
import java.util.List;

import com.hucs_cinema.main.Main;
import com.hucs_cinema.object.User;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class EditUsersPane extends StackPane {

    /**
     * Constructor for editing users screen
     */
    public EditUsersPane() {
        List<User> userList = new ArrayList<>(Main.USER_MAP.values()); // get list of users
        userList.remove(Main.currentUser); // remove current user from list

        TableView<User> userPanel = new TableView<>(); // table view for users
        userPanel.setItems(FXCollections.observableList(userList)); // set items for table view
        userPanel.setPlaceholder(new Label("No user available in the database!")); // set placeholder for table view (if
                                                                                   // no user available)

        TableView.TableViewSelectionModel<User> selectionModel = userPanel.getSelectionModel(); // get selection model
                                                                                                // for table view (for
                                                                                                // selecting user)
        selectionModel.selectFirst(); // select first user as default

        TableColumn<User, String> column1 = new TableColumn<>("Username"); // column for username
        TableColumn<User, String> column2 = new TableColumn<>("Club Member"); // column for club membership
        TableColumn<User, String> column3 = new TableColumn<>("Admin"); // column for admin

        /*
         * The cell factory methods is for adjusting what will appear in each column
         */

        userPanel.getColumns().add(column1); // add columns to table view
        column1.setCellValueFactory(cellData -> { // set cell factory for username column
            String username = cellData.getValue().getUsername(); // get username of user
            return new ReadOnlyStringWrapper(username); // return username as read only string wrapper
        });

        userPanel.getColumns().add(column2); // add columns to table view
        column2.setCellValueFactory(cellData -> { // set cell factory for club membership column
            boolean isClubMember = cellData.getValue().isClubMember(); // get club membership of user
            String isClubMemberStr; // string for club membership
            isClubMemberStr = ((isClubMember) ? "true" : "false"); // set string for club membership
            return new ReadOnlyStringWrapper(isClubMemberStr); // return club membership as read only string wrapper
        });

        userPanel.getColumns().add(column3); // add columns to table view
        column3.setCellValueFactory(cellData -> { // set cell factory for admin column
            boolean isAdmin = cellData.getValue().isAdmin(); // get admin status of user
            String isAdminStr; // string for admin status
            isAdminStr = ((isAdmin) ? "true" : "false"); // set string for admin status
            return new ReadOnlyStringWrapper(isAdminStr); // return admin status as read only string wrapper
        });

        Button btBack = new Button("<- BACK"); // back button to go back to user pane
        btBack.setOnAction(e -> Main.stage.setScene(new Scene(new UserPane(Main.currentUser)))); // set action for back
                                                                                                 // button (go back to
                                                                                                 // user pane)

        Button btClubMember = new Button("Promote/Denote Club Member"); // button for promoting/denoting club membership
        btClubMember.setOnAction(e -> { // set action for club membership button (promote/denote club membership)
            User selectedUser = selectionModel.getSelectedItems().get(0); // get selected user
            selectedUser.setClubMember(!selectedUser.isClubMember()); // set club membership of selected user to
                                                                      // opposite of current club membership
            userPanel.refresh(); // refresh table view to show changes
        });

        Button btAdmin = new Button("Promote/Denote Admin"); // button for promoting/denoting admin
        btAdmin.setOnAction(e -> { // set action for admin button (promote/denote admin)
            User selectedUser = selectionModel.getSelectedItems().get(0); // get selected user
            selectedUser.setAdmin(!selectedUser.isAdmin()); // set admin status of selected user to opposite of current
                                                            // admin status
            userPanel.refresh(); // refresh table view to show changes
        });

        HBox paneForButtons = new HBox(10); // pane for buttons
        paneForButtons.getChildren().addAll(btBack, btClubMember, btAdmin); // add buttons to pane
        btBack.setAlignment(Pos.CENTER_LEFT); // set alignment for buttons (left, center, right)
        btClubMember.setAlignment(Pos.CENTER); // set alignment for buttons (left, center, right)
        btAdmin.setAlignment(Pos.CENTER_RIGHT); // set alignment for buttons (left, center, right)

        GridPane mainPane = new GridPane(); // main pane for scene
        mainPane.setVgap(10); // set vertical gap for main pane
        mainPane.setAlignment(Pos.CENTER); // set alignment for main pane (top, center, bottom)
        mainPane.setPadding(new Insets(10)); // set padding for main pane

        mainPane.add(userPanel, 0, 0); // add table view to main pane
        mainPane.add(paneForButtons, 0, 1); // add buttons to main pane

        getChildren().add(mainPane); // add main pane to scene
    }
}