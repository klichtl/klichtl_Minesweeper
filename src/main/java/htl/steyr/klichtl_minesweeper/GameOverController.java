package htl.steyr.klichtl_minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    public Button saveButtonClicked;

    private Boolean gameWon;
    private Integer timeElapsed;
    private String difficulty;

    public void setGameResult(Boolean won, Integer timeElapsed, String difficulty) {
        this.gameWon = won;
        this.timeElapsed = timeElapsed;

        if (won) {
            resultLabel.setText("You Won");
        } else {
            resultLabel.setText("Game Over");
        }

        timeLabel.setText("Time Elapsed: " + timeElapsed + "s");

        this.difficulty = difficulty;
    }


    @FXML
    public void onSaveButtonClicked() {
        String user = username.getText().trim();
        if (!user.isEmpty()) {
            writeGameResultToFile(user, gameWon, timeElapsed);
        } else {
            System.out.println("Please enter a valid username!");
        }
    }

    private void writeGameResultToFile(String username, Boolean won, Integer timeElapsed) {
        File file = new File("game_results.csv");

        try (FileWriter writer = new FileWriter(file, true)) {
            if (!file.exists() || file.length() == 0) {
                writer.write("username;result;time;difficulty\n");
            }

            String result;
            if (won) {
                result = "won";
            } else {
                result = "lost";
            }

            writer.write(username + ";" + result + ";" + timeElapsed + ";" + difficulty + "\n");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving...");
        }
    }
}