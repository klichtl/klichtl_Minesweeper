package htl.steyr.klichtl_minesweeper;

public class BombException extends Exception {

    public BombException() {
        super("You have clicked on a Mine :(");
    }

}