module com.mycompany.tictactoeclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.media;

    opens com.mycompany.tictactoeclient to javafx.fxml;
    exports com.mycompany.tictactoeclient;
    requires javafx.mediaEmpty;
    requires com.mycompany.tictactoeshared;
}
