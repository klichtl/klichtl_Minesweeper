package htl.steyr.klichtl_minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    public GridPane Grid;
    @FXML
    public ChoiceBox<String> chosenDifficulty;

    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    /* HashMap with the difficulty and the DifficultySettings to store information about the difficulty */

    public HashMap<String, DifficultySettings> difficultys = new HashMap<>() {{
        put("Beginner", new DifficultySettings(
                "/htl/steyr/klichtl_minesweeper/difficultys/beginner-Grid-view.fxml",
                8, 12, 15));
        put("Advanced", new DifficultySettings(
                "/htl/steyr/klichtl_minesweeper/difficultys/advanced-Grid-view.fxml",
                16, 24, 35));
        put("Professional", new DifficultySettings(
                "/htl/steyr/klichtl_minesweeper/difficultys/professional-Grid-view.fxml",
                20, 30, 120));
    }};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (chosenDifficulty != null) {
            /* Add the different choices of difficulty */
            chosenDifficulty.getItems().addAll("Beginner", "Advanced", "Professional");

            /* Set a default value */
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

        /* load the file-path to the selected difficulty */
        FXMLLoader loadGrid = new FXMLLoader(getClass().getResource(settings.getPath_To_Fxml()));

        GridPane selectedGrid = loadGrid.load();

        Grid.getChildren().clear();
        Grid.getRowConstraints().clear();
        Grid.getColumnConstraints().clear();

        /* give every Colum and Row the same space */

        for (Integer i = 0; i < ROWS; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / ROWS);
            Grid.getRowConstraints().add(rowConstraints);
        }

        for (Integer i = 0; i < COLS; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / COLS);
            Grid.getColumnConstraints().add(colConstraints);
        }


        /* fill every field with a button */
        for (Integer col = 0; col < COLS; ++col) {
            for (Integer row = 0; row < ROWS; ++row) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("button-view.fxml"));
                Pane buttonPane = loader.load();
                ButtonController controller = loader.getController();
                controller.setController(this);

                buttonPane.setUserData(controller);

                Grid.add(buttonPane, col, row);
            }
        }


    public void revealFields(Integer COL, Integer ROW) {

    }

}