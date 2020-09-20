/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serramai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import model.GameRecord;
import model.Validator;

/**
 * FXML Controller class that represents a GameRecordApplicationController.
 * Contains all the methods and logic of a GameRecord storage application.
 *
 * @author Maiziel Serrao
 */
public class FXMLGameRecordApplicationController implements Initializable {

    @FXML
    private ListView<String> lstFiles;

    @FXML
    private ListView<Integer> lstPlayers;

    @FXML
    private Label lblTitle;

    @FXML
    private Button btnLoad, btnSave, btnCheck, btnQuit;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ComboBox<String> ddlGames;

    ArrayList<GameRecord> games = new ArrayList<GameRecord>();
    //represents the valid GameRecords stored in the csv file selected
    ArrayList<GameRecord> displayableGames = new ArrayList<GameRecord>();
    //represents the GameRecords that are valid in games ArrayList
    int thisIndex = -1;
    //represents the selected game index in the displayableGames ArrayList

    Validator isValid = new Validator();

    /**
     * Initializes the controller class and contains the listeners on properties
     * within the program.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> obsFileList = getFiles();

        lstFiles.getSelectionModel().selectedItemProperty().
                addListener(new InvalidationListener() {

                    @Override
                    public void invalidated(Observable o) {
                        if (lstFiles.getSelectionModel().getSelectedItem()
                                != null) {
                            btnLoad.setVisible(true);
                        } else {
                            btnLoad.setVisible(false);
                        }
                    }
                });

        lstPlayers.getSelectionModel().selectedItemProperty().
                addListener(new InvalidationListener() {

                    @Override
                    public void invalidated(Observable o) {
                        if (lstPlayers.getSelectionModel().getSelectedItem()
                                != null) {
                            btnCheck.setVisible(true);
                        } else {
                            btnCheck.setVisible(false);
                            resetAfterCheckGames();
                        }
                    }
                });

        ddlGames.getSelectionModel().selectedItemProperty().
                addListener(new InvalidationListener() {

                    @Override
                    public void invalidated(Observable o) {
                        String itemName
                                = ddlGames.getSelectionModel().
                                        getSelectedItem();
                        if (itemName != null) {
                            int thisIndex = matchGameToString(displayableGames,
                                    itemName);
                            String displayGameName = displayableGames.
                                    get(thisIndex).toString();
                            txtDescription.setText(displayGameName);
                            btnSave.setVisible(true);
                        } else {
                            txtDescription.setText("");
                            btnSave.setVisible(false);
                        }
                    }
                });
    }

    /**
     * A method to display all available .csv files the game_collections
     * directory.
     *
     * @return ObservableList of String containing the .csv file names
     */
    public ObservableList<String> getFiles() {
        //String array containing the list of all files in game_collections
        String[] repo = new File("src/game_collections").list();

        //Counting and storing all files with .csv extension in an ArrayList
        int counter = 0;
        ArrayList<String> allFilesWithCsv = new ArrayList<String>();
        for (int i = 0; i < repo.length; i++) {
            if (repo[i].matches(".*\\.csv$")) {
                allFilesWithCsv.add(repo[i]);
                counter++;
            }
        }

        //Adding all Strings in ArrayList into String []
        String[] allCSVFiles = new String[counter];
        for (int i = 0; i < counter; i++) {
            String file = allFilesWithCsv.get(i);
            allCSVFiles[i] = file;
        }

        //Create an observable list containing all the names of the .csv files 
        ObservableList<String> obsFileList
                = FXCollections.observableArrayList(allCSVFiles);
        lstFiles.setItems(obsFileList);

        return obsFileList;
    }

