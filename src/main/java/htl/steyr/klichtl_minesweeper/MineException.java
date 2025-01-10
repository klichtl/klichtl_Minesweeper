package htl.steyr.klichtl_minesweeper;

public class MineException extends Exception {

    public MineException() {
        super("You have clicked on a Mine :(");
    }

}