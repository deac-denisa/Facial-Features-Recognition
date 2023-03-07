module com.example.facialrecognition {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;
    requires java.desktop;


    opens com.example.facialrecognition to javafx.fxml;
    exports com.example.facialrecognition;
}