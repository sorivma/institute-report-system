module snk.institutereportsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires validatorfx;

    opens snk.institutereportsystem to javafx.fxml;
    exports snk.institutereportsystem;
    exports snk.institutereportsystem.boundary;
    opens snk.institutereportsystem.boundary to javafx.fxml;


    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires static lombok;
}