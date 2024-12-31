package htl.steyr.klichtl_minesweeper;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonController implements Initializable {

    public Label bomb_Label;
    public Label info_Label;
    public Button button;

    private boolean is_A_Bomb = false;
    private boolean is_Marked = false;
    private boolean is_Revealed = false;
    private Integer bombs_Nearby = 0;

    private int col = -1; // ??
    private int row = -1; // ??

    private GameController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (bomb_Label != null) {
            bomb_Label.setVisible(false);
            info_Label.setVisible(false);
        }
    }

    public void buttonClicked(MouseEvent mouseEvent) {

        if (mouseEvent != null) {

            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                if (!is_Marked) {
                    is_Marked = true;
                    button.setText("🚩");
                } else {
                    is_Marked = false;
                    button.setText("");
                }
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY && !is_Marked) {
                reveal();
            }

        } else {
            reveal();
        }
    }

    public void reveal() {

    }



    public void setPosition(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public boolean isBomb() {
        return is_A_Bomb;
    }

    public void setBomb(boolean bomb) {
        this.is_A_Bomb = bomb;

        bomb_Label.setVisible(bomb);
    }

    public void setBombsNearby(int num) {
        this.bombs_Nearby = num;

        if (!isBomb()) {
            bomb_Label.setVisible(false);
            info_Label.setVisible(true);

            info_Label.setText(Integer.toString(num));
        }
    }

    public int getBombsNearby() {
        return bombs_Nearby;
    }

    public boolean isRevealed() {
        return is_Revealed;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }



}