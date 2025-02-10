package htl.steyr.klichtl_minesweeper;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameOverController {

    @FXML
    public Label resultLabel;
    @FXML
    public Label timeLabel;
    @FXML
    public TextField username;
    @FXML
    public Button saveButton;

    private int timeElapsed;
    private String difficulty;

    /**
     * Description for the setGameResult method:
     * <p>
     * In this Methode, the text of the different Labels is set depending on the status of the game.
     * If the player has lost, he is not able to save his record.
     *
     * @param won boolean to see if the player has won or not won
     * @param timeElapsed the time that was elapsed since the game was started
     * @param difficulty the difficulty that was selected
     */
    public void setGameResult(boolean won, int timeElapsed, String difficulty) {
        this.timeElapsed = timeElapsed;
        if (won) {
            resultLabel.setText("You Won");
        } else {
            resultLabel.setText("You Lost");
            saveButton.setDisable(true);
            username.setDisable(true);
        }
        timeLabel.setText("Time Elapsed: " + timeElapsed + "s");
        this.difficulty = difficulty;
    }

    /**
     * Description for the onSaveButtonClicked method:
     * <p>
     * In this Methode, it is checked if the user has entered a username, and therefore is
     * able to save his result with the writeGameResultToFile.
     * Also, if the Save-Button is clicked the Window is closed.
     *
     * @param actionEvent the Save-Button
     */
    public void onSaveButtonClicked(javafx.event.ActionEvent actionEvent) {
        String user = username.getText().trim();
        if (!user.isEmpty()) {
            writeGameResultToFile(user, timeElapsed);
        } else {
            System.out.println("Please enter a username!");
        }

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Description for the writeGameResultToFile method:
     * <p>
     * In this Methode, the username, time, and difficulty is written into the "game-results.csv".
     *
     * @param username the username the user has chosen
     * @param timeElapsed the time that is elapsed since the beginning of the game
     */
    private void writeGameResultToFile(String username, int timeElapsed) {

        File file = new File("game_results.csv");

        try {
            FileWriter writer = new FileWriter(file, true);

            writer.write(username + ";" + timeElapsed + ";" + difficulty + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving...");
        }
    }
}