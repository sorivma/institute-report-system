package snk.institutereportsystem.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import snk.institutereportsystem.HelloApplication;
import snk.institutereportsystem.control.EmployeeController;
import snk.institutereportsystem.entity.Report;
import snk.institutereportsystem.entity.Status;
import snk.institutereportsystem.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EmployeeBoundary {
    EmployeeController employeeController = new EmployeeController();

    @FXML
    private Text fullName;

    @FXML
    private Button logOut;

    @FXML
    private VBox reportList;

    @FXML
    private Text themeLabel;

    @FXML
    private TextArea reportBody;

    @FXML
    private Button saveReportBtn;


    private static final User currentUser = AuthorizeBoundary.currentUser;
    private static Report selectedReport;

    @FXML
    private void initialize() {
        if (reportList != null) {
            fullName.setText(AuthorizeBoundary.fullName);
            List<Report> reports = employeeController.getReports(currentUser);
            for (Report report : reports){
                String status;
                Status enStatus = report.getStatus();
                switch (enStatus){
                    case InWORK -> status = "Работа в процессе";
                    case APPROVED -> status = "Работа принята";
                    case DECLINED -> status = "Работа отправлена на доработку";
                    case UNCHECKED -> status = "Работа не проверена";
                    default -> status = "Статус неопределен";
                }
                Button button = new Button(employeeController.getReportName(report.getThemeId()) + "\n" + status);
                button.setAlignment(Pos.BASELINE_LEFT);
                button.setPrefWidth(302);
                button.setOnAction(
                        actionEvent -> {
                            selectedReport = report;
                            Parent root;
                            try {
                                root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.
                                        class
                                        .getResource("fxml/employee/report.fxml")));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Stage stage = (Stage) button.getScene().getWindow();
                            stage.setScene(new Scene(root));
                        }
                );
                reportList.getChildren().add(button);
            }
        }

        if (reportBody != null){
            reportBody.setText("");
            if (selectedReport.getText() == null){
                reportBody.setPromptText("Введите текст отчёта");
            } else {
                reportBody.setText(selectedReport.getText());
            }
            themeLabel.setText(employeeController.getReportName(selectedReport.getThemeId()));

            if (selectedReport.getStatus().equals(Status.UNCHECKED) || selectedReport.getStatus().equals(Status.APPROVED)){
                reportBody.setEditable(false);
            }
        }
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

    @FXML
    private void saveReportHndl(){
        selectedReport.setText(reportBody.getText());
        System.out.println(selectedReport);
        employeeController.saveReport(selectedReport);
    }

    @FXML
    private void sendReportHndl(){
        selectedReport.setText(reportBody.getText());
        System.out.println(selectedReport);
        System.out.println(selectedReport.getText());
        employeeController.saveReport(selectedReport);
        employeeController.sendReport(selectedReport);
        initialize();
    }

    @FXML
    private void backHndl(){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.
                    class
                    .getResource("fxml/employee/employee.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) reportBody.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
