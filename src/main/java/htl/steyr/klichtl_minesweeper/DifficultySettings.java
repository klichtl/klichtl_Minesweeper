package htl.steyr.klichtl_minesweeper;

public class DifficultySettings {

    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    public DifficultySettings( int ROWS, int COLS, int MINES) {
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

    public void setROWS(Integer ROWS) {
        this.ROWS = ROWS;
    }

    public void setCOLS(Integer COLS) {
        this.COLS = COLS;
    }

    public void setMINES(Integer MINES) {
        this.MINES = MINES;
    }
}