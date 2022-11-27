package snk.institutereportsystem.boundary;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import snk.institutereportsystem.HelloApplication;
import snk.institutereportsystem.control.HeadController;
import snk.institutereportsystem.entity.Report;
import snk.institutereportsystem.entity.Theme;
import snk.institutereportsystem.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HeadBoundary {
    HeadController headController = new HeadController();
    @FXML
    private Button logOutBtn;

    @FXML
    private Button backBtn;

    @FXML
    private VBox employeeList;

    @FXML
    private Text fullName;

    @FXML
    private VBox assignedThemes;

    @FXML
    private ComboBox<Theme> assignBtn;

    @FXML
    private VBox reportList;

    @FXML
    private Text theme;

    @FXML
    private Text employeeFullName;

    @FXML
    private TextArea reportBody;


    private static User selectedUser;
    private static Theme selectedTheme;
    private static Report selectedReport;


    @FXML
    private void initialize() {
        if (employeeList != null) {
            if (selectedUser != null){
                initializeEmployersThemes(selectedUser);
            }
            employeeList.getChildren().removeAll(employeeList.getChildren());
            Iterable<User> employers = headController.getEmployers();
            for (User user : employers) {
                Button button = new Button(
                        user.getSurname() + " " + user.getName() + " " + user.getPatronymic()
                );
                button.setMinWidth(230);
                button.setMaxWidth(500);
                button.setOnAction(
                        actionEvent -> {
                            selectedUser = user;
                            fullName.setText(
                                    selectedUser.getSurname() + " "
                                            + selectedUser.getName() + " "
                                            + selectedUser.getPatronymic());
                            initializeEmployersThemes(user);
                        }
                );
                employeeList.getChildren().add(button);
            }
            assignBtn.setItems(FXCollections.observableList(headController.getFreeThemes()));
        }

        if (reportList != null){
            List<Report> unCheckedReports = headController.getUncheckedReports();
            for (Report report : unCheckedReports){
                User employee = headController.getUser(report.getUserId());
                Button button = new Button(headController.getThemeName(report.getThemeId())
                + "\n" + "Автор: " + employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic());
                button.setOnAction(actionEvent -> {
                    selectedReport = report;
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                                .class
                                .getResource("fxml/head/check_report.fxml")));
                        Stage stage = (Stage) reportList.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Проверка отчёта");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());

                    }
                });
                reportList.getChildren().add(button);
            }
        }

        if (reportBody != null){
            User user = headController.getUser(selectedReport.getUserId());
            employeeFullName.setText(user.getSurname() + " " + user.getName() + user.getPatronymic());
            theme.setText(headController.getThemeName(selectedReport.getThemeId()));
            reportBody.setText(selectedReport.getText());
        }

    }

    @FXML
    private void logOutHandle() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/authorize.fxml")));
            Stage stage = (Stage) logOutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Аутендификация");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void themeAssiHandle() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/head/theme_assignment.fxml")));
            Stage stage = (Stage) logOutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Назначение тем");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void reportCheckHandle() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/head/reports_check.fxml")));
            Stage stage = (Stage) logOutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Проверка отчётов");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void backToHeadPage() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/head/head.fxml")));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Меню руководителя");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initializeEmployersThemes(User user) {
        Iterable<Theme> themes = headController.getThemes(user);
        assignedThemes.getChildren().removeAll(assignedThemes.getChildren());
        for (Theme theme : themes) {
            Text themeText = new Text(theme.getName());
            themeText.prefWidth(300);
            assignedThemes.getChildren().add(themeText);
        }

    }

    @FXML
    private void assignThemeHandle() {
        selectedTheme = assignBtn.getValue();
    }

    @FXML
    private void addThemeHndl() {
        headController.assignTheme(selectedUser, selectedTheme);
        initialize();
    }

    @FXML
    private void backToReportsHndl(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/head/reports_check.fxml")));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Проверка отчётов");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void approveHndl(){
        headController.approveReport(selectedReport);
        backToReportsHndl();
    }

    @FXML
    private void declineHndl(){
        headController.declineReport(selectedReport);
        backToReportsHndl();
    }
}
