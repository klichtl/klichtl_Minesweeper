package htl.steyr.klichtl_minesweeper;

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

    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (chosenDifficulty != null) {
            chosenDifficulty.getItems().addAll("Beginner", "Advanced", "Professional");
            chosenDifficulty.setValue("Beginner");
        }
    }

    public String getDifficulty() {
        String difficulty = chosenDifficulty.getValue();
        String difficultyPath = "";

        switch (difficulty) {
            case "Beginner":
                difficultyPath = "/htl/steyr/klichtl_minesweeper/difficultys/beginner-Grid-view.fxml";
                    ROWS = 8;
                    COLS = 8;
                    MINES = 10;
                break;
            case "Advanced":
                difficultyPath = "/htl/steyr/klichtl_minesweeper/difficultys/advanced-Grid-view.fxml";
                    ROWS = 16;
                    COLS = 16;
                    MINES = 40;
                break;
            case "Professional":
                difficultyPath = "/htl/steyr/klichtl_minesweeper/difficultys/professional-Grid-view.fxml";
                    ROWS = 16;
                    COLS = 30;
                    MINES = 99;
                break;
            default:
                throw new IllegalArgumentException("Error loading Difficulty");
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