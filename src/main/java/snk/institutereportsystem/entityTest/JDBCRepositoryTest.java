package snk.institutereportsystem.entityTest;

import snk.institutereportsystem.entity.JDBCRepository;
import snk.institutereportsystem.entity.Role;
import snk.institutereportsystem.entity.Theme;
import snk.institutereportsystem.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCRepositoryTest {
    public static void main(String[] args) {
        User testUser1 = new User(
                null,
                "тест1",
                "тест1",
                "тест1",
                "тест1",
                Role.EMPLOYEE
        );
        User testUser2 = new User(
                null,
                "test2",
                "test2",
                "test2",
                "test2",
                Role.EMPLOYEE
        );
        User testUser3 = new User(
                null,
                "test3",
                "test3",
                "test3",
                "test3",
                Role.EMPLOYEE
        );
        Theme testTheme1 = new Theme(
                null,
                "Котики"
        );
        Theme testTheme2 = new Theme(
                null,
                "Протоны"
        );
        Theme testTheme3 = new Theme(
                null,
                "Электроны"
        );

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:institute.db");
            JDBCRepository repository = new JDBCRepository(connection);
            repository.addUser(testUser1);
            repository.addUser(testUser2);
            repository.addUser(testUser3);
//            System.out.println(repository.getUser(0L));
//            System.out.println(repository.getUser(1L));
//            System.out.println(repository.getUser(2L));
//            repository.addTheme(testTheme1);
//            repository.addTheme(testTheme2);
//            repository.addTheme(testTheme3);
//            System.out.println(repository.getTheme(0L));
//            System.out.println(repository.getTheme(1L));
//            System.out.println(repository.getTheme(2L));
//            repository.assignTheme(4L, 0L);
//            repository.assignTheme(4L, 1L);
//            repository.assignTheme(5L, 2L);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
