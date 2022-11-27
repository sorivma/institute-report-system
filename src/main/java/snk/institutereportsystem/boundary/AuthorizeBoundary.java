package snk.institutereportsystem.boundary;

import snk.institutereportsystem.entity.User;
import snk.institutereportsystem.exceptions.IllegalRoleException;
import snk.institutereportsystem.exceptions.NoSuchUserException;
import snk.institutereportsystem.exceptions.WrongPasswordException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import snk.institutereportsystem.HelloApplication;
import snk.institutereportsystem.control.AuthorizeController;


import java.io.IOException;
import java.util.Objects;

/**
 * Класс, репрезентирующий интерфейс взаимодействия с пользователем,
 * содержит логику обработки событий пользовательского интерфейса
 * Содержит поля <b>ERROR_MSG_WRONG</b> <b>ERROR_MSG_INVALID</b>
 *
 * @author Кристина Ларионова
 * @version 1.0
 */
public class AuthorizeBoundary {
    /**
     * Поле содержашее сообщение ошибки некорректности пароля
     */
    private static final String ERROR_MSG_WRONG = "Неправильный логин/пароль";
    /**
     * Поле содержащее сообщение ошибки некорректности логина
     */
    private static final String ERROR_MSG_INVALID = "Некоректный логин";
    /**
     * Поле контроллера авторизации
     */
    private final AuthorizeController authorizeController = new AuthorizeController();
    /**
     * Поле ввода логина
     */
    @FXML
    private TextField idField;
    /**
     * Поле ввода пароля
     */
    @FXML
    private PasswordField passwordField;
    /**
     * Поле кнопки
     */
    @FXML
    private Button logIn;
    /**
     * Поле сообщения об ошибке
     */
    @FXML
    private Text errorMSG;
    /**
     * Метод авторизации пользователя в системе, вызывает контроллер, который возвращает URL следующей сцены,
     * необходимой пользавателю
     */

    public static String fullName;
    public static User currentUser;
    @FXML
    private void handleLogIn() {
        try {
            String scene_url = authorizeController.authorize(Long.valueOf(idField.getText()), passwordField.getText());
            fullName = authorizeController.getFullName(Long.valueOf(idField.getText()));
            currentUser = authorizeController.getUser(Long.valueOf(idField.getText()));
            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(scene_url)));
            Stage stage = (Stage) logIn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (WrongPasswordException e) {
            errorMSG.setText(ERROR_MSG_WRONG);

        } catch (NoSuchUserException | NumberFormatException e) {
            errorMSG.setText(ERROR_MSG_INVALID);

        } catch (IllegalRoleException e) {
            errorMSG.setText("Обратитесь к администратору");

        } catch (IOException e) {
            System.out.println("Files corrupted");
        }
    }

}
