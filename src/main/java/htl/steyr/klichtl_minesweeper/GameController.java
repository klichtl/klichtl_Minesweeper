package htl.steyr.klichtl_minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    public GridPane Grid;
    @FXML
    public ChoiceBox<String> chosenDifficulty;
    @FXML
    public TextField MinesRemaining;
    @FXML
    public TextField TimeElapsed;

    private Timeline timeline;

    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    public Integer fieldsMarked = 0;

    public Integer secondsElapsed = 0;

    ButtonController buttonController = new ButtonController();

    /* HashMap with the difficulty and the DifficultySettings to store information about the difficulty */

    public HashMap<String, DifficultySettings> difficultys = new HashMap<>() {{
        put("Beginner", new DifficultySettings(8, 12, 15));
        put("Advanced", new DifficultySettings(16, 24, 35));
        put("Professional", new DifficultySettings(20, 30, 120));
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

    @FXML
    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {

        Grid.getChildren().clear();
        Grid.getRowConstraints().clear();
        Grid.getColumnConstraints().clear();

        getDifficulty();
        setEvenSize();
        setFieldsMarked(0);
        Timer();

        /* fill every field with a button depending on the difficulty selected */
        for (Integer col = 0; col < COLS; ++col) {
            for (Integer row = 0; row < ROWS; ++row) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("button-view.fxml"));
                Pane buttonPane = loader.load();
                buttonController = loader.getController();
                buttonController.setController(this);

                buttonController.setCoordinates(col, row);

                buttonPane.setUserData(buttonController);

                Grid.add(buttonPane, col, row);
            }
        }

        Integer mineCount = 0;
        HashSet<String> placedMines = new HashSet<>();

        while (mineCount < MINES) {
            Random random = new Random();
            Integer randomCOL = random.nextInt(COLS);
            Integer randomROW = random.nextInt(ROWS);

            String coordinates = randomCOL + "," + randomROW;

            /* set a mine, only if there is not an existing mine under the button */
            if (!placedMines.contains(coordinates)) {
                placedMines.add(coordinates);
                buttonController = getController(randomCOL, randomROW);
                if (buttonController != null) {
                    buttonController.setMine(true);
                }

                ++mineCount;
            }
        }

        for (Integer col = 0; col < COLS; ++col) {
            for (Integer row = 0; row < ROWS; ++row) {
                buttonController = getController(col, row);
                buttonController.setMines_Nearby(getMines_Near_Position(col, row));
            }
        }
    }

    public void revealFields(Integer COL, Integer ROW) {

        /* If zero mines are nearby, show all surrounding fields */

        for (Integer col = (-1); col <= 1; ++col) {

            for (Integer row = (-1); row <= 1; ++row) {

                if (COL + col >= 0 && COL + col < COLS && ROW + row >= 0 && ROW + row < ROWS) {
                    buttonController = getController(COL + col, ROW + row);

                    if (buttonController != null && !buttonController.isRevealed() && !buttonController.isMine()) {
                        try {
                            buttonController.reveal();
                        } catch (MineException e) {
                            System.out.println("Game Over! Mine found at: " + COL + " | " + ROW);
                        }

                        if (buttonController.getMines_Nearby() == 0) {
                            revealFields(COL + col, ROW + row);
                        }
                    }
                }
            }
        }
    }

    private ButtonController getController(int COL, int ROW) {
        for (Node child : Grid.getChildren()) {
            if (GridPane.getColumnIndex(child) == COL && GridPane.getRowIndex(child) == ROW) {
                return (ButtonController) child.getUserData();
            }
        }
        return null;
    }

    public int getMines_Near_Position(int COL, int ROW) {

        int minesNearby = 0;

        for (Integer col = (-1); col <= 1; ++col) {
            for (Integer row = (-1); row <= 1; ++row) {
                if (col != 0 || row != 0) {
                    /* Get the controller from the surrounding fields */
                    buttonController = getController(COL + col, ROW + row);

                    if (buttonController != null && buttonController.isMine()) {
                        ++minesNearby;
                    }
                }
            }
        }

//        if (buttonController != null && buttonController.info_Label != null) {
//            switch (minesNearby) {
//                case 1:
//                    buttonController.info_Label.setStyle("-fx-text-fill: blue");
//                    break;
//                case 2:
//                    buttonController.info_Label.setStyle("-fx-text-fill: green");
//                    break;
//                case 3:
//                    buttonController.info_Label.setStyle("-fx-text-fill: red");
//                    break;
//            }
//        }

        return minesNearby;
    }

    public void setEvenSize() {

        /* give every Row the same space */
        for (Integer row = 0; row < ROWS; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / ROWS);
            Grid.getRowConstraints().add(rowConstraints);
        }

        /* give every Colum the same space */
        for (Integer col = 0; col < COLS; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / COLS);
            Grid.getColumnConstraints().add(colConstraints);
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

    public int getFieldsMarked() {
        return fieldsMarked;
    }

    public void setFieldsMarked(int fieldsMarked) {
        this.fieldsMarked = fieldsMarked;
        updateFlagsRemaining();
    }

    public void updateFlagsRemaining() {
        MinesRemaining.setText("Flags remaining: " + (MINES - fieldsMarked));
    }

    public void Timer() {
        if (timeline == null) { // Überprüfen, ob die Timeline bereits existiert
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                secondsElapsed++;
                TimeElapsed.setText("Time elapsed: " + secondsElapsed + "s");
            }));

            /* repeat timeless amounts */
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }
}