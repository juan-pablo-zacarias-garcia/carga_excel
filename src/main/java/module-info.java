module com.mycompany.carga_excel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.base;
    
    opens com.mycompany.carga_excel to javafx.fxml;
    exports com.mycompany.carga_excel;
}
