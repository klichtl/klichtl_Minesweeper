package htl.steyr.klichtl_minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    public ChoiceBox<String> chosenDifficulty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /* Set the values for the ChoiceBox (Difficulty) */
        chosenDifficulty.getItems().addAll("Beginner", "Advanced", "Professional");

    }

    public void onStartGameButtonClicked(ActionEvent actionEvent) {

        /* See what difficulty was chosen, and therefore, change what grid is displayed */

//        switch (getChosenDifficulty()) {
//            case "Beginner":
//
//                break;
//            case "Advanced":
//
//                break;
//            case "Professional":
//
//                break;
//        }

    }

    public ChoiceBox<String> getChosenDifficulty() {
        return chosenDifficulty;
    }

    public void setChoseDifficulty(ChoiceBox<String> choseDifficulty) {
        this.chosenDifficulty = choseDifficulty;
    }
}