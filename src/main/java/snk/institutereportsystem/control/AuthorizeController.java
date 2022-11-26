package snk.institutereportsystem.control;


import snk.institutereportsystem.exceptions.IllegalRoleException;
import snk.institutereportsystem.exceptions.NoSuchUserException;
import snk.institutereportsystem.exceptions.WrongPasswordException;
import snk.institutereportsystem.entity.JDBCRepository;
import snk.institutereportsystem.entity.Role;
import snk.institutereportsystem.entity.User;

/**
 * Класс контроллера авторизации с полем <b>repository</b>
 * Контроллер, отвечающий за логику авторизации в системе, работает напрямую с базой данных
 * @author Арсений Дубровский
 */
public class AuthorizeController {
    /**
     * Поле экземпляра класса взаимодействия с репозиторием
     */
    JDBCRepository repository = JDBCRepository.getInstance();

    /**
     *
     * @param id - id пользователя
     * @param password - пароль пользователя
     * @return - возвращает URL необходимой сцены конкретного пользователя
     * @throws NoSuchUserException - несуществование пользователя
     * @throws WrongPasswordException - некорректный пароль пользователя
     * @throws IllegalRoleException - некорректная роль пользователя в базе данных
     */
    public String authorize(Long id, String password) throws NoSuchUserException,
            WrongPasswordException, IllegalRoleException {

        User user = repository.getUser(id);

        if (user == null) {
            throw new NoSuchUserException();
        }

        if (!user.getPassword().equals(password)) {
            throw new WrongPasswordException();
        }

        Role role = user.getRole();
        String scene_url;
        switch (role) {
            case ADMIN -> scene_url = "admin.fxml";
            case HEAD -> scene_url = "head.fxml";
            case EMPLOYEE -> scene_url = "employee.fxml";
            default -> {
                throw new IllegalRoleException();
            }
        }

        return scene_url;
    }
}
