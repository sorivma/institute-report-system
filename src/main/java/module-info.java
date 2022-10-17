module snk.institutereportsystem {
    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
            requires validatorfx;
                    
    opens snk.institutereportsystem to javafx.fxml;
    exports snk.institutereportsystem;
}