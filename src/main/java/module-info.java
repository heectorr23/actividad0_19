module com.empresa.actividad0_19 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens com.empresa.actividad0_19 to javafx.fxml;
    exports com.empresa.actividad0_19;
}