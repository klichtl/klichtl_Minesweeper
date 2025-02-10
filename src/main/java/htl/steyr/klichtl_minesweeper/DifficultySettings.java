package htl.steyr.klichtl_minesweeper;

public class DifficultySettings {

    public int ROWS;
    public int COLS;
    public int MINES;

    public DifficultySettings(int ROWS, int COLS, int MINES) {
        setROWS(ROWS);
        setCOLS(COLS);
        setMINES(MINES);
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

    public void setROWS(int ROWS) {
        this.ROWS = ROWS;
    }

    public void setCOLS(int COLS) {
        this.COLS = COLS;
    }

    public void setMINES(int MINES) {
        this.MINES = MINES;
    }
}