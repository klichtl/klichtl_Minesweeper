package htl.steyr.klichtl_minesweeper;

public class DifficultySettings {

    private int rows;
    private int cols;
    private int mines;

    public DifficultySettings(int rows, int cols, int mines) {
        setRows(rows);
        setCols(cols);
        setMines(mines);
    }

    public int getMines() {
        return mines;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }
}