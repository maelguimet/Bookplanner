module bookplanner.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires org.jetbrains.annotations;

    opens bookplanner.application to javafx.fxml;
    exports bookplanner.application;
    exports bookplanner.controllers;
    opens bookplanner.controllers to javafx.fxml;
}