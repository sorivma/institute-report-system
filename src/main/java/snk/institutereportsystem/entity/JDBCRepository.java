package snk.institutereportsystem.entity;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Singleton класс взаимодействия с базой данных
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
     * @return - экземпляр Iterable, содержащий всех пользователей
     */
    public Iterable<User> getAllUsers() {
        try {
            String SELECT_USERS = """
                    SELECT * FROM USERS
                    """;
            PreparedStatement getUsersStatement = connection.prepareStatement(SELECT_USERS);
            ResultSet resultSet = getUsersStatement.executeQuery();
            Set<User> userSet = new HashSet<>();
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
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Метод добавления тем в базу данных
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

    /**
     * Метод назначения темы пользователю
     * @param userId - id пользователя
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
     * @param status - статус отчёта
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
     * @throws SQLException - SQL ошибка
     */
    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}
