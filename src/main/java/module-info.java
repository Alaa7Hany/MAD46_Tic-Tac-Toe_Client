module com.mycompany.tictactoeclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.tictactoeclient to javafx.fxml;
    exports com.mycompany.tictactoeclient;
}
