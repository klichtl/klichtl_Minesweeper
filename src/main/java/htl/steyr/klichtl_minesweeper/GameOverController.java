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

    private boolean gameWon;
    private int timeElapsed;
    private String difficulty;

    public void setGameResult(boolean won, int timeElapsed, String difficulty) {
        this.gameWon = won;
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

    private void writeGameResultToFile(String username, int timeElapsed) {

        File file = new File("game_results.csv");

        try {
            FileWriter writer = new FileWriter(file, true);

            writer.write(username + ";" + timeElapsed + ";" + difficulty + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving...");
        }
    }
}