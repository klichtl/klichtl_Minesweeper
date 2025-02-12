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

    private boolean is_A_Mine = false;
    public boolean is_Marked = false;
    private boolean is_Revealed = false;
    private int mines_Nearby = 0;

    private int COL;
    private int ROW;

    private GameController controller;

    /**
     * Description for the initialize method:
     * <p>
     * In this Methode, the info_ & mine_Label are set to be not visible, this will change later on...
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (mine_Label != null) {
            mine_Label.setVisible(false);
            info_Label.setVisible(false);
        }
    }

    /**
     * Description for the buttonClicked method:
     * <p>
     * In this Methode, mouse clicks are handled.
     * - right click -> set a flag to the Field
     * - left click -> reveal the Field
     *
     * @param mouseEvent the mouseevent triggered by a click.
     */
    public void buttonClicked(MouseEvent mouseEvent) {
        if (mouseEvent != null) {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                if (!is_Marked && controller.getFieldsMarked() < controller.mines) {
                    is_Marked = true;
                    button.setText("🚩");
                    controller.setFieldsMarked(controller.getFieldsMarked() + 1);
                } else if (is_Marked) {
                    is_Marked = false;
                    button.setText("");
                    controller.setFieldsMarked(controller.getFieldsMarked() - 1);
                }
                controller.checkGameStatus();
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY && !is_Marked) {
                reveal();
            }
        }
    }

    /**
     * Description for the reveal method:
     * <p>
     * In this Methode, it is checked if the Field that was clicked is a mine or not.
     * If it is a mine, the timer is stopped, and the mine_Label is shown, als well as showing the gameOverScreen.
     * If it is not, the info_Label is shown, and the game goes on.
     */
    public void reveal() {
        if (!is_Revealed && !is_Marked) {
            is_Revealed = true;
            button.setVisible(false);
            controller.checkGameStatus();

            if (isMine()) {
                mine_Label.setVisible(true);
                controller.revealAllFields();
                controller.stopTimer();
                controller.showGameOverScreen(false);
            } else {
                info_Label.setVisible(true);
                if (getMines_Nearby() == 0) {
                    controller.revealFields(COL, ROW);
                }
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

    /**
     * Description for the setMines_Nearby method:
     * <p>
     * In this Methode, the info_Label gets the value of the mines nearby.
     * Also, if, 0 mines are nearby, the info_Label is set to be invisible, for better looks.
     */
    public void setMines_Nearby(int mineCount) {
        this.mines_Nearby = mineCount;

        if (!isMine()) {
            mine_Label.setVisible(false);
            info_Label.setVisible(true);
            info_Label.setText(Integer.toString(mines_Nearby));
            if (mines_Nearby == 0) {
                info_Label.setStyle("-fx-text-fill: rgba(0, 0, 0, 0)");
            }
        }
    }

    public int getMines_Nearby() {
        return mines_Nearby;
    }

    public boolean isRevealed() {
        return is_Revealed;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void setCoordinates(int col, int row) {
        this.COL = col;
        this.ROW = row;
    }
}