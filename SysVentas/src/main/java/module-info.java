module pe.edu.upeu.sysventas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;
    requires jakarta.validation;
    requires pe.edu.upeu.sysventas;

    opens pe.edu.upeu.sysventas to javafx.fxml;
    exports pe.edu.upeu.sysventas;
}