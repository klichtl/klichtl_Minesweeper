package htl.steyr.klichtl_minesweeper;

import javafx.event.ActionEvent;
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
    public TextField usernameText;
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
     * @param won         boolean to see if the player has won or not won
     * @param timeElapsed the time that was elapsed since the game was started
     * @param difficulty  the difficulty that was selected
     */
    public void setGameResult(boolean won, int timeElapsed, String difficulty) {
        this.timeElapsed = timeElapsed;
        if (won) {
            resultLabel.setText("You Won");
        } else {
            resultLabel.setText("You Lost");
            saveButton.setDisable(true);
            usernameText.setDisable(true);
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
    public void onSaveButtonClicked(ActionEvent actionEvent) {
        String username = usernameText.getText().trim();
        if (!username.isEmpty()) {
            User user = new User(username, timeElapsed, difficulty);
            writeGameResultToFile(user);
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
     * @param user User Object with the needed data
     */
    private void writeGameResultToFile(User user) {

        File file = new File("game_results.csv");

        try {
            FileWriter writer = new FileWriter(file, true);

            StringBuilder dataForTable = new StringBuilder();
            dataForTable.append(user.getUsername()).append(";")
                    .append(user.getTime()).append(";")
                    .append(user.getDifficulty());
            writer.write(dataForTable.toString() + "\n");

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving...");
        }
    }
}