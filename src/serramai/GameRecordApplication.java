/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serramai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Maiziel Serrao
 */
public class GameRecordApplication extends Application {

    /**
     * A method that initiates the program and the program setting.
     *
     * @param stage the stage to be initialized and set
     * @throws Exception for any error in initialization
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().
                getResource("FXMLGameRecordApplication.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("GAME RECORD APPLICATION!!!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The method that launches the arguments and runs the code.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
