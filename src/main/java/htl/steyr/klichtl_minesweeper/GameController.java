
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
import java.util.Objects;
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

    public int ROWS;
    public int COLS;
    public int MINES;
    public int fieldsMarked = 0;
    public int secondsElapsed = 0;

    public Timeline timeline;

    private boolean gameOver = false;
    private boolean firstClick = true;

    private String currentDifficulty;

    ButtonController buttonController = new ButtonController();

    /* Hashmap to store the different Settings appropriate to the Difficulty */
    public HashMap<String, DifficultySettings> difficultys = new HashMap<>() {{
        put("Beginner", new DifficultySettings(6, 10, 15));
        put("Advanced", new DifficultySettings(15, 25, 35));
        put("Professional", new DifficultySettings(18, 30, 120));
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

    /**
     * Description for the onStartButtonClicked method:
     * <p>
     * In this method, all elements that get changed during the game, are set back to their beginning value.
     * In addition to that, the Grid is filled with Buttons.
     */
    @FXML
    public void onStartButtonClicked() throws IOException {
        // Reset the game state and UI
        Grid.getChildren().clear();
        Grid.getRowConstraints().clear();
        Grid.getColumnConstraints().clear();

        gameOver = false;
        firstClick = true;
        Timer();

        setEvenSize();
        setFieldsMarked(0);

        for (int col = 0; col < COLS; ++col) {
            for (int row = 0; row < ROWS; ++row) {
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

    /**
     * Description for the Timer method:
     * <p>
     * In this Method, a timer is started, and every second, the timer gets updated.
     */
    public void Timer() {
        stopTimer();
        secondsElapsed = 0;
        TimeElapsed.setText("Time elapsed: " + secondsElapsed + "s");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            TimeElapsed.setText("Time elapsed: " + secondsElapsed + "s");
        }));

        /* repeat an endless time */
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Description for the stopTimer method:
     * <p>
     * In this Method, the timer gets stopped.
     */
    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Description for the revealFields method:
     * <p>
     * In this Methode, first it is checked, if the click is the first click at the current game, and therefor the mines get placed excluding the Field that is clicked.
     * After that, two for-loops are called, to go through the "Fields" that surround the clicked Button.
     * if there are zero mines nearby, the method is called recursive.
     *
     * @param COL the Colum of the Button, that is clicked
     * @param ROW the Row of the Button, that is clicked
     */
    public void revealFields(int COL, int ROW) {
        if (firstClick) {
            placeMinesAfterFirstClick(COL, ROW);
            firstClick = false;
        }
        for (int col = (-1); col <= 1; ++col) {
            for (int row = (-1); row <= 1; ++row) {
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

    /**
     * Description for the revealAllFields method:
     * <p>
     * In this Methode, all fields are revealed.
     */
    public void revealAllFields() {
        for (int col = 0; col < COLS; ++col) {
            for (int row = 0; row < ROWS; ++row) {
                buttonController = getController(col, row);
                if (buttonController != null && !buttonController.isRevealed()) {
                    buttonController.reveal();
                }
            }
        }
    }

    /**
     * Description for the placeMinesAfterFirstClick method:
     * <p>
     * In this Method, the coordinates, of the first Button clicked, and the surrounding Buttons are store,
     * so they can get excluded.
     * Now we can place all the mines, and exclude the before mentioned Fields.
     * After all that, all fields are getting the value of the bombs nearby set.
     *
     * @param firstClickCol Colum of the Button, that was clicked first
     * @param firstClickRow Row of the Button, that was clicked first
     */
    public void placeMinesAfterFirstClick(int firstClickCol, int firstClickRow) {
        /* Hashset to store the mines that have been placed */
        HashSet<String> placedMines = new HashSet<>();
        int mineCount = 0;
        Random random = new Random();

        /* Hashset to store the fields that have to be excluded */
        HashSet<String> excludedCoords = new HashSet<>();
        /* exclude the neighbor fields */
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
            int randomCOL = random.nextInt(COLS);
            int randomROW = random.nextInt(ROWS);

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

    /**
     * Description for the getMinesNearPosition method:
     * <p>
     * In this Method, the mines of the Button, that is chosen, gets counted, by looking for bombs in the neighbor Fields.
     * Depending on the count, the number gets a different colour.
     *
     * @param COL the selected Colum
     * @param ROW the selected Row
     * @return minesNearby
     */
    public int getMines_Near_Position(int COL, int ROW) {
        int minesNearby = 0;

        for (int col = (-1); col <= 1; ++col) {
            for (int row = (-1); row <= 1; ++row) {
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

    /**
     * Description for the getController method:
     * <p>
     * In this Method, the controller for the Button depending on the Col and Row is gotten.
     */
    private ButtonController getController(int COL, int ROW) {
        for (Node child : Grid.getChildren()) {
            if (GridPane.getColumnIndex(child) == COL && GridPane.getRowIndex(child) == ROW) {
                return (ButtonController) child.getUserData();
            }
        }
        return null;
    }

    /**
     * Description for the checkGameStatus method:
     * <p>
     * In this Method, it is checked, if the user has already won.
     * This is done, by checking if the user has revealed all non-mine-fields and marked all mine-fields.
     * Two booleans are created, and set to true, but if the conditions above are not true, the booleans are set to false,
     * and the game goes on, until eventually the user has won, and then the Game-Over-Screen is displayed and the user has won.
     */
    public void checkGameStatus() {
        boolean allNonMinesRevealed = true;
        boolean allMinesMarked = true;

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

    /**
     * Description for the showGameOverScreen method:
     * <p>
     * In this Method, the Game-Over-Screen gets displayed.
     *
     * @param won boolean, to see if the user has won = true, or not won = false
     */
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
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Minesweeper-Icon.png"))));
            stage.show();
        } catch (IOException e) {
            System.out.println("Game-over-view could not be found");
        }
    }

    /**
     * Description for the setDifficulty method:
     * <p>
     * In this Method, the data from the difficulty-Settings, depending on the selected
     * difficulty get written into the local variables.
     *
     * @param difficulty the difficulty selected
     */
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

    /**
     * Description for the setDifficulty method:
     * <p>
     * In this Method, the Rows and Columns get the same size, by taking 100% of the grid,
     * and deviding that with the amount of Rows/Cols.
     */
    public void setEvenSize() {
        for (int row = 0; row < ROWS; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / ROWS);
            Grid.getRowConstraints().add(rowConstraints);
        }

        for (int col = 0; col < COLS; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / COLS);
            Grid.getColumnConstraints().add(colConstraints);
        }
    }

    public int getFieldsMarked() {
        return fieldsMarked;
    }

    /**
     * Description for the setFieldsMarked method:
     * <p>
     * In this Method, the text of the MinesRemaining TextField is updated
     */
    public void setFieldsMarked(int fieldsMarked) {
        this.fieldsMarked = fieldsMarked;
        /* update the TExtField */
        MinesRemaining.setText("Flags remaining: " + (MINES - fieldsMarked));
    }

    /**
     * Description for the switchTOMenu method:
     * <p>
     * In this Method, the active stage is switched to the menu-view.
     */
    public void switchToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) switch_to_Menu_Button.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}