package snk.institutereportsystem.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import snk.institutereportsystem.HelloApplication;
import snk.institutereportsystem.entity.User;

import java.io.IOException;
import java.util.Objects;

public class EmployeeBoundary {

    @FXML
    private Text fullName;

    @FXML
    private Button logOut;


    private static final User currentUser = AuthorizeBoundary.currentUser;

    @FXML
    private void initialize() {
        fullName.setText(AuthorizeBoundary.fullName);
    }

    @FXML
    private void handleLogOut() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/authorize.fxml")));
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Аутендификация");
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }
}
