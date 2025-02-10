package htl.steyr.klichtl_minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    public Button switch_to_Game_Button;
    @FXML
    public ChoiceBox<String> chosenDifficulty;

    /**
     * Description for the initialize method:
     * <p>
     * In this Methode, the available difficulties are set.
     * Also, a default value is set (Beginner).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (chosenDifficulty != null) {
            /* Add all Difficulty Options */
            chosenDifficulty.getItems().addAll("Beginner", "Advanced", "Professional");
            /* Default Difficulty */
            chosenDifficulty.setValue("Beginner");
        }
    }

    /**
     * Description for the switchToGame method:
     * <p>
     * In this Methode, the game-view is shown in the current stage.
     */
    @FXML
    public void switchToGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        Parent root = loader.load();

        GameController gameController = loader.getController();
        String difficulty = chosenDifficulty.getValue();
        gameController.setDifficulty(difficulty);
        gameController.onStartButtonClicked();

        Stage stage = (Stage) switch_to_Game_Button.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}