    /**
     * A method to get GameRecords from a selected file and adds those
     * GameRecords to an ArrayList of GameRecord called games.
     *
     * @param selectedItem represents the path to the selected file
     * @return an ArrayList filled with GameRecords contained within the file
     * @throws FileNotFoundException if the file does not exist
     */
    public ArrayList<GameRecord> getGameRecords(String selectedItem)
            throws FileNotFoundException {
        String fileName = new String("src/game_collections/");
        fileName += selectedItem;
        File file = new File(fileName);
        Scanner scan = new Scanner(file);

        games.clear();

        int counter = 0;
        while (scan.hasNextLine()) {
            String record = scan.nextLine();
            if (record.contains(",")) {
                String[] fields = record.split(",");

                if (fields.length == 5) {
                    GameRecord thisGR = parseGameRecord(fields);
                    if (!thisGR.equals(new GameRecord())) {
                        games.add(thisGR);
                    }
                    counter++;
                }
            }
        }

        boolean displayPlayers = false;

        if (counter == 0) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "No Game Records",
                    "No game records are in this file.");
            alert.showAndWait();
        } else {
            displayPlayers = true;
        }

        if (displayPlayers == true) {
            displayPlayers();
        } else {
            lstPlayers.getItems().clear();
        }

        return games;
    }

    /**
     * A method to match the name of an item to a GameRecord in an ArrayList.
     *
     * @param displayableGames ArrayList containing GameRecords
     * @param itemName String to be matched with the gameName of a GameRecord
     * object
     * @return int representing the index within the ArrayList of GameRecord
     * matching the String; returns -1 if there is no match
     */
    public int matchGameToString(ArrayList<GameRecord> displayableGames,
            String itemName) {
        if (itemName != null) {
            String displayGameName = "This game";
            for (int i = 0; i < displayableGames.size(); i++) {
                if (itemName.equals(displayableGames.get(i).getGameName())) {
                    displayGameName = displayableGames.get(i).toString();
                    thisIndex = i;
                }
            }
        }
        return thisIndex;
    }

    /**
     * A method that sets the number of players in the ListView from 1-10.
     */
    public void displayPlayers() {
        Integer[] numOfPlayers = new Integer[10];
        for (int i = 0; i < 10; i++) {
            numOfPlayers[i] = i + 1;
        }

        //Create an observable list containing all the names of the .csv files 
        ObservableList<Integer> obsNumPlayersList
                = FXCollections.observableArrayList(numOfPlayers);
        lstPlayers.setItems(obsNumPlayersList);
        lstPlayers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * A method that takes in an array of Strings and parses them into a
     * GameRecord if the fields are valid. If not, the fields remain as the
     * GameRecord default values.
     *
     * @param fields String array representing the different data members of a
     * GameRecord object
     * @return GameRecord object containing the fields from the String array
     */
    public GameRecord parseGameRecord(String[] fields) {
        GameRecord newGameRecord = new GameRecord();

        boolean nameOk = false;
        boolean minPlayersOk = false;
        boolean maxPlayersOk = false;
        boolean priceOk = false;
        boolean descriptionOk = false;

        try {
            String name = fields[0];
            int minPlayers = Integer.parseInt(fields[1]);
            int maxPlayers = Integer.parseInt(fields[2]);
            double price = Double.parseDouble(fields[3]);
            String description = fields[4];

            if (isValid.isValidString(name)) {
                newGameRecord.setGameName(name);
                nameOk = true;
            }
            if (isValid.isInt(minPlayers)) {
                newGameRecord.setMinPlayers(minPlayers);
                minPlayersOk = true;
            }
            if (isValid.isInt(maxPlayers)) {
                newGameRecord.setMaxPlayers(maxPlayers);
                maxPlayersOk = true;
            }
            if (isValid.isPositiveDouble(price)) {
                newGameRecord.setPrice(price);
                priceOk = true;
            }
            if (isValid.isValidString(description)) {
                newGameRecord.setDescription(description);
                descriptionOk = true;
            }
        } catch (Exception ex) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Bad Game Record",
                    "This file contains invalid Game Record fields. "
                    + ex.getMessage());
        }

        if (nameOk && minPlayersOk && maxPlayersOk && priceOk
                && descriptionOk) {
            return newGameRecord;
        } else {
            return new GameRecord();
        }

    }

    /**
     * A method that calls the getGameRecords() method using the file that is
     * selected in the ListView of files. Called when the load button is
     * clicked. Catches the FileNotFound exception that is possibly thrown in
     * the getGameRecords() method.
     */
    @FXML
    public void load() {
        resetAfterLoad();
        String selectedItem = lstFiles.getSelectionModel().getSelectedItem();
        try {
            getGameRecords(selectedItem);
        } catch (FileNotFoundException ex) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "File Not Found",
                    "This file does not exist.");
            alert.showAndWait();
            resetAfterLoad();
        }
    }

    /**
     * A method that looks at selected players in the ListView of players and
     * returns them as an ArrayList of Integer objects. Every index represents
     * the minPlayers and the last index represents the maxPlayer.
     *
     * @return ArrayList of Integers that represents the minPlayers and
     * maxPlayer
     */
    public ArrayList<Integer> getMinAndMaxPlayers() {
        ObservableList<Integer> selectedPlayers
                = lstPlayers.getSelectionModel().getSelectedItems();

        ArrayList<Integer> minAndMaxPlayers = new ArrayList<Integer>();
        for (int i = 0; i < selectedPlayers.size(); i++) {
            Integer players = selectedPlayers.get(i);
            minAndMaxPlayers.add(players);
        }

        return minAndMaxPlayers;
    }

    /**
     * A method that retrieves GameRecords that match the user's specifications
     * of minPlayers and maxPlayers and returns them as an ArrayList of
     * GameRecord.
     *
     * @param games ArrayList of GameRecord to be checked against the user's
     * specifications of minPlayers and maxPlayer
     * @return ArrayList of GameRecord containing all GameRecords that match the
     * user's specifications of minPlayers and maxPlayer
     */
    public ArrayList<GameRecord> getGames(ArrayList<GameRecord> games) {

        ArrayList<Integer> minAndMaxPlayers = getMinAndMaxPlayers();
        //loop from absolute min to absolute max
        for (int i = 0; i < minAndMaxPlayers.size(); i++) {
            int minPlayer = minAndMaxPlayers.get(i);

            //loop from relative min to relative max
            for (int k = i; k < minAndMaxPlayers.size(); k++) {
                int maxPlayer = minAndMaxPlayers.get(k);

                //loop through games
                for (int j = 0; j < games.size(); j++) {
                    int thisGamesMinPlayers = games.get(j).getMinPlayers();
                    int thisGamesMaxPlayers = games.get(j).getMaxPlayers();

                    if ((thisGamesMinPlayers == minPlayer)
                            && (thisGamesMaxPlayers == maxPlayer)) {
                        displayableGames.add(games.get(j));
                    }
                }
            }
        }

        return displayableGames;
    }

    /**
     * A method that displays the games that match the user's specifications of
     * minPlayers and maxPlayers.
     */
    @FXML
    public void displayGames() {
        ddlGames.getSelectionModel().clearSelection();
        resetAfterCheckGames();
        displayableGames.clear();
        displayableGames = getGames(games);

        if (displayableGames.size() > 0) {

            ArrayList<String> gameRecords = new ArrayList<String>();
            for (int i = 0; i < displayableGames.size(); i++) {
                String gameRecordString = displayableGames.get(i).getGameName();
                gameRecords.add(gameRecordString);

            }

            ObservableList<String> obsGamesList
                    = FXCollections.observableArrayList(gameRecords);
            ddlGames.setItems(obsGamesList);
            ddlGames.getSelectionModel().clearSelection();

            int numOfGames = gameRecords.size();

            String alertString = "";
            if (gameRecords.size() > 1) {
                alertString = String.format("%d game records match your player"
                        + " specifications.", numOfGames);
            } else {
                alertString = String.format("%d game record matches your player"
                        + " specifications.", numOfGames);
            }

            Alert alert = createAlert(Alert.AlertType.INFORMATION,
                    "Matched Games", alertString);
            alert.showAndWait();
        } else {
            Alert alert = createAlert(Alert.AlertType.ERROR, "No Matches",
                    "No game records that match your player specifications "
                    + "exist.");
            alert.showAndWait();
            lstPlayers.getSelectionModel().clearSelection();
            btnCheck.setVisible(false);
            resetAfterCheckGames();
        }

    }

    /**
     * A method that retrieves the GameRecord to be stores and saves it within a
     * file that is named after its minPlayer and maxPlayer.
     *
     * @throws FileNotFoundException if the path to file does not exist.
     */
    @FXML
    public void saveGame() {

        if (ddlGames.getSelectionModel().isEmpty()) {
            Alert alert = createAlert(Alert.AlertType.ERROR,
                    "Game Not Selected", "Please select a game!");
            alert.showAndWait();
        }

        if (thisIndex != -1) {
            Alert confirm = createAlert(Alert.AlertType.CONFIRMATION,
                    "Save Game", "Are you sure you want to save the game?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    String fileName = appendGame();
                    Alert alert = createAlert(Alert.AlertType.INFORMATION,
                            "Game Record Saved", "Your game was saved in "
                            + "the game_collections directory as "
                            + fileName + ".");
                    alert.showAndWait();
                    ddlGames.getSelectionModel().clearSelection();
                    txtDescription.setText("");
                    btnSave.setVisible(false);
                } catch (IOException ex) {
                    Alert alert = createAlert(Alert.AlertType.ERROR,
                            "IOException", "Error in writing to the file!");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = createAlert(Alert.AlertType.ERROR,
                    "Game Not Selected", "Please select a game!");
            alert.showAndWait();
        }

    }

    /**
     * A method that appends a GameRecord to a file titled as the minimum and
     * maximum players of the GameRecord.
     *
     * @throws IOException if there is a problem with the path to the file
     */
    public String appendGame() throws IOException {
        GameRecord recordToSave = displayableGames.get(thisIndex);
        int minPlayers = recordToSave.getMinPlayers();
        int maxPlayers = recordToSave.getMaxPlayers();
        String fileName = "mygamesfor" + minPlayers
                + "to" + maxPlayers + "players.txt";
        String gameRecordToSave = recordToSave.getGameName() + ","
                + recordToSave.getMinPlayers() + ","
                + recordToSave.getMaxPlayers() + ","
                + recordToSave.getPrice() + ","
                + recordToSave.getDescription();

        FileWriter fileWriter = new FileWriter("src/game_collections/"
                + fileName, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(gameRecordToSave);
        printWriter.close();

        return fileName;
    }

    /**
     * A method to create an Alert dialog when called.
     *
     * @param type represents the type of Alert dialog
     * @param title represents the title to be set
     * @param context represents the context text to be set
     * @return Alert dialog object containing the parameters above
     */
    public Alert createAlert(Alert.AlertType type, String title,
            String context) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        return alert;
    }

    /**
     * A method that hides and clears the selection of the ListView, ComboBox
     * and TextArea after clicking the load button.
     */
    public void resetAfterLoad() {
        lstPlayers.getSelectionModel().clearSelection();
        lstPlayers.getItems().clear();
        btnCheck.setVisible(false);
        ddlGames.getItems().clear();
        btnSave.setVisible(false);
        txtDescription.setText("");
    }

    /**
     * A method that hides and clears the selection of the ComboBox and TextArea
     * after clicking the check games button.
     */
    public void resetAfterCheckGames() {
        txtDescription.setText("");
        btnSave.setVisible(false);
        ddlGames.getItems().clear();
    }

    /**
     * A method that alerts the user that they are about to quit the session. If
     * the user confirms that they will quit the session, the program exits.
     */
    @FXML
    public void quitGame() {
        Alert confirm = createAlert(Alert.AlertType.CONFIRMATION,
                "Quit Session", "Are you sure you want to quit this session?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

}//end of class
