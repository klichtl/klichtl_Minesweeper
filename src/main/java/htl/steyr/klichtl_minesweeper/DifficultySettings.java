package htl.steyr.klichtl_minesweeper;

public class DifficultySettings {


    public Integer ROWS;
    public Integer COLS;
    public Integer MINES;

    public DifficultySettings( int ROWS, int COLS, int MINES) {
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

}
