package htl.steyr.klichtl_minesweeper;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonController implements Initializable {

    public Label mine_Label;
    public Label info_Label;
    public Button button;

    private Boolean is_A_Mine = false;
    private Boolean is_Marked = false;
    private Boolean is_Revealed = false;
    private Integer mines_Nearby = 0;

    private Integer COL = -1;
    private Integer ROW = -1;

    private GameController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (mine_Label != null) {
            mine_Label.setVisible(false);
            info_Label.setVisible(false);
        }
    }

    public void buttonClicked(MouseEvent mouseEvent) throws MineException {

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

    public void reveal() throws MineException {
        is_Revealed = true;
        button.setVisible(false);

        if (isMine()) {
            mine_Label.setVisible(true);
            throw new MineException();
        } else {
            info_Label.setVisible(true);
            if (getMines_Nearby() == 0) {
                controller.revealFields(COL, ROW);
            }
        }
    }


    public boolean isMine() {
        return is_A_Mine;
    }

    public void setMine(boolean mine) {
        this.is_A_Mine = mine;
        mine_Label.setVisible(mine);
    }

    public void setMines_Nearby(int mineCount) {
        this.mines_Nearby = mineCount;

        if (!isMine()) {
            mine_Label.setVisible(false);
            info_Label.setVisible(true);

            info_Label.setText(Integer.toString(mines_Nearby));
        }
    }

    public Integer getMines_Nearby() {
        return mines_Nearby;
    }

    public Boolean isRevealed() {
        return is_Revealed;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }
}