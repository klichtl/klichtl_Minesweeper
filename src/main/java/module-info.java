module htl.steyr.klichtl_minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens htl.steyr.klichtl_minesweeper to javafx.fxml;
    exports htl.steyr.klichtl_minesweeper;
}