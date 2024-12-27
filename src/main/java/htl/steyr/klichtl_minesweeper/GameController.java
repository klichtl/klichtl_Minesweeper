package htl.steyr.klichtl_minesweeper;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    public GridPane Grid;
    @FXML
    public ChoiceBox<String> chosenDifficulty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chosenDifficulty.setItems(FXCollections.observableArrayList("Beginner", "Advanced", "Professional"));
        chosenDifficulty.setValue("Beginner");
    }

    public String getDifficulty() {
        String difficultyPath = "";

        String difficulty = chosenDifficulty.getValue();

        switch (difficulty) {
            case "Beginner":
                difficultyPath = "/htl/steyr/klichtl_minesweeper/difficultys/beginner-Grid-view.fxml";
                break;
            case "Advanced":
                difficultyPath = "/htl/steyr/klichtl_minesweeper/difficultys/advanced-Grid-view.fxml";
                break;
            case "Professional":
                difficultyPath = "/htl/steyr/klichtl_minesweeper/difficultys/professional-Grid-view.fxml";
                break;
            default:
                throw new IllegalStateException("Error loading Difficulty");
        }

        return difficultyPath;
    }

    @FXML
    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loadGrid = new FXMLLoader(getClass().getResource(getDifficulty()));
        GridPane selectedGrid = loadGrid.load();

        Grid.getChildren().clear();
        Grid.add(selectedGrid, 0, 0);
    }
}