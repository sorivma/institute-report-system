package snk.institutereportsystem.control;

import snk.institutereportsystem.entity.JDBCRepository;
import snk.institutereportsystem.entity.Role;
import snk.institutereportsystem.entity.Theme;
import snk.institutereportsystem.entity.User;
import snk.institutereportsystem.exceptions.*;

import java.util.List;

public class AdminController {
    private final JDBCRepository repository = JDBCRepository.getInstance();

    public List<User> getUsers() {
        return repository.getAllUsers();
    }

    public void addUser(String name, String surname, String patronymic, String password, Role role) throws
            NoNameException, NoSurnameException, NoPasswordException, NoPatronymicException, NoRoleException {
        if (name.equals("")){
            throw new NoNameException();
        }
        if (surname.equals("")){
            throw new NoSurnameException();
        }
        if (patronymic.equals("")){
            throw new NoPatronymicException();
        }
        if (password.equals("")){
            throw new NoPasswordException();
        }
        if (role == null){
            throw new NoRoleException();
        }
        repository.addUser(new User(
                null,
                name,
                surname,
                patronymic,
                password,
                role
        ));
    }

    public Iterable<Theme> getThemes() {
        return repository.getThemes();
    }

    public User getUser(Long id){
        return repository.getUser(id);
    }

    public Theme getTheme(Long id){
        return repository.getTheme(id);
    }

    public void addTheme(String name) {
        repository.addTheme(new Theme(null, name));
    }

    public void deleteTheme(Long id){
        repository.deleteTheme(id);
    }

    public void deleteUser(Long id){
        repository.deleteUser(id);
    }

    public void updateUser(User user) throws NoPasswordException, NoPatronymicException, NoSurnameException, NoNameException {
        if (user.getName().equals("")){
            throw new NoNameException();
        }
        if (user.getSurname().equals("")){
            throw new NoSurnameException();
        }
        if (user.getPatronymic().equals("")){
            throw new NoPatronymicException();
        }
        if (user.getPassword().equals("")){
            throw new NoPasswordException();
        }
        repository.updateUser(user);
    }

    public void updateTheme(Theme selectedTheme) {
        repository.updateTheme(selectedTheme);
    }
}
