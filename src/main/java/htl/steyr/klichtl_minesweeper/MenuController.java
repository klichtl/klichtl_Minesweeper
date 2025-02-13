package htl.steyr.klichtl_minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    public Button switch_to_Game_Button;
    @FXML
    public ChoiceBox<String> chosenDifficulty;
    @FXML
    public Text timeBeginner;
    @FXML
    public Text timeAdvanced;
    @FXML
    public Text timeProfessional;
    @FXML
    public Text usernameBeginner;
    @FXML
    public Text usernameAdvanced;
    @FXML
    public Text usernameProfessional;

    /**
     * Description for the initialize method:
     * <p>
     * In this Methode, the available difficulties are set.
     * Also, a default value is set (Beginner).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (chosenDifficulty != null) {
            /* Add all Difficulty Options */
            chosenDifficulty.getItems().addAll("Beginner", "Advanced", "Professional");
            /* Default Difficulty */
            chosenDifficulty.setValue("Beginner");
        }
        getHighScores();
    }

    /**
     * Description for the switchToGame method:
     * <p>
     * In this Methode, the game-view is shown in the current stage.
     */
    @FXML
    public void switchToGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        Parent root = loader.load();

        GameController gameController = loader.getController();
        String difficulty = chosenDifficulty.getValue();
        gameController.setDifficulty(difficulty);
        gameController.onStartButtonClicked();

        Stage stage = (Stage) switch_to_Game_Button.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Description for the getHighScores method:
     * <p>
     * In this Methode, the best times per difficulty is decided, and then displayed on the menu.
     * If no record is available of the difficulty, N/A is displayed.
     */
    public void getHighScores() {
        List<String[]> table = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("game_results.csv"))) {
            String row;
            while ((row = br.readLine()) != null) {
                String[] data = row.split(";");
                table.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String[] bestTimeBeginner = null;
        String[] bestTimeAdvanced = null;
        String[] bestTimeProfessional = null;

        int minTimeBeginner = Integer.MAX_VALUE;
        int minTimeAdvanced = Integer.MAX_VALUE;
        int minTimeProfessional = Integer.MAX_VALUE;

        for (String[] row : table) {
            String difficulty = row[2];
            int time = Integer.MAX_VALUE;

            try {
                time = Integer.parseInt(row[1].trim());
            } catch (NumberFormatException e) {
                /* if an invalid number is detected, skip the row */
                continue;
            }

            if (difficulty.equals("Beginner") && time < minTimeBeginner) {
                minTimeBeginner = time;
                bestTimeBeginner = row;
            } else if (difficulty.equals("Advanced") && time < minTimeAdvanced) {
                minTimeAdvanced = time;
                bestTimeAdvanced = row;
            } else if (difficulty.equals("Professional") && time < minTimeProfessional) {
                minTimeProfessional = time;
                bestTimeProfessional = row;
            }
        }

        if (bestTimeBeginner != null) {
            usernameBeginner.setText(bestTimeBeginner[0]);
            timeBeginner.setText(bestTimeBeginner[1] + "s");
        } else {
            usernameBeginner.setText("N/A");
            timeBeginner.setText("N/A");
        }

        if (bestTimeAdvanced != null) {
            usernameAdvanced.setText(bestTimeAdvanced[0]);
            timeAdvanced.setText(bestTimeAdvanced[1] + "s");
        } else {
            usernameAdvanced.setText("N/A");
            timeAdvanced.setText("N/A");
        }

        if (bestTimeProfessional != null) {
            usernameProfessional.setText(bestTimeProfessional[0]);
            timeProfessional.setText(bestTimeProfessional[1] + "s");
        } else {
            usernameProfessional.setText("N/A");
            timeProfessional.setText("N/A");
        }
    }
}