package htl.steyr.klichtl_minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    public GridPane Grid;
    @FXML
    public ChoiceBox<String> chosenDifficulty;

    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    public HashMap<String, DifficultySettings> difficultys = new HashMap<>() {{
        put("Beginner", new DifficultySettings(
                "/htl/steyr/klichtl_minesweeper/difficultys/beginner-Grid-view.fxml",
                8, 8, 10));
        put("Advanced", new DifficultySettings(
                "/htl/steyr/klichtl_minesweeper/difficultys/advanced-Grid-view.fxml",
                16, 16, 40));
        put("Professional", new DifficultySettings(
                "/htl/steyr/klichtl_minesweeper/difficultys/professional-Grid-view.fxml",
                16, 30, 99));
    }};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (chosenDifficulty != null) {
            chosenDifficulty.getItems().addAll("Beginner", "Advanced", "Professional");
            chosenDifficulty.setValue("Beginner");
        }
    }

    public DifficultySettings getDifficulty() {
        String difficulty = chosenDifficulty.getValue();
        DifficultySettings settings = difficultys.get(difficulty);

        if (settings != null) {
            ROWS = settings.getROWS();
            COLS = settings.getCOLS();
            MINES = settings.getMINES();
        } else {
            throw new IllegalArgumentException("Error loading Difficulty");
        }
        return settings;
    }

    @FXML
    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {
        DifficultySettings settings = getDifficulty();
        FXMLLoader loadGrid = new FXMLLoader(getClass().getResource(settings.getPath_To_Fxml()));
        GridPane selectedGrid = loadGrid.load();

        Grid.getChildren().clear();
        Grid.add(selectedGrid, 0, 0);

        for (int col = 0; col < COLS; ++col) {
            for (int row = 0; row < ROWS; ++row) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("button-view.fxml"));
                Pane buttonPane = loader.load();
                ButtonController controller = loader.getController();


                buttonPane.setUserData(controller);

                Grid.add(buttonPane, col, row);
            }
        }
    }


    public void revealFields(Integer col, Integer row) {

        /*
         * Schritt 1: Decke das Feld auf
         */


        /*
         * Schritt 2: Wenn keine Bomve daneben liegen,
         *            rufe die Funktion rekursiv auf und
         *            decke wiederum alle umligenden Felder auf!
         */

    }

}