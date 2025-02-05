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
import javafx.scene.image.Image;
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
    public Button switch_to_Menu_Button;

    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    public Integer fieldsMarked = 0;
    public Integer secondsElapsed = 0;

    public Timeline timeline;

    private Boolean gameOver = false;
    private Boolean firstClick = true;

    private String currentDifficulty;

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

        gameOver = false;
        firstClick = true;
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
    }

    public void revealFields(Integer COL, Integer ROW) {
        if (firstClick) {
            placeMinesAfterFirstClick(COL, ROW);
            firstClick = false;
        }
        for (Integer col = (-1); col <= 1; ++col) {
            for (Integer row = (-1); row <= 1; ++row) {
                if (COL + col >= 0 && COL + col < COLS && ROW + row >= 0 && ROW + row < ROWS) {
                    buttonController = getController(COL + col, ROW + row);
                    if (buttonController != null && !buttonController.isRevealed() && !buttonController.isMine()) {
                        buttonController.reveal();

                        /* recursive function call */
                        if (buttonController.getMines_Nearby() == 0) {
                            revealFields(COL + col, ROW + row);
                        }
                    }
                }
            }
        }
        checkGameStatus();
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
        this.currentDifficulty = difficulty;
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


    public void checkGameStatus() {
        Boolean allNonMinesRevealed = true;
        Boolean allMinesMarked = true;

        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                ButtonController buttonController = getController(col, row);

                if (buttonController != null) {
                    if (!buttonController.isMine() && !buttonController.isRevealed()) {
                        allNonMinesRevealed = false;
                    }
                    if (buttonController.isMine() && !buttonController.is_Marked) {
                        allMinesMarked = false;
                    }
                }
            }
        }

        /* if all non-mine-fields are revealed and all mines are marked, the player has won */
        if (allNonMinesRevealed && allMinesMarked) {
            stopTimer();
            showGameOverScreen(true);
        }
    }

    public void showGameOverScreen(boolean won) {
        if (gameOver) {
            return;
        }
        gameOver = true;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-over-view.fxml"));
            Parent root = loader.load();

            GameOverController gameOverController = loader.getController();
            gameOverController.setGameResult(won, secondsElapsed, currentDifficulty);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Game-Over");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Minesweeper-Icon.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placeMinesAfterFirstClick(int firstClickCol, int firstClickRow) {
        /* Hashset to store the mines that have been placed */
        HashSet<String> placedMines = new HashSet<>();
        Integer mineCount = 0;
        Random random = new Random();

        /* Hashset to store the fields that have to be excluded */
        HashSet<String> excludedCoords = new HashSet<>();
        /* exclude the neighbor fields from  */
        for (int col = -1; col <= 1; col++) {
            for (int row = -1; row <= 1; row++) {
                if (firstClickCol + col >= 0 && firstClickCol + col < COLS && firstClickRow + row >= 0 && firstClickRow + row < ROWS) {
                    excludedCoords.add((firstClickCol + col) + "," + (firstClickRow + row));
                }
            }
        }
        /* exclude the first button clicked */
        excludedCoords.add(firstClickCol + "," + firstClickRow);

        /* place the mines excluding the neighbor fields after clicking the first button */
        while (mineCount < MINES) {
            Integer randomCOL = random.nextInt(COLS);
            Integer randomROW = random.nextInt(ROWS);

            String coordinates = randomCOL + "," + randomROW;

            if (!placedMines.contains(coordinates) && !excludedCoords.contains(coordinates)) {
                placedMines.add(coordinates);
                ButtonController buttonController = getController(randomCOL, randomROW);
                if (buttonController != null) {
                    buttonController.setMine(true);
                }
                mineCount++;
            }
        }

        /* set the value of mines nearby */
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                ButtonController buttonController = getController(col, row);
                if (buttonController != null) {
                    buttonController.setMines_Nearby(getMines_Near_Position(col, row));
                }
            }
        }
    }


}