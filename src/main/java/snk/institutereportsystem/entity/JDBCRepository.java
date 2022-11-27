package snk.institutereportsystem.entity;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Singleton класс взаимодействия с базой данных
 *
 * @author Арсений Дубровский
 * @version 2.1
 */
public final class JDBCRepository {
    /**
     * Поле подключения к базе данных
     */
    private Connection connection;
    /**
     * Поле самого класса
     */
    private static JDBCRepository instance = null;

    /**
     * Метод для возвращения instance`а экземпляра класса
     *
     * @return - экземпляр класса JDBCRepository
     */
    public static synchronized JDBCRepository getInstance() {
        if (instance == null) {
            try {
                instance = new JDBCRepository();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

        return instance;
    }

    /**
     * SQL запрос для создания таблицы пользователей
     */
    private final static String CREATE_USER_TABLE = """
            CREATE TABLE IF NOT EXISTS users(
            userId INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT,
            surName TEXT,
            patronymic TEXT,
            password TEXT UNIQUE,
            role TEXT
            )
            """;
    /**
     * SQL запрос для создания таблицы тем
     */
    private final static String CREATE_THEME_TABLE = """
            CREATE TABLE IF NOT EXISTS themes(
            themeId INTEGER PRIMARY KEY AUTOINCREMENT,
            themeName TEXT UNIQUE
            )
            """;

    /**
     * SQL запрос для создания таблицы отчётов
     */
    private final static String CREATE_REPORT_TABLE = """
            CREATE TABLE IF NOT EXISTS reports(
            reportId INTEGER PRIMARY KEY AUTOINCREMENT,
            userId INTEGER,
            reportText TEXT,
            status TEXT,
            themeId INTEGER,
            FOREIGN KEY (userId) REFERENCES users(userId),
            FOREIGN KEY (themeId) REFERENCES themes(themeId)
            )
            """;


    /**
     * Конструктор класса, открывающий соединение с базой данных или создающий её.
     *
     * @throws SQLException - SQL ошибка
     */
    private JDBCRepository() throws SQLException {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setEncoding("UTF_8");
        String url = "jdbc:sqlite:institute.db";
        setConnection(DriverManager.getConnection(url));
        setConnection(connection);
        connection.createStatement().execute(CREATE_USER_TABLE);
        connection.createStatement().execute(CREATE_THEME_TABLE);
        connection.createStatement().execute(CREATE_REPORT_TABLE);
    }

    /**
     * Метод добавления пользователя в базу данных
     *
     * @param user - Пользователь
     */
    public void addUser(User user) {
        try {
            String INSERT_USER = """
                    INSERT INTO users(userName, surName, patronymic, password, role)
                    VALUES (?,?,?,?,?)
                    """;

            PreparedStatement addUserStatement = connection.prepareStatement(INSERT_USER);
            addUserStatement.setString(1, user.getName());
            addUserStatement.setString(2, user.getSurname());
            addUserStatement.setString(3, user.getPatronymic());
            addUserStatement.setString(4, user.getPassword());
            addUserStatement.setString(5, user.getRole().toString());
            addUserStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод получения пользователя из базы данных
     *
     * @param id - id Пользователя
     * @return - экземпляр класса User или null при отсутсвии пользователя
     */
    public User getUser(Long id) {
        try {
            String SELECT_USER_BY_ID = """
                    SELECT * FROM users WHERE userId = ?
                    """;
            PreparedStatement getUserStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            getUserStatement.setLong(1, id);
            ResultSet resultSet = getUserStatement.executeQuery();
            return new User(
                    resultSet.getLong("userId"),
                    resultSet.getString("userName"),
                    resultSet.getString("surName"),
                    resultSet.getString("patronymic"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role"))
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Метод получения всех пользователей из базы данных
     *
     * @return - экземпляр Iterable, содержащий всех пользователей
     */
    public List<User> getAllUsers() {
        try {
            String SELECT_USERS = """
                    SELECT * FROM USERS
                    """;
            PreparedStatement getUsersStatement = connection.prepareStatement(SELECT_USERS);
            ResultSet resultSet = getUsersStatement.executeQuery();
            List<User> userSet = new ArrayList<>();
            while (resultSet.next()) {
                userSet.add(
                        new User(
                                resultSet.getLong("userId"),
                                resultSet.getString("userName"),
                                resultSet.getString("surName"),
                                resultSet.getString("patronymic"),
                                resultSet.getString("password"),
                                Role.valueOf(resultSet.getString("role"))
                        )
                );
            }

            return userSet;

        } catch (SQLException e) {
            System.out.println("Внутренняя ошибка бд");
            return null;
        }
    }

    public Iterable<User> getUsersByRole(Role role) {
        try {
            String SELECT_STATEMENT = """
                    SELECT * FROM users WHERE role = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT);
            statement.setString(1, role.toString());
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        new User(
                                resultSet.getLong("userId"),
                                resultSet.getString("userName"),
                                resultSet.getString("surName"),
                                resultSet.getString("patronymic"),
                                resultSet.getString("password"),
                                Role.valueOf(resultSet.getString("role"))
                        )
                );
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void updateUser(User user) {
        System.out.println(user);
        try {
            String UPDATE_STATEMENT = """
                    UPDATE users SET
                    userName = ?,
                    surName = ?,
                    patronymic = ?,
                    password = ?,
                    role = ?
                    WHERE userId = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPatronymic());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getRole().toString());
            statement.setLong(6, user.getId());
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        String DELETE_STATEMENT = """
                DELETE FROM users WHERE userId = ?
                """;
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_STATEMENT);
            statement.setLong(1, id);
            statement.execute();

            String DELETE_REPORT_STATEMENT = """
                    DELETE FROM reports WHERE userId = ?
                    """;
            PreparedStatement reportStatement = connection.prepareStatement(DELETE_REPORT_STATEMENT);
            reportStatement.setLong(1, id);
            reportStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод добавления тем в базу данных
     *
     * @param theme - экземпляр класса Theme
     */
    public void addTheme(Theme theme) {
        try {
            String INSERT_THEME = """
                    INSERT INTO themes(themeName)
                    VALUES (?)
                    """;
            PreparedStatement addThemeStatement = connection.prepareStatement(INSERT_THEME);
            addThemeStatement.setString(1, theme.getName());
            addThemeStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод получения темы из базы данных
     *
     * @param id - id темы
     * @return - экземпляр класса Theme
     */
    public Theme getTheme(Long id) {
        try {
            String SELECT_THEME_BY_ID = """
                    SELECT themeName FROM themes WHERE themeId = ?
                    """;
            PreparedStatement getThemeStatement = connection.prepareStatement(SELECT_THEME_BY_ID);
            getThemeStatement.setLong(1, id);
            ResultSet resultSet = getThemeStatement.executeQuery();
            return new Theme(
                    null,
                    resultSet.getString("themeName")
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Iterable<Theme> getThemes() {
        try {
            String SELECT_THEMES = """
                    SELECT * FROM themes
                    """;
            PreparedStatement getThemeStatement = connection.prepareStatement(SELECT_THEMES);
            ResultSet resultSet = getThemeStatement.executeQuery();
            Set<Theme> themeSet = new HashSet<>();
            while (resultSet.next()) {
                themeSet.add(
                        new Theme(
                                resultSet.getLong("themeId"),
                                resultSet.getString("themeName")
                        )
                );
            }
            return themeSet;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteTheme(Long id) {
        try {
            String DELETE_STATEMENT = """
                    DELETE FROM themes where themeId = ?
                    """;
            PreparedStatement deleteThemeStatement = connection.prepareStatement(DELETE_STATEMENT);
            deleteThemeStatement.setLong(1, id);
            deleteThemeStatement.execute();

            String DELETE_REPORTS_THEME = """
                    DELETE FROM reports WHERE themeId = ?
                    """;
            PreparedStatement deleteReportStatement = connection.prepareStatement(DELETE_REPORTS_THEME);
            deleteReportStatement.setLong(1, id);
            deleteReportStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод назначения темы пользователю
     *
     * @param userId  - id пользователя
     * @param themeId - id темы
     */
    public void assignTheme(Long userId, Long themeId) {
        try {
            String ADD_REPORT_TO_USER = """
                    INSERT INTO reports(userID,status,themeId)
                    VALUES (?,?,?)
                    """;
            PreparedStatement assignStatement = connection.prepareStatement(ADD_REPORT_TO_USER);
            assignStatement.setLong(1, userId);
            assignStatement.setString(2, Status.InWORK.toString());
            assignStatement.setLong(3, themeId);
            assignStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Изменить статус отчёта
     *
     * @param status   - статус отчёта
     * @param reportId - id отчёта
     */
    public void changeReportStatus(Status status, Long reportId) {
        try {
            String CHANGE_STATUS = """
                    UPDATE reports SET status = ? WHERE reportId = ?
                    """;
            PreparedStatement changeStatusStatement = connection.prepareStatement(CHANGE_STATUS);
            changeStatusStatement.setString(1, status.toString());
            changeStatusStatement.setLong(2, reportId);
            changeStatusStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Закрыть соединение базы данных
     *
     * @throws SQLException - SQL ошибка
     */
    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public void updateTheme(Theme selectedTheme) {
        try {
            String UPDATE_STATEMENT = """
                    UPDATE themes SET themeName = ? WHERE themeId = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
            statement.setString(1, selectedTheme.getName());
            statement.setLong(2, selectedTheme.getId());
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Iterable<Theme> getThemesByUser(User user) {
        try {
            String GET_STATEMENT = """
                    SELECT * FROM themes WHERE themeId IN (SELECT themeId FROM reports WHERE userId = ?)
                    """;

            PreparedStatement statement = connection.prepareStatement(GET_STATEMENT);
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            List<Theme> themes = new ArrayList<>();
            while (resultSet.next()) {
                themes.add(
                        new Theme(
                                resultSet.getLong("themeId"),
                                resultSet.getString("themeName")
                        )
                );
            }
            return themes;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Theme> getFreeThemes() {
        try {
            String GET_STATEMENT = """
                    SELECT * FROM themes WHERE themeId NOT IN (SELECT themeId FROM reports)
                    """;
            PreparedStatement statement = connection.prepareStatement(GET_STATEMENT);
            ResultSet resultSet = statement.executeQuery();
            List<Theme> themes = new ArrayList<>();
            while (resultSet.next()) {
                themes.add(
                        new Theme(
                                resultSet.getLong("themeId"),
                                resultSet.getString("themeName")
                        )
                );
            }
            return themes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Report> getReports(Long id) {
        try{
            String GET_STATEMENT = """
                    SELECT * FROM reports WHERE userId = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(GET_STATEMENT);
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();
            List<Report> reports = new ArrayList<>();
            while (resultSet.next()){
                reports.add(
                        new Report(
                                resultSet.getLong("reportId"),
                                resultSet.getLong("userId"),
                                resultSet.getLong("themeId"),
                                resultSet.getString("reportText"),
                                Status.valueOf(resultSet.getString("status"))
                        )
                );
            }
            return reports;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void setReportText(long reportId, String text) {
        try{
            System.out.println(reportId);
            System.out.println(text);
            String UPDATE_STATEMENT = """
                    UPDATE reports SET reportText = ? WHERE reportId = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
            statement.setString(1, text);
            statement.setLong(2, reportId);
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Report> getUncheckedReports() {
        try{
            String GET_STATEMENT = """
                    SELECT * FROM reports WHERE status = 'UNCHECKED'
                    """;
            PreparedStatement statement = connection.prepareStatement(GET_STATEMENT);
            ResultSet resultSet = statement.executeQuery();
            List<Report> reports = new ArrayList<>();
            while (resultSet.next()){
                reports.add(
                        new Report(
                                resultSet.getLong("reportId"),
                                resultSet.getLong("userId"),
                                resultSet.getLong("themeId"),
                                resultSet.getString("reportText"),
                                Status.valueOf(resultSet.getString("status"))
                        )
                );
            }
            return reports;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
