package snk.institutereportsystem.boundary;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import snk.institutereportsystem.HelloApplication;
import snk.institutereportsystem.control.AdminController;
import snk.institutereportsystem.entity.Role;
import snk.institutereportsystem.entity.Theme;
import snk.institutereportsystem.entity.User;
import snk.institutereportsystem.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminBoundary {
    private final AdminController adminController = new AdminController();
    @FXML
    private TextArea themeName;

    @FXML
    private Button backBtn;

    @FXML
    private VBox themeList;

    @FXML
    private VBox usersList;
    @FXML
    private Button logOut;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField patronymic;

    @FXML
    private TextField password;

    @FXML
    private ComboBox<String> roleBtn;

    @FXML
    private Text er1;

    @FXML
    private Text er2;

    @FXML
    private Text er3;

    @FXML
    private Text er4;

    @FXML
    private Text er5;

    @FXML
    private TextField themeDeleteField;

    @FXML
    private TextField userDeleteField;

    @FXML
    private Text themeDeleteError;

    @FXML
    private Text userDeleteError;

    @FXML
    private Text fullNameField;

    private String comboboxRole = "Employee";

    @FXML
    private TextField changedName;
    @FXML
    private TextField changedSur;

    @FXML
    private TextField changedPat;

    @FXML
    private TextField changedPas;

    @FXML
    private ComboBox<String> changedRole;

    @FXML
    private AnchorPane changeAnchor;

    @FXML
    private Text changeErr;

    @FXML
    private Button cBackBtn;

    @FXML
    private AnchorPane themeChangeAnckor;

    @FXML
    private TextArea textChangeThemeField;

    @FXML
    private Button themecBackBtn;
    private static User selectedUser;
    private static Theme selectedTheme;

    @FXML
    private void initialize() {
        if (fullNameField != null) {
            fullNameField.setText(AuthorizeBoundary.fullName);
        }

        if (roleBtn != null) {
            List<String> rolesList = new ArrayList<>();
            roleBtn.setItems(FXCollections.observableList(rolesList));
            rolesList.add("Admin");
            rolesList.add("Head");
            rolesList.add("Employee");
            roleBtn.setItems(FXCollections.observableList(rolesList));
        }
        if (usersList != null) {
            usersList.getChildren().removeAll(usersList.getChildren());
            List<User> users = adminController.getUsers();
            for (User user : users) {
                Role role = user.getRole();
                String role_name;
                switch (role) {
                    case ADMIN -> role_name = "Администратор";
                    case EMPLOYEE -> role_name = "Сотрудник";
                    case HEAD -> role_name = "Руководитель";
                    default -> role_name = "Роль не определена";
                }
                Button button = new Button(user.getId() + " " +
                        user.getName() + " "
                        + user.getSurname() + " "
                        + user.getPatronymic() + " " + role_name);
                button.setMinWidth(230);
                button.setMaxWidth(500);
                button.setOnAction(actionEvent -> {
                    selectedUser = user;
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.
                                class
                                .getResource("fxml/admin/change.fxml")));
                        Stage stage = (Stage) button.getScene().getWindow();
                        stage.setScene(new Scene(root));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                usersList.getChildren().add(button);
            }
        }

        if (themeList != null) {
            themeList.getChildren().removeAll(themeList.getChildren());
            Iterable<Theme> themes = adminController.getThemes();
            for (Theme theme : themes) {
                Button button = new Button(theme.getId() + " " + theme.getName());
                button.setOnAction(actionEvent -> {
                    selectedTheme = theme;
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.
                                class
                                .getResource("fxml/admin/changeTheme.fxml")));
                        Stage stage = (Stage) button.getScene().getWindow();
                        stage.setScene(new Scene(root));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                button.setPrefWidth(230);
                themeList.getChildren().add(button);
            }
        }
        if (roleBtn != null) {
            roleBtn.setValue("Employee");
        }

        if (changeAnchor != null) {
            changedName.setText(selectedUser.getName());
            changedSur.setText(selectedUser.getSurname());
            changedPat.setText(selectedUser.getPatronymic());
            changedPas.setText(selectedUser.getPassword());
            List<String> rolesList = new ArrayList<>();
            rolesList.add("ADMIN");
            rolesList.add("HEAD");
            rolesList.add("EMPLOYEE");
            changedRole.setItems(FXCollections.observableList(rolesList));
            Role role = selectedUser.getRole();
            String role_name;
            switch (role) {
                case ADMIN -> role_name = "ADMIN";
                case EMPLOYEE -> role_name = "EMPLOYEE";
                case HEAD -> role_name = "HEAD";
                default -> role_name = "не определена";
            }
            changedRole.setValue(role_name);
        }

        if (themeChangeAnckor != null){
            textChangeThemeField.setText(selectedTheme.getName());
        }
    }

    @FXML
    private void handleEmp() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/admin/users.fxml")));
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Управление сотрудниками");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleThe() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/admin/theme.fxml")));
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Управление темами");
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/admin/admin.fxml")));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("НИИ");
        } catch (IOException e) {
            System.out.println("Files corrupted");
        }
    }

    @FXML
    private void addThemeHndl() {
        adminController.addTheme(themeName.getText());
        initialize();
    }

    @FXML
    private void roleBtnHandle() {
        this.comboboxRole = roleBtn.getValue();
        System.out.println(comboboxRole);
    }

    @FXML
    private void addUserHndl() {
        Role role;
        System.out.println(comboboxRole);
        switch (comboboxRole) {
            case "Admin" -> role = Role.ADMIN;
            case "Head" -> role = Role.HEAD;
            default -> role = Role.EMPLOYEE;
        }
        try {
            adminController.addUser(
                    name.getText(),
                    surname.getText(),
                    patronymic.getText(),
                    password.getText(),
                    role
            );
            initialize();
        } catch (NoNameException e) {
            er1.setText("*");
        } catch (NoSurnameException e) {
            er2.setText("*");
        } catch (NoPasswordException e) {
            er3.setText("*");
        } catch (NoPatronymicException e) {
            er4.setText("*");
        } catch (NoRoleException e) {
            er5.setText("*");
        }
    }

    @FXML
    private void saveBtnHndl(){
        selectedUser.setName(changedName.getText());
        selectedUser.setSurname(changedSur.getText());
        selectedUser.setPatronymic(changedPat.getText());
        selectedUser.setPassword(changedPas.getText());
        selectedUser.setRole(Role.valueOf(changedRole.getValue()));
        try {
            adminController.updateUser(selectedUser);

        } catch (Exception e){
            changeErr.setText("Карточка некорректна");
        }
    }

    @FXML
    private void delBtnHndl(){
        adminController.deleteUser(selectedUser.getId());
        changeBackBtnHndl();
    }

    @FXML
    private void changeBackBtnHndl(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/admin/users.fxml")));
            Stage stage = (Stage) cBackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Пользователи");
        } catch (IOException e) {
            System.out.println("Files corrupted");
        }
    }


    @FXML
    private void themeChangeBackBtnHndl(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication
                    .class
                    .getResource("fxml/admin/theme.fxml")));
            Stage stage = (Stage) themeChangeAnckor.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Темы");
        } catch (IOException e) {
            System.out.println("Files corrupted");
        }
    }
    @FXML
    private void changeThemeSaveBtnHndl(){
        selectedTheme.setName(textChangeThemeField.getText());
        adminController.updateTheme(selectedTheme);
        themeChangeBackBtnHndl();
    }

    @FXML
    private void deleteThemeHndl(){
        adminController.deleteTheme(selectedTheme.getId());
        themeChangeBackBtnHndl();
    }

}
