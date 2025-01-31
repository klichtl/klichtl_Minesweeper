package htl.steyr.klichtl_minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class GameController {

    @FXML
    public GridPane Grid;
    @FXML
    public TextField MinesRemaining;
    @FXML
    public TextField TimeElapsed;
    @FXML
    public TextField game_Table;
    @FXML
    public Button switch_to_Menu_Button;

    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    public Integer fieldsMarked = 0;
    public Integer secondsElapsed = 0;

    public Timeline timeline;

    ButtonController buttonController = new ButtonController();

    /* Hashmap to store the different Settings appropriate to the Difficulty */
    public HashMap<String, DifficultySettings> difficultys = new HashMap<>() {{
        put("Beginner", new DifficultySettings(8, 12, 15));
        put("Advanced", new DifficultySettings(16, 24, 35));
        put("Professional", new DifficultySettings(20, 30, 120));
    }};

    /* Hashmap to store the different colours the info_label get, matching the mines Nearby */
    public HashMap<Integer, String> mineColours = new HashMap<>() {{
        put(1, "#0000FF");  /* Blue         */
        put(2, "#008000");  /* Green        */
        put(3, "#FF0000");  /* Red          */
        put(4, "#000080");  /* Blue         */
        put(5, "#800000");  /* dark Blue    */
        put(6, "#008080");  /* Cyan         */
        put(7, "#000000");  /* Black        */
        put(8, "#808080");  /* Grey         */
    }};

    @FXML
    public void onStartButtonClicked() throws IOException {

        Grid.getChildren().clear();
        Grid.getRowConstraints().clear();
        Grid.getColumnConstraints().clear();

        secondsElapsed = 0;
        Timer();

        setEvenSize();
        setFieldsMarked(0);

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

        /* Hashmap to store the Coordinates of the already placed Mines */
        HashSet<String> placedMines = new HashSet<>();
        Integer mineCount = 0;


        while (mineCount < MINES) {
            Random random = new Random();
            Integer randomCOL = random.nextInt(COLS);
            Integer randomROW = random.nextInt(ROWS);

            String coordinates = randomCOL + "," + randomROW;

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
        for (Integer col = (-1); col <= 1; ++col) {
            for (Integer row = (-1); row <= 1; ++row) {
                if (COL + col >= 0 && COL + col < COLS && ROW + row >= 0 && ROW + row < ROWS) {
                    buttonController = getController(COL + col, ROW + row);

                    if (buttonController != null && !buttonController.isRevealed() && !buttonController.isMine()) {

                        buttonController.reveal();

                        if (buttonController.getMines_Nearby() == 0) {
                            revealFields(COL + col, ROW + row);
                        }
                    }
                }
            }
        }
    }

    public void revealAllFields() {
        for (Integer col = 0; col < COLS; ++col) {
            for (Integer row = 0; row < ROWS; ++row) {
                buttonController = getController(col, row);
                if (buttonController != null && !buttonController.isRevealed()) {
                    buttonController.reveal();
                }
            }
        }
        game_Table.setText("Game over with " + (MINES - fieldsMarked) + " Flags remaining");
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
                    ButtonController neighborController = getController(COL + col, ROW + row);

                    if (neighborController != null && neighborController.isMine()) {
                        ++minesNearby;
                    }
                }
            }
        }

        ButtonController currentController = getController(COL, ROW);
        if (currentController != null && currentController.info_Label != null) {
            String colour = mineColours.getOrDefault(minesNearby, null);

            if (colour != null) {
                currentController.info_Label.setStyle("-fx-text-fill: " + colour);
            } else {
                currentController.info_Label.setText(null);
            }
        }

        return minesNearby;
    }

    public void setEvenSize() {
        for (Integer row = 0; row < ROWS; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / ROWS);
            Grid.getRowConstraints().add(rowConstraints);
        }

        for (Integer col = 0; col < COLS; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / COLS);
            Grid.getColumnConstraints().add(colConstraints);
        }
    }

    public void setDifficulty(String difficulty) {
        DifficultySettings settings = difficultys.get(difficulty);
        if (settings != null) {
            ROWS = settings.getROWS();
            COLS = settings.getCOLS();
            MINES = settings.getMINES();
        } else {
            throw new IllegalArgumentException("Error loading Difficulty");
        }
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
        stopTimer();
        secondsElapsed = 0;
        TimeElapsed.setText("Time elapsed: 0s");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            TimeElapsed.setText("Time elapsed: " + secondsElapsed + "s");
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void switchToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) switch_to_Menu_Button.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
