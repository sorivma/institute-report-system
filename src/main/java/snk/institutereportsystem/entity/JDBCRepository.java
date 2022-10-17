package snk.institutereportsystem.entity;

import java.sql.*;

public final class JDBCRepository {
    //    private Connection connection;
    private String url;

    private final static String CREATE_USER_TABLE = """
            CREATE TABLE IF NOT EXISTS users(
            userId INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT,
            surName TEXT,
            patronymic TEXT,
            password TEXT,
            role TEXT
            )
            """;
    private final static String CREATE_THEME_TABLE = """
            CREATE TABLE IF NOT EXISTS themes(
            themeId INTEGER PRIMARY KEY AUTOINCREMENT,
            themeName TEXT
            )
            """;
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


    public JDBCRepository(String url) throws SQLException {
        this.url = url;
        Connection connection = DriverManager.getConnection(url);
        connection.createStatement().execute(CREATE_USER_TABLE);
        connection.createStatement().execute(CREATE_THEME_TABLE);
        connection.createStatement().execute(CREATE_REPORT_TABLE);
        connection.close();
    }

    public void addUser(User user) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url);
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
        addUserStatement.execute();
        connection.close();
    }

    public User getUser(Long id) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url);
        String SELECT_USER_BY_ID = """
                SELECT * FROM users WHERE userId = ?
                """;
        PreparedStatement getUserStatement = connection.prepareStatement(SELECT_USER_BY_ID);
        getUserStatement.setLong(1, id);
        ResultSet resultSet = getUserStatement.executeQuery();

        User user = new User(
                resultSet.getLong("userId"),
                resultSet.getString("userName"),
                resultSet.getString("surName"),
                resultSet.getString("patronymic"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
        connection.close();
        return user;

    }

    public void addTheme(Theme theme) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url);
        String INSERT_THEME = """
                INSERT INTO themes(themeName)
                VALUES (?)
                """;
        PreparedStatement addThemeStatement = connection.prepareStatement(INSERT_THEME);
        addThemeStatement.setString(1, theme.getName());
        addThemeStatement.execute();
        connection.close();
    }

    public Theme getTheme(Long id) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url);
        String SELECT_THEME_BY_ID = """
                SELECT themeName FROM themes WHERE themeId = ?
                """;
        PreparedStatement getThemeStatement = connection.prepareStatement(SELECT_THEME_BY_ID);
        getThemeStatement.setLong(1, id);
        ResultSet resultSet = getThemeStatement.executeQuery();
        connection.close();
        return new Theme(
                null,
                resultSet.getString("themeName")
        );
    }

    public void assignTheme(Long userId, Long themeId) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url);
        String ADD_REPORT_TO_USER = """
                INSERT INTO reports(userID,status,themeId)
                VALUES (?,?,?)
                """;
        PreparedStatement assignStatement = connection.prepareStatement(ADD_REPORT_TO_USER);
        assignStatement.setLong(1, userId);
        assignStatement.setString(2, Status.InWORK.toString());
        assignStatement.setLong(3, themeId);
        assignStatement.execute();
        connection.close();
    }

    public void changeReportStatus(Status status, Long reportId) throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        String CHANGE_STATUS = """
                UPDATE reports SET status = ? WHERE reportId = ?
                """;
        PreparedStatement changeStatusStatement = connection.prepareStatement(CHANGE_STATUS);
        changeStatusStatement.setString(1, status.toString());
        changeStatusStatement.setLong(2, reportId);
        changeStatusStatement.execute();
        connection.close();
    }

//    public void setConnection(Connection connection) {
//        this.connection = connection;
//    }

//    public void closeConnection() throws SQLException {
//        this.connection.close();
//    }
}
