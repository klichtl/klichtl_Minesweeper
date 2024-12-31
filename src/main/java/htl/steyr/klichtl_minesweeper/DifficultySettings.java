package htl.steyr.klichtl_minesweeper;

public class DifficultySettings {

    public String path_To_Fxml;
    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    public DifficultySettings(String path_To_Fxml, int ROWS, int COLS, int MINES) {
        this.path_To_Fxml = path_To_Fxml;
        this.ROWS = ROWS;
        this.COLS = COLS;
        this.MINES = MINES;
    }

    public int getMINES() {
        return MINES;
    }

    public int getCOLS() {
        return COLS;
    }

    public int getROWS() {
        return ROWS;
    }

    public String getPath_To_Fxml() {
        return path_To_Fxml;
    }
}
