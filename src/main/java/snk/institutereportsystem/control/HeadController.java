package snk.institutereportsystem.control;

import snk.institutereportsystem.entity.JDBCRepository;
import snk.institutereportsystem.entity.Role;
import snk.institutereportsystem.entity.Theme;
import snk.institutereportsystem.entity.User;

import java.util.List;

public class HeadController {
    private JDBCRepository repository = JDBCRepository.getInstance();


    public Iterable<User> getEmployers() {
        return repository.getUsersByRole(Role.EMPLOYEE);
    }

    public Iterable<Theme> getThemes(User user) {
        return repository.getThemesByUser(user);
    }

    public List<Theme> getFreeThemes() {
        return repository.getFreeThemes();
    }

    public void assignTheme(User selectedUser, Theme selectedTheme) {
        repository.assignTheme(selectedUser.getId(),selectedTheme.getId());
    }
}
